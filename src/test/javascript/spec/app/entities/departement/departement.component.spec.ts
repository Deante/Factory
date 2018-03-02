/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { Headers } from '@angular/http';

import { FactoryTestModule } from '../../../test.module';
import { DepartementComponent } from '../../../../../../main/webapp/app/entities/departement/departement.component';
import { DepartementService } from '../../../../../../main/webapp/app/entities/departement/departement.service';
import { Departement } from '../../../../../../main/webapp/app/entities/departement/departement.model';

describe('Component Tests', () => {

    describe('Departement Management Component', () => {
        let comp: DepartementComponent;
        let fixture: ComponentFixture<DepartementComponent>;
        let service: DepartementService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [FactoryTestModule],
                declarations: [DepartementComponent],
                providers: [
                    DepartementService
                ]
            })
            .overrideTemplate(DepartementComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(DepartementComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DepartementService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new Headers();
                headers.append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of({
                    json: [new Departement(123)],
                    headers
                }));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.departements[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
