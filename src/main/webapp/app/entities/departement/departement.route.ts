import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { DepartementComponent } from './departement.component';
import { DepartementDetailComponent } from './departement-detail.component';
import { DepartementPopupComponent } from './departement-dialog.component';
import { DepartementDeletePopupComponent } from './departement-delete-dialog.component';

export const departementRoute: Routes = [
    {
        path: 'departement',
        component: DepartementComponent,
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'factoryApp.departement.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'departement/:id',
        component: DepartementDetailComponent,
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'factoryApp.departement.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const departementPopupRoute: Routes = [
    {
        path: 'departement-new',
        component: DepartementPopupComponent,
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'factoryApp.departement.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'departement/:id/edit',
        component: DepartementPopupComponent,
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'factoryApp.departement.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'departement/:id/delete',
        component: DepartementDeletePopupComponent,
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'factoryApp.departement.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
