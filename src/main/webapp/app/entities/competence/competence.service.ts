import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { Competence } from './competence.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class CompetenceService {

    private resourceUrl =  SERVER_API_URL + 'api/competences';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/competences';

    constructor(private http: Http) { }

    create(competence: Competence): Observable<Competence> {
        const copy = this.convert(competence);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(competence: Competence): Observable<Competence> {
        const copy = this.convert(competence);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<Competence> {
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
     * Convert a returned JSON object to Competence.
     */
    private convertItemFromServer(json: any): Competence {
        const entity: Competence = Object.assign(new Competence(), json);
        return entity;
    }

    /**
     * Convert a Competence to a JSON which can be sent to the server.
     */
    private convert(competence: Competence): Competence {
        const copy: Competence = Object.assign({}, competence);
        return copy;
    }
}
