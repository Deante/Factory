import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { Departement } from './departement.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class DepartementService {

    private resourceUrl =  SERVER_API_URL + 'api/departements';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/departements';

    constructor(private http: Http) { }

    create(departement: Departement): Observable<Departement> {
        const copy = this.convert(departement);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(departement: Departement): Observable<Departement> {
        const copy = this.convert(departement);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<Departement> {
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
     * Convert a returned JSON object to Departement.
     */
    private convertItemFromServer(json: any): Departement {
        const entity: Departement = Object.assign(new Departement(), json);
        return entity;
    }

    /**
     * Convert a Departement to a JSON which can be sent to the server.
     */
    private convert(departement: Departement): Departement {
        const copy: Departement = Object.assign({}, departement);
        return copy;
    }
}
