import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { StagiaireComponent } from './stagiaire.component';
import { StagiaireDetailComponent } from './stagiaire-detail.component';
import { StagiairePopupComponent } from './stagiaire-dialog.component';
import { StagiaireDeletePopupComponent } from './stagiaire-delete-dialog.component';

export const stagiaireRoute: Routes = [
    {
        path: 'stagiaire',
        component: StagiaireComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'factoryApp.stagiaire.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'stagiaire/:id',
        component: StagiaireDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'factoryApp.stagiaire.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const stagiairePopupRoute: Routes = [
    {
        path: 'stagiaire-new',
        component: StagiairePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'factoryApp.stagiaire.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'stagiaire/:id/edit',
        component: StagiairePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'factoryApp.stagiaire.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'stagiaire/:id/delete',
        component: StagiaireDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'factoryApp.stagiaire.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
