import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { Projecteur } from './projecteur.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class ProjecteurService {

    private resourceUrl =  SERVER_API_URL + 'api/projecteurs';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/projecteurs';

    constructor(private http: Http) { }

    create(projecteur: Projecteur): Observable<Projecteur> {
        const copy = this.convert(projecteur);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(projecteur: Projecteur): Observable<Projecteur> {
        const copy = this.convert(projecteur);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<Projecteur> {
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
     * Convert a returned JSON object to Projecteur.
     */
    private convertItemFromServer(json: any): Projecteur {
        const entity: Projecteur = Object.assign(new Projecteur(), json);
        return entity;
    }

    /**
     * Convert a Projecteur to a JSON which can be sent to the server.
     */
    private convert(projecteur: Projecteur): Projecteur {
        const copy: Projecteur = Object.assign({}, projecteur);
        return copy;
    }
}
