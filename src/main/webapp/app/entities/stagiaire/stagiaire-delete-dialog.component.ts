import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Stagiaire } from './stagiaire.model';
import { StagiairePopupService } from './stagiaire-popup.service';
import { StagiaireService } from './stagiaire.service';

@Component({
    selector: 'jhi-stagiaire-delete-dialog',
    templateUrl: './stagiaire-delete-dialog.component.html'
})
export class StagiaireDeleteDialogComponent {

    stagiaire: Stagiaire;

    constructor(
        private stagiaireService: StagiaireService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.stagiaireService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'stagiaireListModification',
                content: 'Deleted an stagiaire'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-stagiaire-delete-popup',
    template: ''
})
export class StagiaireDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private stagiairePopupService: StagiairePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.stagiairePopupService
                .open(StagiaireDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
