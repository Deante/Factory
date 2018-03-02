import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Competence } from './competence.model';
import { CompetencePopupService } from './competence-popup.service';
import { CompetenceService } from './competence.service';
import { Matiere, MatiereService } from '../matiere';
import { Formateur, FormateurService } from '../formateur';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-competence-dialog',
    templateUrl: './competence-dialog.component.html'
})
export class CompetenceDialogComponent implements OnInit {

    competence: Competence;
    isSaving: boolean;

    matieres: Matiere[];

    formateurs: Formateur[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private competenceService: CompetenceService,
        private matiereService: MatiereService,
        private formateurService: FormateurService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.matiereService.query()
            .subscribe((res: ResponseWrapper) => { this.matieres = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.formateurService.query()
            .subscribe((res: ResponseWrapper) => { this.formateurs = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.competence.id !== undefined) {
            this.subscribeToSaveResponse(
                this.competenceService.update(this.competence));
        } else {
            this.subscribeToSaveResponse(
                this.competenceService.create(this.competence));
        }
    }

    private subscribeToSaveResponse(result: Observable<Competence>) {
        result.subscribe((res: Competence) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Competence) {
        this.eventManager.broadcast({ name: 'competenceListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackMatiereById(index: number, item: Matiere) {
        return item.id;
    }

    trackFormateurById(index: number, item: Formateur) {
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
    selector: 'jhi-competence-popup',
    template: ''
})
export class CompetencePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private competencePopupService: CompetencePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.competencePopupService
                    .open(CompetenceDialogComponent as Component, params['id']);
            } else {
                this.competencePopupService
                    .open(CompetenceDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
