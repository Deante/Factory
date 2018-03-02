/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { FactoryTestModule } from '../../../test.module';
import { GestionnaireDialogComponent } from '../../../../../../main/webapp/app/entities/gestionnaire/gestionnaire-dialog.component';
import { GestionnaireService } from '../../../../../../main/webapp/app/entities/gestionnaire/gestionnaire.service';
import { Gestionnaire } from '../../../../../../main/webapp/app/entities/gestionnaire/gestionnaire.model';
import { UserService } from '../../../../../../main/webapp/app/shared';

describe('Component Tests', () => {

    describe('Gestionnaire Management Dialog Component', () => {
        let comp: GestionnaireDialogComponent;
        let fixture: ComponentFixture<GestionnaireDialogComponent>;
        let service: GestionnaireService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [FactoryTestModule],
                declarations: [GestionnaireDialogComponent],
                providers: [
                    UserService,
                    GestionnaireService
                ]
            })
            .overrideTemplate(GestionnaireDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(GestionnaireDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(GestionnaireService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new Gestionnaire(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(entity));
                        comp.gestionnaire = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'gestionnaireListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new Gestionnaire();
                        spyOn(service, 'create').and.returnValue(Observable.of(entity));
                        comp.gestionnaire = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'gestionnaireListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
