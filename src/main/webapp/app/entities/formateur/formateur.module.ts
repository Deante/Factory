import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FactorySharedModule } from '../../shared';
import { FactoryAdminModule } from '../../admin/admin.module';
import {
    FormateurService,
    FormateurPopupService,
    FormateurComponent,
    FormateurDetailComponent,
    FormateurDialogComponent,
    FormateurPopupComponent,
    FormateurDeletePopupComponent,
    FormateurDeleteDialogComponent,
    formateurRoute,
    formateurPopupRoute,
} from './';

const ENTITY_STATES = [
    ...formateurRoute,
    ...formateurPopupRoute,
];

@NgModule({
    imports: [
        FactorySharedModule,
        FactoryAdminModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        FormateurComponent,
        FormateurDetailComponent,
        FormateurDialogComponent,
        FormateurDeleteDialogComponent,
        FormateurPopupComponent,
        FormateurDeletePopupComponent,
    ],
    entryComponents: [
        FormateurComponent,
        FormateurDialogComponent,
        FormateurPopupComponent,
        FormateurDeleteDialogComponent,
        FormateurDeletePopupComponent,
    ],
    providers: [
        FormateurService,
        FormateurPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class FactoryFormateurModule {}
