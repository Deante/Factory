/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { Headers } from '@angular/http';

import { FactoryTestModule } from '../../../test.module';
import { FormationComponent } from '../../../../../../main/webapp/app/entities/formation/formation.component';
import { FormationService } from '../../../../../../main/webapp/app/entities/formation/formation.service';
import { Formation } from '../../../../../../main/webapp/app/entities/formation/formation.model';

describe('Component Tests', () => {

    describe('Formation Management Component', () => {
        let comp: FormationComponent;
        let fixture: ComponentFixture<FormationComponent>;
        let service: FormationService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [FactoryTestModule],
                declarations: [FormationComponent],
                providers: [
                    FormationService
                ]
            })
            .overrideTemplate(FormationComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(FormationComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(FormationService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new Headers();
                headers.append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of({
                    json: [new Formation(123)],
                    headers
                }));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.formations[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
