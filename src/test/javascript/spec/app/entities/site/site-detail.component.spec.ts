/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';

import { FactoryTestModule } from '../../../test.module';
import { SiteDetailComponent } from '../../../../../../main/webapp/app/entities/site/site-detail.component';
import { SiteService } from '../../../../../../main/webapp/app/entities/site/site.service';
import { Site } from '../../../../../../main/webapp/app/entities/site/site.model';

describe('Component Tests', () => {

    describe('Site Management Detail Component', () => {
        let comp: SiteDetailComponent;
        let fixture: ComponentFixture<SiteDetailComponent>;
        let service: SiteService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [FactoryTestModule],
                declarations: [SiteDetailComponent],
                providers: [
                    SiteService
                ]
            })
            .overrideTemplate(SiteDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(SiteDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SiteService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new Site(123)));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.site).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
