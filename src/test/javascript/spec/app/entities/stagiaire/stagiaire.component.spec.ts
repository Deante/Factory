/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { Headers } from '@angular/http';

import { FactoryTestModule } from '../../../test.module';
import { StagiaireComponent } from '../../../../../../main/webapp/app/entities/stagiaire/stagiaire.component';
import { StagiaireService } from '../../../../../../main/webapp/app/entities/stagiaire/stagiaire.service';
import { Stagiaire } from '../../../../../../main/webapp/app/entities/stagiaire/stagiaire.model';

describe('Component Tests', () => {

    describe('Stagiaire Management Component', () => {
        let comp: StagiaireComponent;
        let fixture: ComponentFixture<StagiaireComponent>;
        let service: StagiaireService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [FactoryTestModule],
                declarations: [StagiaireComponent],
                providers: [
                    StagiaireService
                ]
            })
            .overrideTemplate(StagiaireComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(StagiaireComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(StagiaireService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new Headers();
                headers.append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of({
                    json: [new Stagiaire(123)],
                    headers
                }));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.stagiaires[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
