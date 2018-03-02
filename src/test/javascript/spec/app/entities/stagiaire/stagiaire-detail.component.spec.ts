/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';

import { FactoryTestModule } from '../../../test.module';
import { StagiaireDetailComponent } from '../../../../../../main/webapp/app/entities/stagiaire/stagiaire-detail.component';
import { StagiaireService } from '../../../../../../main/webapp/app/entities/stagiaire/stagiaire.service';
import { Stagiaire } from '../../../../../../main/webapp/app/entities/stagiaire/stagiaire.model';

describe('Component Tests', () => {

    describe('Stagiaire Management Detail Component', () => {
        let comp: StagiaireDetailComponent;
        let fixture: ComponentFixture<StagiaireDetailComponent>;
        let service: StagiaireService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [FactoryTestModule],
                declarations: [StagiaireDetailComponent],
                providers: [
                    StagiaireService
                ]
            })
            .overrideTemplate(StagiaireDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(StagiaireDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(StagiaireService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new Stagiaire(123)));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.stagiaire).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
