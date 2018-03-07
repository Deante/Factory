/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';

import { FactoryTestModule } from '../../../test.module';
import { DisponibiliteDetailComponent } from '../../../../../../main/webapp/app/entities/disponibilite/disponibilite-detail.component';
import { DisponibiliteService } from '../../../../../../main/webapp/app/entities/disponibilite/disponibilite.service';
import { Disponibilite } from '../../../../../../main/webapp/app/entities/disponibilite/disponibilite.model';

describe('Component Tests', () => {

    describe('Disponibilite Management Detail Component', () => {
        let comp: DisponibiliteDetailComponent;
        let fixture: ComponentFixture<DisponibiliteDetailComponent>;
        let service: DisponibiliteService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [FactoryTestModule],
                declarations: [DisponibiliteDetailComponent],
                providers: [
                    DisponibiliteService
                ]
            })
            .overrideTemplate(DisponibiliteDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(DisponibiliteDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DisponibiliteService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new Disponibilite(123)));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.disponibilite).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
