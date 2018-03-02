import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { FormationComponent } from './formation.component';
import { FormationDetailComponent } from './formation-detail.component';
import { FormationPopupComponent } from './formation-dialog.component';
import { FormationDeletePopupComponent } from './formation-delete-dialog.component';

export const formationRoute: Routes = [
    {
        path: 'formation',
        component: FormationComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'factoryApp.formation.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'formation/:id',
        component: FormationDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'factoryApp.formation.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const formationPopupRoute: Routes = [
    {
        path: 'formation-new',
        component: FormationPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'factoryApp.formation.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'formation/:id/edit',
        component: FormationPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'factoryApp.formation.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'formation/:id/delete',
        component: FormationDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'factoryApp.formation.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
