import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { Formateur } from './formateur.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class FormateurService {

    private resourceUrl =  SERVER_API_URL + 'api/formateurs';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/formateurs';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(formateur: Formateur): Observable<Formateur> {
        const copy = this.convert(formateur);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(formateur: Formateur): Observable<Formateur> {
        const copy = this.convert(formateur);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<Formateur> {
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
     * Convert a returned JSON object to Formateur.
     */
    private convertItemFromServer(json: any): Formateur {
        const entity: Formateur = Object.assign(new Formateur(), json);
        entity.dateDebutDispo = this.dateUtils
            .convertLocalDateFromServer(json.dateDebutDispo);
        entity.dateFinDispo = this.dateUtils
            .convertLocalDateFromServer(json.dateFinDispo);
        return entity;
    }

    /**
     * Convert a Formateur to a JSON which can be sent to the server.
     */
    private convert(formateur: Formateur): Formateur {
        const copy: Formateur = Object.assign({}, formateur);
        copy.dateDebutDispo = this.dateUtils
            .convertLocalDateToServer(formateur.dateDebutDispo);
        copy.dateFinDispo = this.dateUtils
            .convertLocalDateToServer(formateur.dateFinDispo);
        return copy;
    }
}
