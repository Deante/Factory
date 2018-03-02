import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { Technicien } from './technicien.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class TechnicienService {

    private resourceUrl =  SERVER_API_URL + 'api/techniciens';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/techniciens';

    constructor(private http: Http) { }

    create(technicien: Technicien): Observable<Technicien> {
        const copy = this.convert(technicien);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(technicien: Technicien): Observable<Technicien> {
        const copy = this.convert(technicien);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<Technicien> {
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
     * Convert a returned JSON object to Technicien.
     */
    private convertItemFromServer(json: any): Technicien {
        const entity: Technicien = Object.assign(new Technicien(), json);
        return entity;
    }

    /**
     * Convert a Technicien to a JSON which can be sent to the server.
     */
    private convert(technicien: Technicien): Technicien {
        const copy: Technicien = Object.assign({}, technicien);
        return copy;
    }
}
