/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { Headers } from '@angular/http';

import { FactoryTestModule } from '../../../test.module';
import { GestionnaireComponent } from '../../../../../../main/webapp/app/entities/gestionnaire/gestionnaire.component';
import { GestionnaireService } from '../../../../../../main/webapp/app/entities/gestionnaire/gestionnaire.service';
import { Gestionnaire } from '../../../../../../main/webapp/app/entities/gestionnaire/gestionnaire.model';

describe('Component Tests', () => {

    describe('Gestionnaire Management Component', () => {
        let comp: GestionnaireComponent;
        let fixture: ComponentFixture<GestionnaireComponent>;
        let service: GestionnaireService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [FactoryTestModule],
                declarations: [GestionnaireComponent],
                providers: [
                    GestionnaireService
                ]
            })
            .overrideTemplate(GestionnaireComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(GestionnaireComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(GestionnaireService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new Headers();
                headers.append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of({
                    json: [new Gestionnaire(123)],
                    headers
                }));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.gestionnaires[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
