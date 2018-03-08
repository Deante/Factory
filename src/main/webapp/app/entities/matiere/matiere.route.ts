import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { MatiereComponent } from './matiere.component';
import { MatiereDetailComponent } from './matiere-detail.component';
import { MatierePopupComponent } from './matiere-dialog.component';
import { MatiereDeletePopupComponent } from './matiere-delete-dialog.component';

export const matiereRoute: Routes = [
    {
        path: 'matiere',
        component: MatiereComponent,
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'factoryApp.matiere.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'matiere/:id',
        component: MatiereDetailComponent,
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'factoryApp.matiere.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const matierePopupRoute: Routes = [
    {
        path: 'matiere-new',
        component: MatierePopupComponent,
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'factoryApp.matiere.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'matiere/:id/edit',
        component: MatierePopupComponent,
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'factoryApp.matiere.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'matiere/:id/delete',
        component: MatiereDeletePopupComponent,
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'factoryApp.matiere.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
