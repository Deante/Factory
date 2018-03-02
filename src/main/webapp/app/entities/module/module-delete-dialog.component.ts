import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Module } from './module.model';
import { ModulePopupService } from './module-popup.service';
import { ModuleService } from './module.service';

@Component({
    selector: 'jhi-module-delete-dialog',
    templateUrl: './module-delete-dialog.component.html'
})
export class ModuleDeleteDialogComponent {

    module: Module;

    constructor(
        private moduleService: ModuleService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.moduleService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'moduleListModification',
                content: 'Deleted an module'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-module-delete-popup',
    template: ''
})
export class ModuleDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private modulePopupService: ModulePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modulePopupService
                .open(ModuleDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
