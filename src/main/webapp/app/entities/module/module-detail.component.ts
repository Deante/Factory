import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Module } from './module.model';
import { ModuleService } from './module.service';

@Component({
    selector: 'jhi-module-detail',
    templateUrl: './module-detail.component.html'
})
export class ModuleDetailComponent implements OnInit, OnDestroy {

    module: Module;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private moduleService: ModuleService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInModules();
    }

    load(id) {
        this.moduleService.find(id).subscribe((module) => {
            this.module = module;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInModules() {
        this.eventSubscriber = this.eventManager.subscribe(
            'moduleListModification',
            (response) => this.load(this.module.id)
        );
    }
}
