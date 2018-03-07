import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FactorySharedModule } from '../../shared';
import {
    FormationService,
    FormationPopupService,
    FormationComponent,
    FormationDetailComponent,
    FormationDialogComponent,
    FormationPopupComponent,
    FormationDeletePopupComponent,
    FormationDeleteDialogComponent,
    FormationPdfComponent,
    formationRoute,
    formationPopupRoute,
} from './';

const ENTITY_STATES = [
    ...formationRoute,
    ...formationPopupRoute,
];

@NgModule({
    imports: [
        FactorySharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        FormationComponent,
        FormationDetailComponent,
        FormationDialogComponent,
        FormationDeleteDialogComponent,
        FormationPopupComponent,
        FormationDeletePopupComponent,
        FormationPdfComponent
    ],
    entryComponents: [
        FormationComponent,
        FormationDialogComponent,
        FormationPopupComponent,
        FormationDeleteDialogComponent,
        FormationDeletePopupComponent,
    ],
    providers: [
        FormationService,
        FormationPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class FactoryFormationModule {}
