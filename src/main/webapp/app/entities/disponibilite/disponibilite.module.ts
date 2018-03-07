import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FactorySharedModule } from '../../shared';
import {
    DisponibiliteService,
    DisponibilitePopupService,
    DisponibiliteComponent,
    DisponibiliteDetailComponent,
    DisponibiliteDialogComponent,
    DisponibilitePopupComponent,
    DisponibiliteDeletePopupComponent,
    DisponibiliteDeleteDialogComponent,
    disponibiliteRoute,
    disponibilitePopupRoute,
} from './';

const ENTITY_STATES = [
    ...disponibiliteRoute,
    ...disponibilitePopupRoute,
];

@NgModule({
    imports: [
        FactorySharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        DisponibiliteComponent,
        DisponibiliteDetailComponent,
        DisponibiliteDialogComponent,
        DisponibiliteDeleteDialogComponent,
        DisponibilitePopupComponent,
        DisponibiliteDeletePopupComponent,
    ],
    entryComponents: [
        DisponibiliteComponent,
        DisponibiliteDialogComponent,
        DisponibilitePopupComponent,
        DisponibiliteDeleteDialogComponent,
        DisponibiliteDeletePopupComponent,
    ],
    providers: [
        DisponibiliteService,
        DisponibilitePopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class FactoryDisponibiliteModule {}
