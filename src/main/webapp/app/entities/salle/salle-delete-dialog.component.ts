import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Salle } from './salle.model';
import { SallePopupService } from './salle-popup.service';
import { SalleService } from './salle.service';

@Component({
    selector: 'jhi-salle-delete-dialog',
    templateUrl: './salle-delete-dialog.component.html'
})
export class SalleDeleteDialogComponent {

    salle: Salle;

    constructor(
        private salleService: SalleService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.salleService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'salleListModification',
                content: 'Deleted an salle'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-salle-delete-popup',
    template: ''
})
export class SalleDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private sallePopupService: SallePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.sallePopupService
                .open(SalleDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
