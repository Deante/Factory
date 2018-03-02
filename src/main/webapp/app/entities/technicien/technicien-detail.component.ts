import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Technicien } from './technicien.model';
import { TechnicienService } from './technicien.service';

@Component({
    selector: 'jhi-technicien-detail',
    templateUrl: './technicien-detail.component.html'
})
export class TechnicienDetailComponent implements OnInit, OnDestroy {

    technicien: Technicien;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private technicienService: TechnicienService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInTechniciens();
    }

    load(id) {
        this.technicienService.find(id).subscribe((technicien) => {
            this.technicien = technicien;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInTechniciens() {
        this.eventSubscriber = this.eventManager.subscribe(
            'technicienListModification',
            (response) => this.load(this.technicien.id)
        );
    }
}
