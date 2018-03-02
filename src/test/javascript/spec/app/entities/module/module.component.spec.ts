/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { Headers } from '@angular/http';

import { FactoryTestModule } from '../../../test.module';
import { ModuleComponent } from '../../../../../../main/webapp/app/entities/module/module.component';
import { ModuleService } from '../../../../../../main/webapp/app/entities/module/module.service';
import { Module } from '../../../../../../main/webapp/app/entities/module/module.model';

describe('Component Tests', () => {

    describe('Module Management Component', () => {
        let comp: ModuleComponent;
        let fixture: ComponentFixture<ModuleComponent>;
        let service: ModuleService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [FactoryTestModule],
                declarations: [ModuleComponent],
                providers: [
                    ModuleService
                ]
            })
            .overrideTemplate(ModuleComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ModuleComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ModuleService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new Headers();
                headers.append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of({
                    json: [new Module(123)],
                    headers
                }));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.modules[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
