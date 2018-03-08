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
            pageTitle: 'factoryApp.departement.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'departement/:id',
        component: DepartementDetailComponent,
        data: {
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
            pageTitle: 'factoryApp.departement.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'departement/:id/edit',
        component: DepartementPopupComponent,
        data: {
            pageTitle: 'factoryApp.departement.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'departement/:id/delete',
        component: DepartementDeletePopupComponent,
        data: {
            pageTitle: 'factoryApp.departement.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
