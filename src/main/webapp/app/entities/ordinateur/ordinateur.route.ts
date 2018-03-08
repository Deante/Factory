import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { OrdinateurComponent } from './ordinateur.component';
import { OrdinateurDetailComponent } from './ordinateur-detail.component';
import { OrdinateurPopupComponent } from './ordinateur-dialog.component';
import { OrdinateurDeletePopupComponent } from './ordinateur-delete-dialog.component';

export const ordinateurRoute: Routes = [
    {
        path: 'ordinateur',
        component: OrdinateurComponent,
        data: {
            authorities: ['ROLE_TECHNICIEN', 'ROLE_ADMIN'],
            pageTitle: 'factoryApp.ordinateur.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'ordinateur/:id',
        component: OrdinateurDetailComponent,
        data: {
            authorities: ['ROLE_TECHNICIEN', 'ROLE_ADMIN'],
            pageTitle: 'factoryApp.ordinateur.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const ordinateurPopupRoute: Routes = [
    {
        path: 'ordinateur-new',
        component: OrdinateurPopupComponent,
        data: {
            authorities: ['ROLE_TECHNICIEN', 'ROLE_ADMIN'],
            pageTitle: 'factoryApp.ordinateur.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'ordinateur/:id/edit',
        component: OrdinateurPopupComponent,
        data: {
            authorities: ['ROLE_TECHNICIEN', 'ROLE_ADMIN'],
            pageTitle: 'factoryApp.ordinateur.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'ordinateur/:id/delete',
        component: OrdinateurDeletePopupComponent,
        data: {
            authorities: ['ROLE_TECHNICIEN', 'ROLE_ADMIN'],
            pageTitle: 'factoryApp.ordinateur.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
