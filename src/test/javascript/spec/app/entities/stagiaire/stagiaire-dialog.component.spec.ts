/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { FactoryTestModule } from '../../../test.module';
import { StagiaireDialogComponent } from '../../../../../../main/webapp/app/entities/stagiaire/stagiaire-dialog.component';
import { StagiaireService } from '../../../../../../main/webapp/app/entities/stagiaire/stagiaire.service';
import { Stagiaire } from '../../../../../../main/webapp/app/entities/stagiaire/stagiaire.model';
import { OrdinateurService } from '../../../../../../main/webapp/app/entities/ordinateur';
import { FormationService } from '../../../../../../main/webapp/app/entities/formation';

describe('Component Tests', () => {

    describe('Stagiaire Management Dialog Component', () => {
        let comp: StagiaireDialogComponent;
        let fixture: ComponentFixture<StagiaireDialogComponent>;
        let service: StagiaireService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [FactoryTestModule],
                declarations: [StagiaireDialogComponent],
                providers: [
                    OrdinateurService,
                    FormationService,
                    StagiaireService
                ]
            })
            .overrideTemplate(StagiaireDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(StagiaireDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(StagiaireService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new Stagiaire(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(entity));
                        comp.stagiaire = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'stagiaireListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new Stagiaire();
                        spyOn(service, 'create').and.returnValue(Observable.of(entity));
                        comp.stagiaire = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'stagiaireListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
