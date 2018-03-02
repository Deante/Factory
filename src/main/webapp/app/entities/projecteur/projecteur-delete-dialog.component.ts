import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Projecteur } from './projecteur.model';
import { ProjecteurPopupService } from './projecteur-popup.service';
import { ProjecteurService } from './projecteur.service';

@Component({
    selector: 'jhi-projecteur-delete-dialog',
    templateUrl: './projecteur-delete-dialog.component.html'
})
export class ProjecteurDeleteDialogComponent {

    projecteur: Projecteur;

    constructor(
        private projecteurService: ProjecteurService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.projecteurService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'projecteurListModification',
                content: 'Deleted an projecteur'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-projecteur-delete-popup',
    template: ''
})
export class ProjecteurDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private projecteurPopupService: ProjecteurPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.projecteurPopupService
                .open(ProjecteurDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
