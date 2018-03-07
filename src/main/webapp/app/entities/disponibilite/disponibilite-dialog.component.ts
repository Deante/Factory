import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Disponibilite } from './disponibilite.model';
import { DisponibilitePopupService } from './disponibilite-popup.service';
import { DisponibiliteService } from './disponibilite.service';
import { Formateur, FormateurService } from '../formateur';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-disponibilite-dialog',
    templateUrl: './disponibilite-dialog.component.html'
})
export class DisponibiliteDialogComponent implements OnInit {

    disponibilite: Disponibilite;
    isSaving: boolean;

    formateurs: Formateur[];
    dateDebutDp: any;
    dateFinDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private disponibiliteService: DisponibiliteService,
        private formateurService: FormateurService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.formateurService.query()
            .subscribe((res: ResponseWrapper) => { this.formateurs = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.disponibilite.id !== undefined) {
            this.subscribeToSaveResponse(
                this.disponibiliteService.update(this.disponibilite));
        } else {
            this.subscribeToSaveResponse(
                this.disponibiliteService.create(this.disponibilite));
        }
    }

    private subscribeToSaveResponse(result: Observable<Disponibilite>) {
        result.subscribe((res: Disponibilite) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Disponibilite) {
        this.eventManager.broadcast({ name: 'disponibiliteListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackFormateurById(index: number, item: Formateur) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-disponibilite-popup',
    template: ''
})
export class DisponibilitePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private disponibilitePopupService: DisponibilitePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.disponibilitePopupService
                    .open(DisponibiliteDialogComponent as Component, params['id']);
            } else {
                this.disponibilitePopupService
                    .open(DisponibiliteDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
