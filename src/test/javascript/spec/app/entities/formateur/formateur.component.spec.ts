/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { Headers } from '@angular/http';

import { FactoryTestModule } from '../../../test.module';
import { FormateurComponent } from '../../../../../../main/webapp/app/entities/formateur/formateur.component';
import { FormateurService } from '../../../../../../main/webapp/app/entities/formateur/formateur.service';
import { Formateur } from '../../../../../../main/webapp/app/entities/formateur/formateur.model';

describe('Component Tests', () => {

    describe('Formateur Management Component', () => {
        let comp: FormateurComponent;
        let fixture: ComponentFixture<FormateurComponent>;
        let service: FormateurService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [FactoryTestModule],
                declarations: [FormateurComponent],
                providers: [
                    FormateurService
                ]
            })
            .overrideTemplate(FormateurComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(FormateurComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(FormateurService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new Headers();
                headers.append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of({
                    json: [new Formateur(123)],
                    headers
                }));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.formateurs[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
