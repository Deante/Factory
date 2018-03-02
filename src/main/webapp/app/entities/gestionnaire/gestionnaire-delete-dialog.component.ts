import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Gestionnaire } from './gestionnaire.model';
import { GestionnairePopupService } from './gestionnaire-popup.service';
import { GestionnaireService } from './gestionnaire.service';

@Component({
    selector: 'jhi-gestionnaire-delete-dialog',
    templateUrl: './gestionnaire-delete-dialog.component.html'
})
export class GestionnaireDeleteDialogComponent {

    gestionnaire: Gestionnaire;

    constructor(
        private gestionnaireService: GestionnaireService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.gestionnaireService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'gestionnaireListModification',
                content: 'Deleted an gestionnaire'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-gestionnaire-delete-popup',
    template: ''
})
export class GestionnaireDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private gestionnairePopupService: GestionnairePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.gestionnairePopupService
                .open(GestionnaireDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
