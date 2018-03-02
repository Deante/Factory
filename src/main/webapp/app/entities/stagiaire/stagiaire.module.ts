import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FactorySharedModule } from '../../shared';
import { FactoryAdminModule } from '../../admin/admin.module';
import {
    StagiaireService,
    StagiairePopupService,
    StagiaireComponent,
    StagiaireDetailComponent,
    StagiaireDialogComponent,
    StagiairePopupComponent,
    StagiaireDeletePopupComponent,
    StagiaireDeleteDialogComponent,
    stagiaireRoute,
    stagiairePopupRoute,
} from './';

const ENTITY_STATES = [
    ...stagiaireRoute,
    ...stagiairePopupRoute,
];

@NgModule({
    imports: [
        FactorySharedModule,
        FactoryAdminModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        StagiaireComponent,
        StagiaireDetailComponent,
        StagiaireDialogComponent,
        StagiaireDeleteDialogComponent,
        StagiairePopupComponent,
        StagiaireDeletePopupComponent,
    ],
    entryComponents: [
        StagiaireComponent,
        StagiaireDialogComponent,
        StagiairePopupComponent,
        StagiaireDeleteDialogComponent,
        StagiaireDeletePopupComponent,
    ],
    providers: [
        StagiaireService,
        StagiairePopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class FactoryStagiaireModule {}
