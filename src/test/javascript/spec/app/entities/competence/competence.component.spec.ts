/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { Headers } from '@angular/http';

import { FactoryTestModule } from '../../../test.module';
import { CompetenceComponent } from '../../../../../../main/webapp/app/entities/competence/competence.component';
import { CompetenceService } from '../../../../../../main/webapp/app/entities/competence/competence.service';
import { Competence } from '../../../../../../main/webapp/app/entities/competence/competence.model';

describe('Component Tests', () => {

    describe('Competence Management Component', () => {
        let comp: CompetenceComponent;
        let fixture: ComponentFixture<CompetenceComponent>;
        let service: CompetenceService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [FactoryTestModule],
                declarations: [CompetenceComponent],
                providers: [
                    CompetenceService
                ]
            })
            .overrideTemplate(CompetenceComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CompetenceComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CompetenceService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new Headers();
                headers.append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of({
                    json: [new Competence(123)],
                    headers
                }));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.competences[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
