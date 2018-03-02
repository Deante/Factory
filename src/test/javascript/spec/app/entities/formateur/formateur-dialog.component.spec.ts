/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { FactoryTestModule } from '../../../test.module';
import { FormateurDialogComponent } from '../../../../../../main/webapp/app/entities/formateur/formateur-dialog.component';
import { FormateurService } from '../../../../../../main/webapp/app/entities/formateur/formateur.service';
import { Formateur } from '../../../../../../main/webapp/app/entities/formateur/formateur.model';
import { UserService } from '../../../../../../main/webapp/app/shared';
import { CompetenceService } from '../../../../../../main/webapp/app/entities/competence';
import { FormationService } from '../../../../../../main/webapp/app/entities/formation';

describe('Component Tests', () => {

    describe('Formateur Management Dialog Component', () => {
        let comp: FormateurDialogComponent;
        let fixture: ComponentFixture<FormateurDialogComponent>;
        let service: FormateurService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [FactoryTestModule],
                declarations: [FormateurDialogComponent],
                providers: [
                    UserService,
                    CompetenceService,
                    FormationService,
                    FormateurService
                ]
            })
            .overrideTemplate(FormateurDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(FormateurDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(FormateurService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new Formateur(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(entity));
                        comp.formateur = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'formateurListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new Formateur();
                        spyOn(service, 'create').and.returnValue(Observable.of(entity));
                        comp.formateur = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'formateurListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
