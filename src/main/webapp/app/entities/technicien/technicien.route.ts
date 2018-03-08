import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { TechnicienComponent } from './technicien.component';
import { TechnicienDetailComponent } from './technicien-detail.component';
import { TechnicienPopupComponent } from './technicien-dialog.component';
import { TechnicienDeletePopupComponent } from './technicien-delete-dialog.component';

export const technicienRoute: Routes = [
    {
        path: 'technicien',
        component: TechnicienComponent,
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'factoryApp.technicien.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'technicien/:id',
        component: TechnicienDetailComponent,
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'factoryApp.technicien.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const technicienPopupRoute: Routes = [
    {
        path: 'technicien-new',
        component: TechnicienPopupComponent,
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'factoryApp.technicien.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'technicien/:id/edit',
        component: TechnicienPopupComponent,
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'factoryApp.technicien.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'technicien/:id/delete',
        component: TechnicienDeletePopupComponent,
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'factoryApp.technicien.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
