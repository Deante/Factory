/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { Headers } from '@angular/http';

import { FactoryTestModule } from '../../../test.module';
import { ProjecteurComponent } from '../../../../../../main/webapp/app/entities/projecteur/projecteur.component';
import { ProjecteurService } from '../../../../../../main/webapp/app/entities/projecteur/projecteur.service';
import { Projecteur } from '../../../../../../main/webapp/app/entities/projecteur/projecteur.model';

describe('Component Tests', () => {

    describe('Projecteur Management Component', () => {
        let comp: ProjecteurComponent;
        let fixture: ComponentFixture<ProjecteurComponent>;
        let service: ProjecteurService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [FactoryTestModule],
                declarations: [ProjecteurComponent],
                providers: [
                    ProjecteurService
                ]
            })
            .overrideTemplate(ProjecteurComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ProjecteurComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ProjecteurService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new Headers();
                headers.append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of({
                    json: [new Projecteur(123)],
                    headers
                }));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.projecteurs[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
