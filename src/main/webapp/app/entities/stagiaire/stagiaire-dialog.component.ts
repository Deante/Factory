import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Stagiaire } from './stagiaire.model';
import { StagiairePopupService } from './stagiaire-popup.service';
import { StagiaireService } from './stagiaire.service';
import { User, UserService } from '../../shared';
import { Formation, FormationService } from '../formation';
import { Ordinateur, OrdinateurService } from '../ordinateur';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-stagiaire-dialog',
    templateUrl: './stagiaire-dialog.component.html'
})
export class StagiaireDialogComponent implements OnInit {

    stagiaire: Stagiaire;
    isSaving: boolean;

    users: User[];

    formations: Formation[];

    ordinateurs: Ordinateur[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private stagiaireService: StagiaireService,
        private userService: UserService,
        private formationService: FormationService,
        private ordinateurService: OrdinateurService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.userService.query()
            .subscribe((res: ResponseWrapper) => { this.users = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.formationService.query()
            .subscribe((res: ResponseWrapper) => { this.formations = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.ordinateurService.query()
            .subscribe((res: ResponseWrapper) => { this.ordinateurs = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
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

    trackUserById(index: number, item: User) {
        return item.id;
    }

    trackFormationById(index: number, item: Formation) {
        return item.id;
    }

    trackOrdinateurById(index: number, item: Ordinateur) {
        return item.id;
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
