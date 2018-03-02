/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';

import { FactoryTestModule } from '../../../test.module';
import { MatiereDetailComponent } from '../../../../../../main/webapp/app/entities/matiere/matiere-detail.component';
import { MatiereService } from '../../../../../../main/webapp/app/entities/matiere/matiere.service';
import { Matiere } from '../../../../../../main/webapp/app/entities/matiere/matiere.model';

describe('Component Tests', () => {

    describe('Matiere Management Detail Component', () => {
        let comp: MatiereDetailComponent;
        let fixture: ComponentFixture<MatiereDetailComponent>;
        let service: MatiereService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [FactoryTestModule],
                declarations: [MatiereDetailComponent],
                providers: [
                    MatiereService
                ]
            })
            .overrideTemplate(MatiereDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(MatiereDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MatiereService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new Matiere(123)));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.matiere).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
