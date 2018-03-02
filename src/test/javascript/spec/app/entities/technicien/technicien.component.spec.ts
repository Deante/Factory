/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { Headers } from '@angular/http';

import { FactoryTestModule } from '../../../test.module';
import { TechnicienComponent } from '../../../../../../main/webapp/app/entities/technicien/technicien.component';
import { TechnicienService } from '../../../../../../main/webapp/app/entities/technicien/technicien.service';
import { Technicien } from '../../../../../../main/webapp/app/entities/technicien/technicien.model';

describe('Component Tests', () => {

    describe('Technicien Management Component', () => {
        let comp: TechnicienComponent;
        let fixture: ComponentFixture<TechnicienComponent>;
        let service: TechnicienService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [FactoryTestModule],
                declarations: [TechnicienComponent],
                providers: [
                    TechnicienService
                ]
            })
            .overrideTemplate(TechnicienComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TechnicienComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TechnicienService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new Headers();
                headers.append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of({
                    json: [new Technicien(123)],
                    headers
                }));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.techniciens[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
