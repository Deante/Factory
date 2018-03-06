import { Injectable, LOCALE_ID, NgModule } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import { Formation, FormationService } from '../../entities/formation';
import { ResponseWrapper } from '../../shared';

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
