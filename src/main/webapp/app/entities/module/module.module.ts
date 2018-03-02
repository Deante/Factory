import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FactorySharedModule } from '../../shared';
import {
    ModuleService,
    ModulePopupService,
    ModuleComponent,
    ModuleDetailComponent,
    ModuleDialogComponent,
    ModulePopupComponent,
    ModuleDeletePopupComponent,
    ModuleDeleteDialogComponent,
    moduleRoute,
    modulePopupRoute,
} from './';

const ENTITY_STATES = [
    ...moduleRoute,
    ...modulePopupRoute,
];

@NgModule({
    imports: [
        FactorySharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        ModuleComponent,
        ModuleDetailComponent,
        ModuleDialogComponent,
        ModuleDeleteDialogComponent,
        ModulePopupComponent,
        ModuleDeletePopupComponent,
    ],
    entryComponents: [
        ModuleComponent,
        ModuleDialogComponent,
        ModulePopupComponent,
        ModuleDeleteDialogComponent,
        ModuleDeletePopupComponent,
    ],
    providers: [
        ModuleService,
        ModulePopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class FactoryModuleModule {}
