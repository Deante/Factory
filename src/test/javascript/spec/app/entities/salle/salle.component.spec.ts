/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { Headers } from '@angular/http';

import { FactoryTestModule } from '../../../test.module';
import { SalleComponent } from '../../../../../../main/webapp/app/entities/salle/salle.component';
import { SalleService } from '../../../../../../main/webapp/app/entities/salle/salle.service';
import { Salle } from '../../../../../../main/webapp/app/entities/salle/salle.model';

describe('Component Tests', () => {

    describe('Salle Management Component', () => {
        let comp: SalleComponent;
        let fixture: ComponentFixture<SalleComponent>;
        let service: SalleService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [FactoryTestModule],
                declarations: [SalleComponent],
                providers: [
                    SalleService
                ]
            })
            .overrideTemplate(SalleComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(SalleComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SalleService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new Headers();
                headers.append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of({
                    json: [new Salle(123)],
                    headers
                }));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.salles[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
