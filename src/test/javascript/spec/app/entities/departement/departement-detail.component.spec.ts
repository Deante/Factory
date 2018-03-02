/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';

import { FactoryTestModule } from '../../../test.module';
import { DepartementDetailComponent } from '../../../../../../main/webapp/app/entities/departement/departement-detail.component';
import { DepartementService } from '../../../../../../main/webapp/app/entities/departement/departement.service';
import { Departement } from '../../../../../../main/webapp/app/entities/departement/departement.model';

describe('Component Tests', () => {

    describe('Departement Management Detail Component', () => {
        let comp: DepartementDetailComponent;
        let fixture: ComponentFixture<DepartementDetailComponent>;
        let service: DepartementService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [FactoryTestModule],
                declarations: [DepartementDetailComponent],
                providers: [
                    DepartementService
                ]
            })
            .overrideTemplate(DepartementDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(DepartementDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DepartementService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new Departement(123)));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.departement).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
