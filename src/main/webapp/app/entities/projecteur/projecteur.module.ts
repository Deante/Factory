import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FactorySharedModule } from '../../shared';
import {
    ProjecteurService,
    ProjecteurPopupService,
    ProjecteurComponent,
    ProjecteurDetailComponent,
    ProjecteurDialogComponent,
    ProjecteurPopupComponent,
    ProjecteurDeletePopupComponent,
    ProjecteurDeleteDialogComponent,
    projecteurRoute,
    projecteurPopupRoute,
} from './';

const ENTITY_STATES = [
    ...projecteurRoute,
    ...projecteurPopupRoute,
];

@NgModule({
    imports: [
        FactorySharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        ProjecteurComponent,
        ProjecteurDetailComponent,
        ProjecteurDialogComponent,
        ProjecteurDeleteDialogComponent,
        ProjecteurPopupComponent,
        ProjecteurDeletePopupComponent,
    ],
    entryComponents: [
        ProjecteurComponent,
        ProjecteurDialogComponent,
        ProjecteurPopupComponent,
        ProjecteurDeleteDialogComponent,
        ProjecteurDeletePopupComponent,
    ],
    providers: [
        ProjecteurService,
        ProjecteurPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class FactoryProjecteurModule {}
