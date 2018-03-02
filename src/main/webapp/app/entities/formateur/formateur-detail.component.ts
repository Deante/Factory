import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Formateur } from './formateur.model';
import { FormateurService } from './formateur.service';

@Component({
    selector: 'jhi-formateur-detail',
    templateUrl: './formateur-detail.component.html'
})
export class FormateurDetailComponent implements OnInit, OnDestroy {

    formateur: Formateur;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private formateurService: FormateurService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInFormateurs();
    }

    load(id) {
        this.formateurService.find(id).subscribe((formateur) => {
            this.formateur = formateur;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInFormateurs() {
        this.eventSubscriber = this.eventManager.subscribe(
            'formateurListModification',
            (response) => this.load(this.formateur.id)
        );
    }
}
