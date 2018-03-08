import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { FormateurComponent } from './formateur.component';
import { FormateurDetailComponent } from './formateur-detail.component';
import { FormateurPopupComponent } from './formateur-dialog.component';
import { FormateurDeletePopupComponent } from './formateur-delete-dialog.component';

export const formateurRoute: Routes = [
    {
        path: 'formateur',
        component: FormateurComponent,
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'factoryApp.formateur.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'formateur/:id',
        component: FormateurDetailComponent,
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'factoryApp.formateur.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const formateurPopupRoute: Routes = [
    {
        path: 'formateur-new',
        component: FormateurPopupComponent,
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'factoryApp.formateur.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'formateur/:id/edit',
        component: FormateurPopupComponent,
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'factoryApp.formateur.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'formateur/:id/delete',
        component: FormateurDeletePopupComponent,
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'factoryApp.formateur.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
