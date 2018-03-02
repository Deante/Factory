import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Ordinateur } from './ordinateur.model';
import { OrdinateurPopupService } from './ordinateur-popup.service';
import { OrdinateurService } from './ordinateur.service';

@Component({
    selector: 'jhi-ordinateur-delete-dialog',
    templateUrl: './ordinateur-delete-dialog.component.html'
})
export class OrdinateurDeleteDialogComponent {

    ordinateur: Ordinateur;

    constructor(
        private ordinateurService: OrdinateurService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.ordinateurService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'ordinateurListModification',
                content: 'Deleted an ordinateur'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-ordinateur-delete-popup',
    template: ''
})
export class OrdinateurDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private ordinateurPopupService: OrdinateurPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.ordinateurPopupService
                .open(OrdinateurDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
