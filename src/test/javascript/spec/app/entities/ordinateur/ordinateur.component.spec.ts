/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { Headers } from '@angular/http';

import { FactoryTestModule } from '../../../test.module';
import { OrdinateurComponent } from '../../../../../../main/webapp/app/entities/ordinateur/ordinateur.component';
import { OrdinateurService } from '../../../../../../main/webapp/app/entities/ordinateur/ordinateur.service';
import { Ordinateur } from '../../../../../../main/webapp/app/entities/ordinateur/ordinateur.model';

describe('Component Tests', () => {

    describe('Ordinateur Management Component', () => {
        let comp: OrdinateurComponent;
        let fixture: ComponentFixture<OrdinateurComponent>;
        let service: OrdinateurService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [FactoryTestModule],
                declarations: [OrdinateurComponent],
                providers: [
                    OrdinateurService
                ]
            })
            .overrideTemplate(OrdinateurComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(OrdinateurComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(OrdinateurService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new Headers();
                headers.append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of({
                    json: [new Ordinateur(123)],
                    headers
                }));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.ordinateurs[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
