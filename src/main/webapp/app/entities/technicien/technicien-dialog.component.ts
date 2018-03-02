import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Technicien } from './technicien.model';
import { TechnicienPopupService } from './technicien-popup.service';
import { TechnicienService } from './technicien.service';
import { User, UserService } from '../../shared';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-technicien-dialog',
    templateUrl: './technicien-dialog.component.html'
})
export class TechnicienDialogComponent implements OnInit {

    technicien: Technicien;
    isSaving: boolean;

    users: User[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private technicienService: TechnicienService,
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
        if (this.technicien.id !== undefined) {
            this.subscribeToSaveResponse(
                this.technicienService.update(this.technicien));
        } else {
            this.subscribeToSaveResponse(
                this.technicienService.create(this.technicien));
        }
    }

    private subscribeToSaveResponse(result: Observable<Technicien>) {
        result.subscribe((res: Technicien) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Technicien) {
        this.eventManager.broadcast({ name: 'technicienListModification', content: 'OK'});
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
    selector: 'jhi-technicien-popup',
    template: ''
})
export class TechnicienPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private technicienPopupService: TechnicienPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.technicienPopupService
                    .open(TechnicienDialogComponent as Component, params['id']);
            } else {
                this.technicienPopupService
                    .open(TechnicienDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
