import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Technicien } from './technicien.model';
import { TechnicienPopupService } from './technicien-popup.service';
import { TechnicienService } from './technicien.service';

@Component({
    selector: 'jhi-technicien-delete-dialog',
    templateUrl: './technicien-delete-dialog.component.html'
})
export class TechnicienDeleteDialogComponent {

    technicien: Technicien;

    constructor(
        private technicienService: TechnicienService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.technicienService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'technicienListModification',
                content: 'Deleted an technicien'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-technicien-delete-popup',
    template: ''
})
export class TechnicienDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private technicienPopupService: TechnicienPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.technicienPopupService
                .open(TechnicienDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
