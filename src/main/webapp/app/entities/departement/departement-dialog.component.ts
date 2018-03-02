import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Departement } from './departement.model';
import { DepartementPopupService } from './departement-popup.service';
import { DepartementService } from './departement.service';
import { Site, SiteService } from '../site';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-departement-dialog',
    templateUrl: './departement-dialog.component.html'
})
export class DepartementDialogComponent implements OnInit {

    departement: Departement;
    isSaving: boolean;

    sites: Site[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private departementService: DepartementService,
        private siteService: SiteService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.siteService.query()
            .subscribe((res: ResponseWrapper) => { this.sites = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.departement.id !== undefined) {
            this.subscribeToSaveResponse(
                this.departementService.update(this.departement));
        } else {
            this.subscribeToSaveResponse(
                this.departementService.create(this.departement));
        }
    }

    private subscribeToSaveResponse(result: Observable<Departement>) {
        result.subscribe((res: Departement) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Departement) {
        this.eventManager.broadcast({ name: 'departementListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackSiteById(index: number, item: Site) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-departement-popup',
    template: ''
})
export class DepartementPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private departementPopupService: DepartementPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.departementPopupService
                    .open(DepartementDialogComponent as Component, params['id']);
            } else {
                this.departementPopupService
                    .open(DepartementDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
