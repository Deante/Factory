import {Injectable} from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import {ResponseWrapper} from '../../shared';
import {MyEvent} from '../event/event';
import {FormationService} from '../../entities/formation';

@Injectable()
export class EventService {

    constructor(private http: HttpClient) {
    }

    getEvents(): Observable<any> {
        return this.http.get('content/primeng/assets/data/json/events/scheduleevents.json')
            .map((response) => response);
    }

    getFormationEvents(formationService: FormationService): Observable<any> {
        return formationService.query();
    }

}
