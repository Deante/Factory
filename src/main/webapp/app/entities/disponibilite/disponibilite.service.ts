import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { Disponibilite } from './disponibilite.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class DisponibiliteService {

    private resourceUrl =  SERVER_API_URL + 'api/disponibilites';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/disponibilites';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(disponibilite: Disponibilite): Observable<Disponibilite> {
        const copy = this.convert(disponibilite);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(disponibilite: Disponibilite): Observable<Disponibilite> {
        const copy = this.convert(disponibilite);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<Disponibilite> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    search(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceSearchUrl, options)
            .map((res: any) => this.convertResponse(res));
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        const result = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            result.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return new ResponseWrapper(res.headers, result, res.status);
    }

    /**
     * Convert a returned JSON object to Disponibilite.
     */
    private convertItemFromServer(json: any): Disponibilite {
        const entity: Disponibilite = Object.assign(new Disponibilite(), json);
        entity.dateDebut = this.dateUtils
            .convertLocalDateFromServer(json.dateDebut);
        entity.dateFin = this.dateUtils
            .convertLocalDateFromServer(json.dateFin);
        return entity;
    }

    /**
     * Convert a Disponibilite to a JSON which can be sent to the server.
     */
    private convert(disponibilite: Disponibilite): Disponibilite {
        const copy: Disponibilite = Object.assign({}, disponibilite);
        copy.dateDebut = this.dateUtils
            .convertLocalDateToServer(disponibilite.dateDebut);
        copy.dateFin = this.dateUtils
            .convertLocalDateToServer(disponibilite.dateFin);
        return copy;
    }
}
