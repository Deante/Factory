import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Matiere } from './matiere.model';
import { MatierePopupService } from './matiere-popup.service';
import { MatiereService } from './matiere.service';
import { Module, ModuleService } from '../module';
import { Competence, CompetenceService } from '../competence';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-matiere-dialog',
    templateUrl: './matiere-dialog.component.html'
})
export class MatiereDialogComponent implements OnInit {

    matiere: Matiere;
    isSaving: boolean;

    modules: Module[];

    competences: Competence[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private matiereService: MatiereService,
        private moduleService: ModuleService,
        private competenceService: CompetenceService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.moduleService.query()
            .subscribe((res: ResponseWrapper) => { this.modules = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.competenceService.query()
            .subscribe((res: ResponseWrapper) => { this.competences = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.matiere.id !== undefined) {
            this.subscribeToSaveResponse(
                this.matiereService.update(this.matiere));
        } else {
            this.subscribeToSaveResponse(
                this.matiereService.create(this.matiere));
        }
    }

    private subscribeToSaveResponse(result: Observable<Matiere>) {
        result.subscribe((res: Matiere) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Matiere) {
        this.eventManager.broadcast({ name: 'matiereListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackModuleById(index: number, item: Module) {
        return item.id;
    }

    trackCompetenceById(index: number, item: Competence) {
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
    selector: 'jhi-matiere-popup',
    template: ''
})
export class MatierePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private matierePopupService: MatierePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.matierePopupService
                    .open(MatiereDialogComponent as Component, params['id']);
            } else {
                this.matierePopupService
                    .open(MatiereDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
