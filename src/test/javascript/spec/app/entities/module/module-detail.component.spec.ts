/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';

import { FactoryTestModule } from '../../../test.module';
import { ModuleDetailComponent } from '../../../../../../main/webapp/app/entities/module/module-detail.component';
import { ModuleService } from '../../../../../../main/webapp/app/entities/module/module.service';
import { Module } from '../../../../../../main/webapp/app/entities/module/module.model';

describe('Component Tests', () => {

    describe('Module Management Detail Component', () => {
        let comp: ModuleDetailComponent;
        let fixture: ComponentFixture<ModuleDetailComponent>;
        let service: ModuleService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [FactoryTestModule],
                declarations: [ModuleDetailComponent],
                providers: [
                    ModuleService
                ]
            })
            .overrideTemplate(ModuleDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ModuleDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ModuleService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new Module(123)));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.module).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
