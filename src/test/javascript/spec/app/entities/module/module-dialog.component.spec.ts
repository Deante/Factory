/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { FactoryTestModule } from '../../../test.module';
import { ModuleDialogComponent } from '../../../../../../main/webapp/app/entities/module/module-dialog.component';
import { ModuleService } from '../../../../../../main/webapp/app/entities/module/module.service';
import { Module } from '../../../../../../main/webapp/app/entities/module/module.model';
import { FormationService } from '../../../../../../main/webapp/app/entities/formation';
import { MatiereService } from '../../../../../../main/webapp/app/entities/matiere';
import { FormateurService } from '../../../../../../main/webapp/app/entities/formateur';

describe('Component Tests', () => {

    describe('Module Management Dialog Component', () => {
        let comp: ModuleDialogComponent;
        let fixture: ComponentFixture<ModuleDialogComponent>;
        let service: ModuleService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [FactoryTestModule],
                declarations: [ModuleDialogComponent],
                providers: [
                    FormationService,
                    MatiereService,
                    FormateurService,
                    ModuleService
                ]
            })
            .overrideTemplate(ModuleDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ModuleDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ModuleService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new Module(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(entity));
                        comp.module = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'moduleListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new Module();
                        spyOn(service, 'create').and.returnValue(Observable.of(entity));
                        comp.module = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'moduleListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
