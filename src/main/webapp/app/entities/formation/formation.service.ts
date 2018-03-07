import {Injectable} from '@angular/core';
import {Http, Response, ResponseContentType} from '@angular/http';
import {Observable} from 'rxjs/Observable';
import {SERVER_API_URL} from '../../app.constants';

import {JhiDateUtils} from 'ng-jhipster';

import {Formation} from './formation.model';
import {createRequestOption, ResponseWrapper} from '../../shared';

@Injectable()
export class FormationService {

    private resourceUrl = SERVER_API_URL + 'api/formations';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/formations';

    constructor(private http: Http, private dateUtils: JhiDateUtils) {
    }

    create(formation: Formation): Observable<Formation> {
        const copy = this.convert(formation);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    getpdf(id: number): any {
        return this.http.get(`${this.resourceUrl}/${id}/pdf`, {responseType: ResponseContentType.Blob}).map(
            (res) => {
                return new Blob([res.blob()], {type: 'application/pdf'})
            });
    }

    update(formation: Formation): Observable<Formation> {
        const copy = this.convert(formation);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<Formation> {
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
     * Convert a returned JSON object to Formation.
     */
    private convertItemFromServer(json: any): Formation {
        const entity: Formation = Object.assign(new Formation(), json);
        entity.dateDebutForm = this.dateUtils
            .convertLocalDateFromServer(json.dateDebutForm);
        entity.dateFinForm = this.dateUtils
            .convertLocalDateFromServer(json.dateFinForm);
        return entity;
    }

    /**
     * Convert a Formation to a JSON which can be sent to the server.
     */
    private convert(formation: Formation): Formation {
        const copy: Formation = Object.assign({}, formation);
        copy.dateDebutForm = this.dateUtils
            .convertLocalDateToServer(formation.dateDebutForm);
        copy.dateFinForm = this.dateUtils
            .convertLocalDateToServer(formation.dateFinForm);
        return copy;
    }
}
