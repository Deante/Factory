import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { Ordinateur } from './ordinateur.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class OrdinateurService {

    private resourceUrl =  SERVER_API_URL + 'api/ordinateurs';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/ordinateurs';

    constructor(private http: Http) { }

    create(ordinateur: Ordinateur): Observable<Ordinateur> {
        const copy = this.convert(ordinateur);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(ordinateur: Ordinateur): Observable<Ordinateur> {
        const copy = this.convert(ordinateur);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<Ordinateur> {
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
     * Convert a returned JSON object to Ordinateur.
     */
    private convertItemFromServer(json: any): Ordinateur {
        const entity: Ordinateur = Object.assign(new Ordinateur(), json);
        return entity;
    }

    /**
     * Convert a Ordinateur to a JSON which can be sent to the server.
     */
    private convert(ordinateur: Ordinateur): Ordinateur {
        const copy: Ordinateur = Object.assign({}, ordinateur);
        return copy;
    }
}
