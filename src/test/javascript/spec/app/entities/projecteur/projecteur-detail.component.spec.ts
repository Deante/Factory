/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';

import { FactoryTestModule } from '../../../test.module';
import { ProjecteurDetailComponent } from '../../../../../../main/webapp/app/entities/projecteur/projecteur-detail.component';
import { ProjecteurService } from '../../../../../../main/webapp/app/entities/projecteur/projecteur.service';
import { Projecteur } from '../../../../../../main/webapp/app/entities/projecteur/projecteur.model';

describe('Component Tests', () => {

    describe('Projecteur Management Detail Component', () => {
        let comp: ProjecteurDetailComponent;
        let fixture: ComponentFixture<ProjecteurDetailComponent>;
        let service: ProjecteurService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [FactoryTestModule],
                declarations: [ProjecteurDetailComponent],
                providers: [
                    ProjecteurService
                ]
            })
            .overrideTemplate(ProjecteurDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ProjecteurDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ProjecteurService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new Projecteur(123)));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.projecteur).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
