import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Formation } from './formation.model';
import { FormationPopupService } from './formation-popup.service';
import { FormationService } from './formation.service';
import { Departement, DepartementService } from '../departement';
import { Formateur, FormateurService } from '../formateur';
import { Gestionnaire, GestionnaireService } from '../gestionnaire';
import { Salle, SalleService } from '../salle';
import { Technicien, TechnicienService } from '../technicien';
import { Module, ModuleService } from '../module';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-formation-dialog',
    templateUrl: './formation-dialog.component.html'
})
export class FormationDialogComponent implements OnInit {

    formation: Formation;
    isSaving: boolean;

    departements: Departement[];

    formateurs: Formateur[];

    gestionnaires: Gestionnaire[];

    salles: Salle[];

    techniciens: Technicien[];

    modules: Module[];
    dateDebutFormDp: any;
    dateFinFormDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private formationService: FormationService,
        private departementService: DepartementService,
        private formateurService: FormateurService,
        private gestionnaireService: GestionnaireService,
        private salleService: SalleService,
        private technicienService: TechnicienService,
        private moduleService: ModuleService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.departementService.query()
            .subscribe((res: ResponseWrapper) => { this.departements = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.formateurService.query()
            .subscribe((res: ResponseWrapper) => { this.formateurs = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.gestionnaireService.query()
            .subscribe((res: ResponseWrapper) => { this.gestionnaires = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.salleService.query()
            .subscribe((res: ResponseWrapper) => { this.salles = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.technicienService.query()
            .subscribe((res: ResponseWrapper) => { this.techniciens = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.moduleService.query()
            .subscribe((res: ResponseWrapper) => { this.modules = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.formation.id !== undefined) {
            this.subscribeToSaveResponse(
                this.formationService.update(this.formation));
        } else {
            this.subscribeToSaveResponse(
                this.formationService.create(this.formation));
        }
    }

    private subscribeToSaveResponse(result: Observable<Formation>) {
        result.subscribe((res: Formation) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Formation) {
        this.eventManager.broadcast({ name: 'formationListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackDepartementById(index: number, item: Departement) {
        return item.id;
    }

    trackFormateurById(index: number, item: Formateur) {
        return item.id;
    }

    trackGestionnaireById(index: number, item: Gestionnaire) {
        return item.id;
    }

    trackSalleById(index: number, item: Salle) {
        return item.id;
    }

    trackTechnicienById(index: number, item: Technicien) {
        return item.id;
    }

    trackModuleById(index: number, item: Module) {
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
    selector: 'jhi-formation-popup',
    template: ''
})
export class FormationPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private formationPopupService: FormationPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.formationPopupService
                    .open(FormationDialogComponent as Component, params['id']);
            } else {
                this.formationPopupService
                    .open(FormationDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
