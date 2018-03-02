import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { SalleComponent } from './salle.component';
import { SalleDetailComponent } from './salle-detail.component';
import { SallePopupComponent } from './salle-dialog.component';
import { SalleDeletePopupComponent } from './salle-delete-dialog.component';

export const salleRoute: Routes = [
    {
        path: 'salle',
        component: SalleComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'factoryApp.salle.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'salle/:id',
        component: SalleDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'factoryApp.salle.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const sallePopupRoute: Routes = [
    {
        path: 'salle-new',
        component: SallePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'factoryApp.salle.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'salle/:id/edit',
        component: SallePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'factoryApp.salle.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'salle/:id/delete',
        component: SalleDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'factoryApp.salle.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
