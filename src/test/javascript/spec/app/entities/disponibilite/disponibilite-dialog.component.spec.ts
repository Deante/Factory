/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { FactoryTestModule } from '../../../test.module';
import { DisponibiliteDialogComponent } from '../../../../../../main/webapp/app/entities/disponibilite/disponibilite-dialog.component';
import { DisponibiliteService } from '../../../../../../main/webapp/app/entities/disponibilite/disponibilite.service';
import { Disponibilite } from '../../../../../../main/webapp/app/entities/disponibilite/disponibilite.model';
import { FormateurService } from '../../../../../../main/webapp/app/entities/formateur';

describe('Component Tests', () => {

    describe('Disponibilite Management Dialog Component', () => {
        let comp: DisponibiliteDialogComponent;
        let fixture: ComponentFixture<DisponibiliteDialogComponent>;
        let service: DisponibiliteService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [FactoryTestModule],
                declarations: [DisponibiliteDialogComponent],
                providers: [
                    FormateurService,
                    DisponibiliteService
                ]
            })
            .overrideTemplate(DisponibiliteDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(DisponibiliteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DisponibiliteService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new Disponibilite(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(entity));
                        comp.disponibilite = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'disponibiliteListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new Disponibilite();
                        spyOn(service, 'create').and.returnValue(Observable.of(entity));
                        comp.disponibilite = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'disponibiliteListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
