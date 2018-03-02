import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Salle } from './salle.model';
import { SalleService } from './salle.service';

@Component({
    selector: 'jhi-salle-detail',
    templateUrl: './salle-detail.component.html'
})
export class SalleDetailComponent implements OnInit, OnDestroy {

    salle: Salle;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private salleService: SalleService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInSalles();
    }

    load(id) {
        this.salleService.find(id).subscribe((salle) => {
            this.salle = salle;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInSalles() {
        this.eventSubscriber = this.eventManager.subscribe(
            'salleListModification',
            (response) => this.load(this.salle.id)
        );
    }
}
