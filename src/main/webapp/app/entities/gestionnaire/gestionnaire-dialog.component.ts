import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Gestionnaire } from './gestionnaire.model';
import { GestionnairePopupService } from './gestionnaire-popup.service';
import { GestionnaireService } from './gestionnaire.service';
import { User, UserService } from '../../shared';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-gestionnaire-dialog',
    templateUrl: './gestionnaire-dialog.component.html'
})
export class GestionnaireDialogComponent implements OnInit {

    gestionnaire: Gestionnaire;
    isSaving: boolean;

    users: User[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private gestionnaireService: GestionnaireService,
        private userService: UserService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.userService.query()
            .subscribe((res: ResponseWrapper) => { this.users = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.gestionnaire.id !== undefined) {
            this.subscribeToSaveResponse(
                this.gestionnaireService.update(this.gestionnaire));
        } else {
            this.subscribeToSaveResponse(
                this.gestionnaireService.create(this.gestionnaire));
        }
    }

    private subscribeToSaveResponse(result: Observable<Gestionnaire>) {
        result.subscribe((res: Gestionnaire) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Gestionnaire) {
        this.eventManager.broadcast({ name: 'gestionnaireListModification', content: 'OK'});
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
}

@Component({
    selector: 'jhi-gestionnaire-popup',
    template: ''
})
export class GestionnairePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private gestionnairePopupService: GestionnairePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.gestionnairePopupService
                    .open(GestionnaireDialogComponent as Component, params['id']);
            } else {
                this.gestionnairePopupService
                    .open(GestionnaireDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
