/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { FactoryTestModule } from '../../../test.module';
import { DisponibiliteDeleteDialogComponent } from '../../../../../../main/webapp/app/entities/disponibilite/disponibilite-delete-dialog.component';
import { DisponibiliteService } from '../../../../../../main/webapp/app/entities/disponibilite/disponibilite.service';

describe('Component Tests', () => {

    describe('Disponibilite Management Delete Component', () => {
        let comp: DisponibiliteDeleteDialogComponent;
        let fixture: ComponentFixture<DisponibiliteDeleteDialogComponent>;
        let service: DisponibiliteService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [FactoryTestModule],
                declarations: [DisponibiliteDeleteDialogComponent],
                providers: [
                    DisponibiliteService
                ]
            })
            .overrideTemplate(DisponibiliteDeleteDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(DisponibiliteDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DisponibiliteService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        spyOn(service, 'delete').and.returnValue(Observable.of({}));

                        // WHEN
                        comp.confirmDelete(123);
                        tick();

                        // THEN
                        expect(service.delete).toHaveBeenCalledWith(123);
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
