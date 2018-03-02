import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Projecteur } from './projecteur.model';
import { ProjecteurService } from './projecteur.service';

@Component({
    selector: 'jhi-projecteur-detail',
    templateUrl: './projecteur-detail.component.html'
})
export class ProjecteurDetailComponent implements OnInit, OnDestroy {

    projecteur: Projecteur;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private projecteurService: ProjecteurService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInProjecteurs();
    }

    load(id) {
        this.projecteurService.find(id).subscribe((projecteur) => {
            this.projecteur = projecteur;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInProjecteurs() {
        this.eventSubscriber = this.eventManager.subscribe(
            'projecteurListModification',
            (response) => this.load(this.projecteur.id)
        );
    }
}
