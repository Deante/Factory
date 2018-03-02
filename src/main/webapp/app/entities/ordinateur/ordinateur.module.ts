import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FactorySharedModule } from '../../shared';
import {
    OrdinateurService,
    OrdinateurPopupService,
    OrdinateurComponent,
    OrdinateurDetailComponent,
    OrdinateurDialogComponent,
    OrdinateurPopupComponent,
    OrdinateurDeletePopupComponent,
    OrdinateurDeleteDialogComponent,
    ordinateurRoute,
    ordinateurPopupRoute,
} from './';

const ENTITY_STATES = [
    ...ordinateurRoute,
    ...ordinateurPopupRoute,
];

@NgModule({
    imports: [
        FactorySharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        OrdinateurComponent,
        OrdinateurDetailComponent,
        OrdinateurDialogComponent,
        OrdinateurDeleteDialogComponent,
        OrdinateurPopupComponent,
        OrdinateurDeletePopupComponent,
    ],
    entryComponents: [
        OrdinateurComponent,
        OrdinateurDialogComponent,
        OrdinateurPopupComponent,
        OrdinateurDeleteDialogComponent,
        OrdinateurDeletePopupComponent,
    ],
    providers: [
        OrdinateurService,
        OrdinateurPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class FactoryOrdinateurModule {}
