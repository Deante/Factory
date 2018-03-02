/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';

import { FactoryTestModule } from '../../../test.module';
import { CompetenceDetailComponent } from '../../../../../../main/webapp/app/entities/competence/competence-detail.component';
import { CompetenceService } from '../../../../../../main/webapp/app/entities/competence/competence.service';
import { Competence } from '../../../../../../main/webapp/app/entities/competence/competence.model';

describe('Component Tests', () => {

    describe('Competence Management Detail Component', () => {
        let comp: CompetenceDetailComponent;
        let fixture: ComponentFixture<CompetenceDetailComponent>;
        let service: CompetenceService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [FactoryTestModule],
                declarations: [CompetenceDetailComponent],
                providers: [
                    CompetenceService
                ]
            })
            .overrideTemplate(CompetenceDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CompetenceDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CompetenceService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new Competence(123)));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.competence).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
