import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FactorySharedModule } from '../../shared';
import {
    DepartementService,
    DepartementPopupService,
    DepartementComponent,
    DepartementDetailComponent,
    DepartementDialogComponent,
    DepartementPopupComponent,
    DepartementDeletePopupComponent,
    DepartementDeleteDialogComponent,
    departementRoute,
    departementPopupRoute,
} from './';

const ENTITY_STATES = [
    ...departementRoute,
    ...departementPopupRoute,
];

@NgModule({
    imports: [
        FactorySharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        DepartementComponent,
        DepartementDetailComponent,
        DepartementDialogComponent,
        DepartementDeleteDialogComponent,
        DepartementPopupComponent,
        DepartementDeletePopupComponent,
    ],
    entryComponents: [
        DepartementComponent,
        DepartementDialogComponent,
        DepartementPopupComponent,
        DepartementDeleteDialogComponent,
        DepartementDeletePopupComponent,
    ],
    providers: [
        DepartementService,
        DepartementPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class FactoryDepartementModule {}
