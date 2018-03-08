import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { DisponibiliteComponent } from './disponibilite.component';
import { DisponibiliteDetailComponent } from './disponibilite-detail.component';
import { DisponibilitePopupComponent } from './disponibilite-dialog.component';
import { DisponibiliteDeletePopupComponent } from './disponibilite-delete-dialog.component';

export const disponibiliteRoute: Routes = [
    {
        path: 'disponibilite',
        component: DisponibiliteComponent,
        data: {
            authorities: ['ROLE_FORMATEUR'],
            pageTitle: 'factoryApp.disponibilite.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'disponibilite/:id',
        component: DisponibiliteDetailComponent,
        data: {
            authorities: ['ROLE_FORMATEUR'],
            pageTitle: 'factoryApp.disponibilite.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const disponibilitePopupRoute: Routes = [
    {
        path: 'disponibilite-new',
        component: DisponibilitePopupComponent,
        data: {
            authorities: ['ROLE_FORMATEUR'],
            pageTitle: 'factoryApp.disponibilite.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'disponibilite/:id/edit',
        component: DisponibilitePopupComponent,
        data: {
            authorities: ['ROLE_FORMATEUR'],
            pageTitle: 'factoryApp.disponibilite.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'disponibilite/:id/delete',
        component: DisponibiliteDeletePopupComponent,
        data: {
            authorities: ['ROLE_FORMATEUR'],
            pageTitle: 'factoryApp.disponibilite.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
