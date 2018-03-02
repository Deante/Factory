import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FactorySharedModule } from '../../shared';
import { FactoryAdminModule } from '../../admin/admin.module';
import {
    GestionnaireService,
    GestionnairePopupService,
    GestionnaireComponent,
    GestionnaireDetailComponent,
    GestionnaireDialogComponent,
    GestionnairePopupComponent,
    GestionnaireDeletePopupComponent,
    GestionnaireDeleteDialogComponent,
    gestionnaireRoute,
    gestionnairePopupRoute,
} from './';

const ENTITY_STATES = [
    ...gestionnaireRoute,
    ...gestionnairePopupRoute,
];

@NgModule({
    imports: [
        FactorySharedModule,
        FactoryAdminModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        GestionnaireComponent,
        GestionnaireDetailComponent,
        GestionnaireDialogComponent,
        GestionnaireDeleteDialogComponent,
        GestionnairePopupComponent,
        GestionnaireDeletePopupComponent,
    ],
    entryComponents: [
        GestionnaireComponent,
        GestionnaireDialogComponent,
        GestionnairePopupComponent,
        GestionnaireDeleteDialogComponent,
        GestionnaireDeletePopupComponent,
    ],
    providers: [
        GestionnaireService,
        GestionnairePopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class FactoryGestionnaireModule {}
