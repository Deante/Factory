/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';

import { FactoryTestModule } from '../../../test.module';
import { TechnicienDetailComponent } from '../../../../../../main/webapp/app/entities/technicien/technicien-detail.component';
import { TechnicienService } from '../../../../../../main/webapp/app/entities/technicien/technicien.service';
import { Technicien } from '../../../../../../main/webapp/app/entities/technicien/technicien.model';

describe('Component Tests', () => {

    describe('Technicien Management Detail Component', () => {
        let comp: TechnicienDetailComponent;
        let fixture: ComponentFixture<TechnicienDetailComponent>;
        let service: TechnicienService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [FactoryTestModule],
                declarations: [TechnicienDetailComponent],
                providers: [
                    TechnicienService
                ]
            })
            .overrideTemplate(TechnicienDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TechnicienDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TechnicienService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new Technicien(123)));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.technicien).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
