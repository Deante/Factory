/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';

import { FactoryTestModule } from '../../../test.module';
import { FormateurDetailComponent } from '../../../../../../main/webapp/app/entities/formateur/formateur-detail.component';
import { FormateurService } from '../../../../../../main/webapp/app/entities/formateur/formateur.service';
import { Formateur } from '../../../../../../main/webapp/app/entities/formateur/formateur.model';

describe('Component Tests', () => {

    describe('Formateur Management Detail Component', () => {
        let comp: FormateurDetailComponent;
        let fixture: ComponentFixture<FormateurDetailComponent>;
        let service: FormateurService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [FactoryTestModule],
                declarations: [FormateurDetailComponent],
                providers: [
                    FormateurService
                ]
            })
            .overrideTemplate(FormateurDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(FormateurDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(FormateurService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new Formateur(123)));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.formateur).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
