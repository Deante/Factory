import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Stagiaire } from './stagiaire.model';
import { StagiaireService } from './stagiaire.service';

@Component({
    selector: 'jhi-stagiaire-detail',
    templateUrl: './stagiaire-detail.component.html'
})
export class StagiaireDetailComponent implements OnInit, OnDestroy {

    stagiaire: Stagiaire;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private stagiaireService: StagiaireService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInStagiaires();
    }

    load(id) {
        this.stagiaireService.find(id).subscribe((stagiaire) => {
            this.stagiaire = stagiaire;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInStagiaires() {
        this.eventSubscriber = this.eventManager.subscribe(
            'stagiaireListModification',
            (response) => this.load(this.stagiaire.id)
        );
    }
}
