import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Gestionnaire } from './gestionnaire.model';
import { GestionnaireService } from './gestionnaire.service';

@Component({
    selector: 'jhi-gestionnaire-detail',
    templateUrl: './gestionnaire-detail.component.html'
})
export class GestionnaireDetailComponent implements OnInit, OnDestroy {

    gestionnaire: Gestionnaire;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private gestionnaireService: GestionnaireService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInGestionnaires();
    }

    load(id) {
        this.gestionnaireService.find(id).subscribe((gestionnaire) => {
            this.gestionnaire = gestionnaire;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInGestionnaires() {
        this.eventSubscriber = this.eventManager.subscribe(
            'gestionnaireListModification',
            (response) => this.load(this.gestionnaire.id)
        );
    }
}
