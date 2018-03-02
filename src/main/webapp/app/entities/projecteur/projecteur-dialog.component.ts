import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Projecteur } from './projecteur.model';
import { ProjecteurPopupService } from './projecteur-popup.service';
import { ProjecteurService } from './projecteur.service';
import { Salle, SalleService } from '../salle';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-projecteur-dialog',
    templateUrl: './projecteur-dialog.component.html'
})
export class ProjecteurDialogComponent implements OnInit {

    projecteur: Projecteur;
    isSaving: boolean;

    salles: Salle[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private projecteurService: ProjecteurService,
        private salleService: SalleService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.salleService.query()
            .subscribe((res: ResponseWrapper) => { this.salles = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.projecteur.id !== undefined) {
            this.subscribeToSaveResponse(
                this.projecteurService.update(this.projecteur));
        } else {
            this.subscribeToSaveResponse(
                this.projecteurService.create(this.projecteur));
        }
    }

    private subscribeToSaveResponse(result: Observable<Projecteur>) {
        result.subscribe((res: Projecteur) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Projecteur) {
        this.eventManager.broadcast({ name: 'projecteurListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackSalleById(index: number, item: Salle) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-projecteur-popup',
    template: ''
})
export class ProjecteurPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private projecteurPopupService: ProjecteurPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.projecteurPopupService
                    .open(ProjecteurDialogComponent as Component, params['id']);
            } else {
                this.projecteurPopupService
                    .open(ProjecteurDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
