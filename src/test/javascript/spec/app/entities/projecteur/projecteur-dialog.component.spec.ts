/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { FactoryTestModule } from '../../../test.module';
import { ProjecteurDialogComponent } from '../../../../../../main/webapp/app/entities/projecteur/projecteur-dialog.component';
import { ProjecteurService } from '../../../../../../main/webapp/app/entities/projecteur/projecteur.service';
import { Projecteur } from '../../../../../../main/webapp/app/entities/projecteur/projecteur.model';
import { SalleService } from '../../../../../../main/webapp/app/entities/salle';

describe('Component Tests', () => {

    describe('Projecteur Management Dialog Component', () => {
        let comp: ProjecteurDialogComponent;
        let fixture: ComponentFixture<ProjecteurDialogComponent>;
        let service: ProjecteurService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [FactoryTestModule],
                declarations: [ProjecteurDialogComponent],
                providers: [
                    SalleService,
                    ProjecteurService
                ]
            })
            .overrideTemplate(ProjecteurDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ProjecteurDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ProjecteurService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new Projecteur(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(entity));
                        comp.projecteur = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'projecteurListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new Projecteur();
                        spyOn(service, 'create').and.returnValue(Observable.of(entity));
                        comp.projecteur = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'projecteurListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
