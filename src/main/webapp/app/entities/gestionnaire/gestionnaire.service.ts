import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { Gestionnaire } from './gestionnaire.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class GestionnaireService {

    private resourceUrl =  SERVER_API_URL + 'api/gestionnaires';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/gestionnaires';

    constructor(private http: Http) { }

    create(gestionnaire: Gestionnaire): Observable<Gestionnaire> {
        const copy = this.convert(gestionnaire);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(gestionnaire: Gestionnaire): Observable<Gestionnaire> {
        const copy = this.convert(gestionnaire);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<Gestionnaire> {
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
     * Convert a returned JSON object to Gestionnaire.
     */
    private convertItemFromServer(json: any): Gestionnaire {
        const entity: Gestionnaire = Object.assign(new Gestionnaire(), json);
        return entity;
    }

    /**
     * Convert a Gestionnaire to a JSON which can be sent to the server.
     */
    private convert(gestionnaire: Gestionnaire): Gestionnaire {
        const copy: Gestionnaire = Object.assign({}, gestionnaire);
        return copy;
    }
}
