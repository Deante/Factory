import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { ModuleComponent } from './module.component';
import { ModuleDetailComponent } from './module-detail.component';
import { ModulePopupComponent } from './module-dialog.component';
import { ModuleDeletePopupComponent } from './module-delete-dialog.component';

export const moduleRoute: Routes = [
    {
        path: 'module',
        component: ModuleComponent,
        data: {
            authorities: ['ROLE_GESTIONNAIRE'],
            pageTitle: 'factoryApp.module.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'module/:id',
        component: ModuleDetailComponent,
        data: {
            authorities: ['ROLE_GESTIONNAIRE'],
            pageTitle: 'factoryApp.module.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const modulePopupRoute: Routes = [
    {
        path: 'module-new',
        component: ModulePopupComponent,
        data: {
            authorities: ['ROLE_GESTIONNAIRE'],
            pageTitle: 'factoryApp.module.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'module/:id/edit',
        component: ModulePopupComponent,
        data: {
            authorities: ['ROLE_GESTIONNAIRE'],
            pageTitle: 'factoryApp.module.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'module/:id/delete',
        component: ModuleDeletePopupComponent,
        data: {
            authorities: ['ROLE_GESTIONNAIRE'],
            pageTitle: 'factoryApp.module.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
