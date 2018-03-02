import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Competence } from './competence.model';
import { CompetenceService } from './competence.service';

@Component({
    selector: 'jhi-competence-detail',
    templateUrl: './competence-detail.component.html'
})
export class CompetenceDetailComponent implements OnInit, OnDestroy {

    competence: Competence;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private competenceService: CompetenceService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInCompetences();
    }

    load(id) {
        this.competenceService.find(id).subscribe((competence) => {
            this.competence = competence;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInCompetences() {
        this.eventSubscriber = this.eventManager.subscribe(
            'competenceListModification',
            (response) => this.load(this.competence.id)
        );
    }
}
