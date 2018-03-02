/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { FactoryTestModule } from '../../../test.module';
import { FormationDialogComponent } from '../../../../../../main/webapp/app/entities/formation/formation-dialog.component';
import { FormationService } from '../../../../../../main/webapp/app/entities/formation/formation.service';
import { Formation } from '../../../../../../main/webapp/app/entities/formation/formation.model';
import { DepartementService } from '../../../../../../main/webapp/app/entities/departement';
import { FormateurService } from '../../../../../../main/webapp/app/entities/formateur';
import { GestionnaireService } from '../../../../../../main/webapp/app/entities/gestionnaire';
import { SalleService } from '../../../../../../main/webapp/app/entities/salle';
import { TechnicienService } from '../../../../../../main/webapp/app/entities/technicien';
import { ModuleService } from '../../../../../../main/webapp/app/entities/module';

describe('Component Tests', () => {

    describe('Formation Management Dialog Component', () => {
        let comp: FormationDialogComponent;
        let fixture: ComponentFixture<FormationDialogComponent>;
        let service: FormationService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [FactoryTestModule],
                declarations: [FormationDialogComponent],
                providers: [
                    DepartementService,
                    FormateurService,
                    GestionnaireService,
                    SalleService,
                    TechnicienService,
                    ModuleService,
                    FormationService
                ]
            })
            .overrideTemplate(FormationDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(FormationDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(FormationService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new Formation(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(entity));
                        comp.formation = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'formationListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new Formation();
                        spyOn(service, 'create').and.returnValue(Observable.of(entity));
                        comp.formation = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'formationListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
