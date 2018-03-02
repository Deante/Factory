import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FactorySharedModule } from '../../shared';
import { FactoryAdminModule } from '../../admin/admin.module';
import {
    TechnicienService,
    TechnicienPopupService,
    TechnicienComponent,
    TechnicienDetailComponent,
    TechnicienDialogComponent,
    TechnicienPopupComponent,
    TechnicienDeletePopupComponent,
    TechnicienDeleteDialogComponent,
    technicienRoute,
    technicienPopupRoute,
} from './';

const ENTITY_STATES = [
    ...technicienRoute,
    ...technicienPopupRoute,
];

@NgModule({
    imports: [
        FactorySharedModule,
        FactoryAdminModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        TechnicienComponent,
        TechnicienDetailComponent,
        TechnicienDialogComponent,
        TechnicienDeleteDialogComponent,
        TechnicienPopupComponent,
        TechnicienDeletePopupComponent,
    ],
    entryComponents: [
        TechnicienComponent,
        TechnicienDialogComponent,
        TechnicienPopupComponent,
        TechnicienDeleteDialogComponent,
        TechnicienDeletePopupComponent,
    ],
    providers: [
        TechnicienService,
        TechnicienPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class FactoryTechnicienModule {}
