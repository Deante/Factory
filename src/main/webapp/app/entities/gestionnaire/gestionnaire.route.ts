import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { GestionnaireComponent } from './gestionnaire.component';
import { GestionnaireDetailComponent } from './gestionnaire-detail.component';
import { GestionnairePopupComponent } from './gestionnaire-dialog.component';
import { GestionnaireDeletePopupComponent } from './gestionnaire-delete-dialog.component';

export const gestionnaireRoute: Routes = [
    {
        path: 'gestionnaire',
        component: GestionnaireComponent,
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'factoryApp.gestionnaire.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'gestionnaire/:id',
        component: GestionnaireDetailComponent,
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'factoryApp.gestionnaire.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const gestionnairePopupRoute: Routes = [
    {
        path: 'gestionnaire-new',
        component: GestionnairePopupComponent,
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'factoryApp.gestionnaire.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'gestionnaire/:id/edit',
        component: GestionnairePopupComponent,
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'factoryApp.gestionnaire.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'gestionnaire/:id/delete',
        component: GestionnaireDeletePopupComponent,
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'factoryApp.gestionnaire.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
