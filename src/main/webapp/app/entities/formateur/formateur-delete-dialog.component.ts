import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Formateur } from './formateur.model';
import { FormateurPopupService } from './formateur-popup.service';
import { FormateurService } from './formateur.service';

@Component({
    selector: 'jhi-formateur-delete-dialog',
    templateUrl: './formateur-delete-dialog.component.html'
})
export class FormateurDeleteDialogComponent {

    formateur: Formateur;

    constructor(
        private formateurService: FormateurService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.formateurService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'formateurListModification',
                content: 'Deleted an formateur'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-formateur-delete-popup',
    template: ''
})
export class FormateurDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private formateurPopupService: FormateurPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.formateurPopupService
                .open(FormateurDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
