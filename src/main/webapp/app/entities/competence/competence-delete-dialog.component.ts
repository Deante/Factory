import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Competence } from './competence.model';
import { CompetencePopupService } from './competence-popup.service';
import { CompetenceService } from './competence.service';

@Component({
    selector: 'jhi-competence-delete-dialog',
    templateUrl: './competence-delete-dialog.component.html'
})
export class CompetenceDeleteDialogComponent {

    competence: Competence;

    constructor(
        private competenceService: CompetenceService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.competenceService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'competenceListModification',
                content: 'Deleted an competence'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-competence-delete-popup',
    template: ''
})
export class CompetenceDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private competencePopupService: CompetencePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.competencePopupService
                .open(CompetenceDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
