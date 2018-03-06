import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FactorySharedModule } from '../../shared';
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
