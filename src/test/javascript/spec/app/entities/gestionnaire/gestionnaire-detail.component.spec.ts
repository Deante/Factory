/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';

import { FactoryTestModule } from '../../../test.module';
import { GestionnaireDetailComponent } from '../../../../../../main/webapp/app/entities/gestionnaire/gestionnaire-detail.component';
import { GestionnaireService } from '../../../../../../main/webapp/app/entities/gestionnaire/gestionnaire.service';
import { Gestionnaire } from '../../../../../../main/webapp/app/entities/gestionnaire/gestionnaire.model';

describe('Component Tests', () => {

    describe('Gestionnaire Management Detail Component', () => {
        let comp: GestionnaireDetailComponent;
        let fixture: ComponentFixture<GestionnaireDetailComponent>;
        let service: GestionnaireService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [FactoryTestModule],
                declarations: [GestionnaireDetailComponent],
                providers: [
                    GestionnaireService
                ]
            })
            .overrideTemplate(GestionnaireDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(GestionnaireDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(GestionnaireService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new Gestionnaire(123)));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.gestionnaire).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
