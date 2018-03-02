import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Formateur } from './formateur.model';
import { FormateurPopupService } from './formateur-popup.service';
import { FormateurService } from './formateur.service';
import { User, UserService } from '../../shared';
import { Competence, CompetenceService } from '../competence';
import { Formation, FormationService } from '../formation';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-formateur-dialog',
    templateUrl: './formateur-dialog.component.html'
})
export class FormateurDialogComponent implements OnInit {

    formateur: Formateur;
    isSaving: boolean;

    users: User[];

    competences: Competence[];

    formations: Formation[];
    dateDebutDispoDp: any;
    dateFinDispoDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private formateurService: FormateurService,
        private userService: UserService,
        private competenceService: CompetenceService,
        private formationService: FormationService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.userService.query()
            .subscribe((res: ResponseWrapper) => { this.users = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.competenceService.query()
            .subscribe((res: ResponseWrapper) => { this.competences = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.formationService.query()
            .subscribe((res: ResponseWrapper) => { this.formations = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.formateur.id !== undefined) {
            this.subscribeToSaveResponse(
                this.formateurService.update(this.formateur));
        } else {
            this.subscribeToSaveResponse(
                this.formateurService.create(this.formateur));
        }
    }

    private subscribeToSaveResponse(result: Observable<Formateur>) {
        result.subscribe((res: Formateur) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Formateur) {
        this.eventManager.broadcast({ name: 'formateurListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackUserById(index: number, item: User) {
        return item.id;
    }

    trackCompetenceById(index: number, item: Competence) {
        return item.id;
    }

    trackFormationById(index: number, item: Formation) {
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
    selector: 'jhi-formateur-popup',
    template: ''
})
export class FormateurPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private formateurPopupService: FormateurPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.formateurPopupService
                    .open(FormateurDialogComponent as Component, params['id']);
            } else {
                this.formateurPopupService
                    .open(FormateurDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
