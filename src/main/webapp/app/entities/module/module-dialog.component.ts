import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Module } from './module.model';
import { ModulePopupService } from './module-popup.service';
import { ModuleService } from './module.service';
import { Formation, FormationService } from '../formation';
import { Matiere, MatiereService } from '../matiere';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-module-dialog',
    templateUrl: './module-dialog.component.html'
})
export class ModuleDialogComponent implements OnInit {

    module: Module;
    isSaving: boolean;

    formations: Formation[];

    matieres: Matiere[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private moduleService: ModuleService,
        private formationService: FormationService,
        private matiereService: MatiereService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.formationService.query()
            .subscribe((res: ResponseWrapper) => { this.formations = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.matiereService.query()
            .subscribe((res: ResponseWrapper) => { this.matieres = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.module.id !== undefined) {
            this.subscribeToSaveResponse(
                this.moduleService.update(this.module));
        } else {
            this.subscribeToSaveResponse(
                this.moduleService.create(this.module));
        }
    }

    private subscribeToSaveResponse(result: Observable<Module>) {
        result.subscribe((res: Module) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Module) {
        this.eventManager.broadcast({ name: 'moduleListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackFormationById(index: number, item: Formation) {
        return item.id;
    }

    trackMatiereById(index: number, item: Matiere) {
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
    selector: 'jhi-module-popup',
    template: ''
})
export class ModulePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private modulePopupService: ModulePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modulePopupService
                    .open(ModuleDialogComponent as Component, params['id']);
            } else {
                this.modulePopupService
                    .open(ModuleDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
