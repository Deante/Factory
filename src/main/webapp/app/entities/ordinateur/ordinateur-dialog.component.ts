import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Ordinateur } from './ordinateur.model';
import { OrdinateurPopupService } from './ordinateur-popup.service';
import { OrdinateurService } from './ordinateur.service';
import { Stagiaire, StagiaireService } from '../stagiaire';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-ordinateur-dialog',
    templateUrl: './ordinateur-dialog.component.html'
})
export class OrdinateurDialogComponent implements OnInit {

    ordinateur: Ordinateur;
    isSaving: boolean;

    stagiaires: Stagiaire[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private ordinateurService: OrdinateurService,
        private stagiaireService: StagiaireService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.stagiaireService.query()
            .subscribe((res: ResponseWrapper) => { this.stagiaires = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.ordinateur.id !== undefined) {
            this.subscribeToSaveResponse(
                this.ordinateurService.update(this.ordinateur));
        } else {
            this.subscribeToSaveResponse(
                this.ordinateurService.create(this.ordinateur));
        }
    }

    private subscribeToSaveResponse(result: Observable<Ordinateur>) {
        result.subscribe((res: Ordinateur) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Ordinateur) {
        this.eventManager.broadcast({ name: 'ordinateurListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackStagiaireById(index: number, item: Stagiaire) {
        return item.id;
    }

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
}

@Component({
    selector: 'jhi-ordinateur-popup',
    template: ''
})
export class OrdinateurPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private ordinateurPopupService: OrdinateurPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.ordinateurPopupService
                    .open(OrdinateurDialogComponent as Component, params['id']);
            } else {
                this.ordinateurPopupService
                    .open(OrdinateurDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
