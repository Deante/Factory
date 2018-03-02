import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { ProjecteurComponent } from './projecteur.component';
import { ProjecteurDetailComponent } from './projecteur-detail.component';
import { ProjecteurPopupComponent } from './projecteur-dialog.component';
import { ProjecteurDeletePopupComponent } from './projecteur-delete-dialog.component';

export const projecteurRoute: Routes = [
    {
        path: 'projecteur',
        component: ProjecteurComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'factoryApp.projecteur.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'projecteur/:id',
        component: ProjecteurDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'factoryApp.projecteur.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const projecteurPopupRoute: Routes = [
    {
        path: 'projecteur-new',
        component: ProjecteurPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'factoryApp.projecteur.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'projecteur/:id/edit',
        component: ProjecteurPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'factoryApp.projecteur.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'projecteur/:id/delete',
        component: ProjecteurDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'factoryApp.projecteur.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
