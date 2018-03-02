import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { Formation } from './formation.model';
import { FormationService } from './formation.service';

@Injectable()
export class FormationPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private formationService: FormationService

    ) {
        this.ngbModalRef = null;
    }

    open(component: Component, id?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.formationService.find(id).subscribe((formation) => {
                    if (formation.dateDebutForm) {
                        formation.dateDebutForm = {
                            year: formation.dateDebutForm.getFullYear(),
                            month: formation.dateDebutForm.getMonth() + 1,
                            day: formation.dateDebutForm.getDate()
                        };
                    }
                    if (formation.dateFinForm) {
                        formation.dateFinForm = {
                            year: formation.dateFinForm.getFullYear(),
                            month: formation.dateFinForm.getMonth() + 1,
                            day: formation.dateFinForm.getDate()
                        };
                    }
                    this.ngbModalRef = this.formationModalRef(component, formation);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.formationModalRef(component, new Formation());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    formationModalRef(component: Component, formation: Formation): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.formation = formation;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
        });
        return modalRef;
    }
}
