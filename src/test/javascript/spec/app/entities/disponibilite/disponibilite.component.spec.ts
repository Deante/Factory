/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { Headers } from '@angular/http';

import { FactoryTestModule } from '../../../test.module';
import { DisponibiliteComponent } from '../../../../../../main/webapp/app/entities/disponibilite/disponibilite.component';
import { DisponibiliteService } from '../../../../../../main/webapp/app/entities/disponibilite/disponibilite.service';
import { Disponibilite } from '../../../../../../main/webapp/app/entities/disponibilite/disponibilite.model';

describe('Component Tests', () => {

    describe('Disponibilite Management Component', () => {
        let comp: DisponibiliteComponent;
        let fixture: ComponentFixture<DisponibiliteComponent>;
        let service: DisponibiliteService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [FactoryTestModule],
                declarations: [DisponibiliteComponent],
                providers: [
                    DisponibiliteService
                ]
            })
            .overrideTemplate(DisponibiliteComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(DisponibiliteComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DisponibiliteService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new Headers();
                headers.append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of({
                    json: [new Disponibilite(123)],
                    headers
                }));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.disponibilites[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
