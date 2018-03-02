import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Ordinateur } from './ordinateur.model';
import { OrdinateurService } from './ordinateur.service';

@Component({
    selector: 'jhi-ordinateur-detail',
    templateUrl: './ordinateur-detail.component.html'
})
export class OrdinateurDetailComponent implements OnInit, OnDestroy {

    ordinateur: Ordinateur;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private ordinateurService: OrdinateurService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInOrdinateurs();
    }

    load(id) {
        this.ordinateurService.find(id).subscribe((ordinateur) => {
            this.ordinateur = ordinateur;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInOrdinateurs() {
        this.eventSubscriber = this.eventManager.subscribe(
            'ordinateurListModification',
            (response) => this.load(this.ordinateur.id)
        );
    }
}
