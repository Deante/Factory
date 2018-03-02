import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FactorySharedModule } from '../../shared';
import {
    MatiereService,
    MatierePopupService,
    MatiereComponent,
    MatiereDetailComponent,
    MatiereDialogComponent,
    MatierePopupComponent,
    MatiereDeletePopupComponent,
    MatiereDeleteDialogComponent,
    matiereRoute,
    matierePopupRoute,
} from './';

const ENTITY_STATES = [
    ...matiereRoute,
    ...matierePopupRoute,
];

@NgModule({
    imports: [
        FactorySharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        MatiereComponent,
        MatiereDetailComponent,
        MatiereDialogComponent,
        MatiereDeleteDialogComponent,
        MatierePopupComponent,
        MatiereDeletePopupComponent,
    ],
    entryComponents: [
        MatiereComponent,
        MatiereDialogComponent,
        MatierePopupComponent,
        MatiereDeleteDialogComponent,
        MatiereDeletePopupComponent,
    ],
    providers: [
        MatiereService,
        MatierePopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class FactoryMatiereModule {}
