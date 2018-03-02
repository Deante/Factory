/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { FactoryTestModule } from '../../../test.module';
import { SalleDialogComponent } from '../../../../../../main/webapp/app/entities/salle/salle-dialog.component';
import { SalleService } from '../../../../../../main/webapp/app/entities/salle/salle.service';
import { Salle } from '../../../../../../main/webapp/app/entities/salle/salle.model';
import { ProjecteurService } from '../../../../../../main/webapp/app/entities/projecteur';

describe('Component Tests', () => {

    describe('Salle Management Dialog Component', () => {
        let comp: SalleDialogComponent;
        let fixture: ComponentFixture<SalleDialogComponent>;
        let service: SalleService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [FactoryTestModule],
                declarations: [SalleDialogComponent],
                providers: [
                    ProjecteurService,
                    SalleService
                ]
            })
            .overrideTemplate(SalleDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(SalleDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SalleService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new Salle(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(entity));
                        comp.salle = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'salleListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new Salle();
                        spyOn(service, 'create').and.returnValue(Observable.of(entity));
                        comp.salle = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'salleListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
