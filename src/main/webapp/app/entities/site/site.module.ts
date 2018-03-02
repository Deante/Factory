import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FactorySharedModule } from '../../shared';
import {
    SiteService,
    SitePopupService,
    SiteComponent,
    SiteDetailComponent,
    SiteDialogComponent,
    SitePopupComponent,
    SiteDeletePopupComponent,
    SiteDeleteDialogComponent,
    siteRoute,
    sitePopupRoute,
} from './';

const ENTITY_STATES = [
    ...siteRoute,
    ...sitePopupRoute,
];

@NgModule({
    imports: [
        FactorySharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        SiteComponent,
        SiteDetailComponent,
        SiteDialogComponent,
        SiteDeleteDialogComponent,
        SitePopupComponent,
        SiteDeletePopupComponent,
    ],
    entryComponents: [
        SiteComponent,
        SiteDialogComponent,
        SitePopupComponent,
        SiteDeleteDialogComponent,
        SiteDeletePopupComponent,
    ],
    providers: [
        SiteService,
        SitePopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class FactorySiteModule {}
