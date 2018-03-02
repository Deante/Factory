import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { Formateur } from './formateur.model';
import { FormateurService } from './formateur.service';

@Injectable()
export class FormateurPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private formateurService: FormateurService

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
                this.formateurService.find(id).subscribe((formateur) => {
                    if (formateur.dateDebutDispo) {
                        formateur.dateDebutDispo = {
                            year: formateur.dateDebutDispo.getFullYear(),
                            month: formateur.dateDebutDispo.getMonth() + 1,
                            day: formateur.dateDebutDispo.getDate()
                        };
                    }
                    if (formateur.dateFinDispo) {
                        formateur.dateFinDispo = {
                            year: formateur.dateFinDispo.getFullYear(),
                            month: formateur.dateFinDispo.getMonth() + 1,
                            day: formateur.dateFinDispo.getDate()
                        };
                    }
                    this.ngbModalRef = this.formateurModalRef(component, formateur);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.formateurModalRef(component, new Formateur());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    formateurModalRef(component: Component, formateur: Formateur): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.formateur = formateur;
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
