import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Stagiaire } from './stagiaire.model';
import { StagiairePopupService } from './stagiaire-popup.service';
import { StagiaireService } from './stagiaire.service';
import { Ordinateur, OrdinateurService } from '../ordinateur';
import { Formation, FormationService } from '../formation';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-stagiaire-dialog',
    templateUrl: './stagiaire-dialog.component.html'
})
export class StagiaireDialogComponent implements OnInit {

    stagiaire: Stagiaire;
    isSaving: boolean;

    ordinateurs: Ordinateur[];

    formations: Formation[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private stagiaireService: StagiaireService,
        private ordinateurService: OrdinateurService,
        private formationService: FormationService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.ordinateurService.query()
            .subscribe((res: ResponseWrapper) => { this.ordinateurs = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.formationService.query()
            .subscribe((res: ResponseWrapper) => { this.formations = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.stagiaire.id !== undefined) {
            this.subscribeToSaveResponse(
                this.stagiaireService.update(this.stagiaire));
        } else {
            this.subscribeToSaveResponse(
                this.stagiaireService.create(this.stagiaire));
        }
    }

    private subscribeToSaveResponse(result: Observable<Stagiaire>) {
        result.subscribe((res: Stagiaire) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Stagiaire) {
        this.eventManager.broadcast({ name: 'stagiaireListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackOrdinateurById(index: number, item: Ordinateur) {
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
    selector: 'jhi-stagiaire-popup',
    template: ''
})
export class StagiairePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private stagiairePopupService: StagiairePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.stagiairePopupService
                    .open(StagiaireDialogComponent as Component, params['id']);
            } else {
                this.stagiairePopupService
                    .open(StagiaireDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
