import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { CompetenceComponent } from './competence.component';
import { CompetenceDetailComponent } from './competence-detail.component';
import { CompetencePopupComponent } from './competence-dialog.component';
import { CompetenceDeletePopupComponent } from './competence-delete-dialog.component';

export const competenceRoute: Routes = [
    {
        path: 'competence',
        component: CompetenceComponent,
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'factoryApp.competence.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'competence/:id',
        component: CompetenceDetailComponent,
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'factoryApp.competence.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const competencePopupRoute: Routes = [
    {
        path: 'competence-new',
        component: CompetencePopupComponent,
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'factoryApp.competence.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'competence/:id/edit',
        component: CompetencePopupComponent,
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'factoryApp.competence.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'competence/:id/delete',
        component: CompetenceDeletePopupComponent,
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'factoryApp.competence.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
