/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';

import { FactoryTestModule } from '../../../test.module';
import { OrdinateurDetailComponent } from '../../../../../../main/webapp/app/entities/ordinateur/ordinateur-detail.component';
import { OrdinateurService } from '../../../../../../main/webapp/app/entities/ordinateur/ordinateur.service';
import { Ordinateur } from '../../../../../../main/webapp/app/entities/ordinateur/ordinateur.model';

describe('Component Tests', () => {

    describe('Ordinateur Management Detail Component', () => {
        let comp: OrdinateurDetailComponent;
        let fixture: ComponentFixture<OrdinateurDetailComponent>;
        let service: OrdinateurService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [FactoryTestModule],
                declarations: [OrdinateurDetailComponent],
                providers: [
                    OrdinateurService
                ]
            })
            .overrideTemplate(OrdinateurDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(OrdinateurDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(OrdinateurService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new Ordinateur(123)));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.ordinateur).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
