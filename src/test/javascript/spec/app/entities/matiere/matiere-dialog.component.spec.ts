/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { FactoryTestModule } from '../../../test.module';
import { MatiereDialogComponent } from '../../../../../../main/webapp/app/entities/matiere/matiere-dialog.component';
import { MatiereService } from '../../../../../../main/webapp/app/entities/matiere/matiere.service';
import { Matiere } from '../../../../../../main/webapp/app/entities/matiere/matiere.model';
import { ModuleService } from '../../../../../../main/webapp/app/entities/module';
import { CompetenceService } from '../../../../../../main/webapp/app/entities/competence';

describe('Component Tests', () => {

    describe('Matiere Management Dialog Component', () => {
        let comp: MatiereDialogComponent;
        let fixture: ComponentFixture<MatiereDialogComponent>;
        let service: MatiereService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [FactoryTestModule],
                declarations: [MatiereDialogComponent],
                providers: [
                    ModuleService,
                    CompetenceService,
                    MatiereService
                ]
            })
            .overrideTemplate(MatiereDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(MatiereDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MatiereService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new Matiere(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(entity));
                        comp.matiere = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'matiereListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new Matiere();
                        spyOn(service, 'create').and.returnValue(Observable.of(entity));
                        comp.matiere = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'matiereListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
