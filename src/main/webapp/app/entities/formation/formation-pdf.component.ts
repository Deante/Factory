import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Subscription} from 'rxjs/Subscription';
import {JhiEventManager} from 'ng-jhipster';

import {Formation} from './formation.model';
import {FormationService} from './formation.service';

@Component({
    selector: 'jhi-formation-pdf',
    templateUrl: './formation-pdf.component.html'
})
export class FormationPdfComponent implements OnInit, OnDestroy {

    formation: Formation;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(private eventManager: JhiEventManager,
                private formationService: FormationService,
                private route: ActivatedRoute) {
        // let doc = new jsPDF();
        // doc = this.formationService.getpdf(this.formation.id);
        // doc.output('datauri');
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });

        this.registerChangeInFormations();
    }

    downloadPDF() {
        this.formationService.getpdf(this.formation.id).subscribe((data) => {
            const fileUrl = URL.createObjectURL(data);
            window.open(fileUrl);

        });
    }

    load(id) {
        this.formationService.find(id).subscribe((formation) => {
            this.formation = formation;
        });
    }

    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInFormations() {
        this.eventSubscriber = this.eventManager.subscribe(
            'formationListModification',
            (response) => this.load(this.formation.id)
        );
    }
}
