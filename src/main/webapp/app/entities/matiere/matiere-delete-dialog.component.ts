import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Matiere } from './matiere.model';
import { MatierePopupService } from './matiere-popup.service';
import { MatiereService } from './matiere.service';

@Component({
    selector: 'jhi-matiere-delete-dialog',
    templateUrl: './matiere-delete-dialog.component.html'
})
export class MatiereDeleteDialogComponent {

    matiere: Matiere;

    constructor(
        private matiereService: MatiereService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.matiereService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'matiereListModification',
                content: 'Deleted an matiere'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-matiere-delete-popup',
    template: ''
})
export class MatiereDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private matierePopupService: MatierePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.matierePopupService
                .open(MatiereDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
