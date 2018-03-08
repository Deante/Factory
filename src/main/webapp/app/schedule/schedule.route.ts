import { Route } from '@angular/router';
import { UserRouteAccessService } from '../shared';
import { ScheduleComponent } from './schedule.component';

export const scheduleRoute: Route = {
    path: 'schedule',
    component: ScheduleComponent,
    data: {
        authorities: []
    },
    canActivate: [UserRouteAccessService]
};
