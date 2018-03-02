/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { FactoryTestModule } from '../../../test.module';
import { DepartementDialogComponent } from '../../../../../../main/webapp/app/entities/departement/departement-dialog.component';
import { DepartementService } from '../../../../../../main/webapp/app/entities/departement/departement.service';
import { Departement } from '../../../../../../main/webapp/app/entities/departement/departement.model';
import { SiteService } from '../../../../../../main/webapp/app/entities/site';

describe('Component Tests', () => {

    describe('Departement Management Dialog Component', () => {
        let comp: DepartementDialogComponent;
        let fixture: ComponentFixture<DepartementDialogComponent>;
        let service: DepartementService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [FactoryTestModule],
                declarations: [DepartementDialogComponent],
                providers: [
                    SiteService,
                    DepartementService
                ]
            })
            .overrideTemplate(DepartementDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(DepartementDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DepartementService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new Departement(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(entity));
                        comp.departement = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'departementListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new Departement();
                        spyOn(service, 'create').and.returnValue(Observable.of(entity));
                        comp.departement = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'departementListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
