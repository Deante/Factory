import { Route } from '@angular/router';
import { UserRouteAccessService } from '../shared';
import { ScheduleComponent } from './schedule.component';

export const scheduleRoute: Route = {
    path: 'schedule',
    component: ScheduleComponent,
    data: {
        authorities: ['ROLE_USER'],
        authorities: ['ROLE_ADMIN', 'ROLE_GESTIONNAIRE', 'ROLE_TECHNICIEN', 'ROLE_FORMATEUR'],
        pageTitle: 'factoryApp.schedule.home.title'
    },
    canActivate: [UserRouteAccessService]
};
