import { Route } from '@angular/router';
import { UserRouteAccessService } from '../shared';
import { ScheduleComponent } from './schedule.component';

export const scheduleRoute: Route = {
    path: 'schedule',
    component: ScheduleComponent,
    data: {
<<<<<<< HEAD
        authorities: []
=======
        authorities: ['ROLE_USER'],
        pageTitle: 'factoryApp.schedule.home.title'
>>>>>>> sonny
    },
    canActivate: [UserRouteAccessService]
};
