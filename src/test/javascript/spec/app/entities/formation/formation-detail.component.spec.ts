/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';

import { FactoryTestModule } from '../../../test.module';
import { FormationDetailComponent } from '../../../../../../main/webapp/app/entities/formation/formation-detail.component';
import { FormationService } from '../../../../../../main/webapp/app/entities/formation/formation.service';
import { Formation } from '../../../../../../main/webapp/app/entities/formation/formation.model';

describe('Component Tests', () => {

    describe('Formation Management Detail Component', () => {
        let comp: FormationDetailComponent;
        let fixture: ComponentFixture<FormationDetailComponent>;
        let service: FormationService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [FactoryTestModule],
                declarations: [FormationDetailComponent],
                providers: [
                    FormationService
                ]
            })
            .overrideTemplate(FormationDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(FormationDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(FormationService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new Formation(123)));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.formation).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
