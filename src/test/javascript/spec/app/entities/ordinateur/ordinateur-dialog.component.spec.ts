/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { FactoryTestModule } from '../../../test.module';
import { OrdinateurDialogComponent } from '../../../../../../main/webapp/app/entities/ordinateur/ordinateur-dialog.component';
import { OrdinateurService } from '../../../../../../main/webapp/app/entities/ordinateur/ordinateur.service';
import { Ordinateur } from '../../../../../../main/webapp/app/entities/ordinateur/ordinateur.model';

describe('Component Tests', () => {

    describe('Ordinateur Management Dialog Component', () => {
        let comp: OrdinateurDialogComponent;
        let fixture: ComponentFixture<OrdinateurDialogComponent>;
        let service: OrdinateurService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [FactoryTestModule],
                declarations: [OrdinateurDialogComponent],
                providers: [
                    OrdinateurService
                ]
            })
            .overrideTemplate(OrdinateurDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(OrdinateurDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(OrdinateurService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new Ordinateur(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(entity));
                        comp.ordinateur = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'ordinateurListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new Ordinateur();
                        spyOn(service, 'create').and.returnValue(Observable.of(entity));
                        comp.ordinateur = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'ordinateurListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
