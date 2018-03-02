import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FactorySharedModule } from '../../shared';
import {
    CompetenceService,
    CompetencePopupService,
    CompetenceComponent,
    CompetenceDetailComponent,
    CompetenceDialogComponent,
    CompetencePopupComponent,
    CompetenceDeletePopupComponent,
    CompetenceDeleteDialogComponent,
    competenceRoute,
    competencePopupRoute,
} from './';

const ENTITY_STATES = [
    ...competenceRoute,
    ...competencePopupRoute,
];

@NgModule({
    imports: [
        FactorySharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        CompetenceComponent,
        CompetenceDetailComponent,
        CompetenceDialogComponent,
        CompetenceDeleteDialogComponent,
        CompetencePopupComponent,
        CompetenceDeletePopupComponent,
    ],
    entryComponents: [
        CompetenceComponent,
        CompetenceDialogComponent,
        CompetencePopupComponent,
        CompetenceDeleteDialogComponent,
        CompetenceDeletePopupComponent,
    ],
    providers: [
        CompetenceService,
        CompetencePopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class FactoryCompetenceModule {}
