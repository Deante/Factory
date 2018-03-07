import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { Disponibilite } from './disponibilite.model';
import { DisponibiliteService } from './disponibilite.service';

@Injectable()
export class DisponibilitePopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private disponibiliteService: DisponibiliteService

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
                this.disponibiliteService.find(id).subscribe((disponibilite) => {
                    if (disponibilite.dateDebut) {
                        disponibilite.dateDebut = {
                            year: disponibilite.dateDebut.getFullYear(),
                            month: disponibilite.dateDebut.getMonth() + 1,
                            day: disponibilite.dateDebut.getDate()
                        };
                    }
                    if (disponibilite.dateFin) {
                        disponibilite.dateFin = {
                            year: disponibilite.dateFin.getFullYear(),
                            month: disponibilite.dateFin.getMonth() + 1,
                            day: disponibilite.dateFin.getDate()
                        };
                    }
                    this.ngbModalRef = this.disponibiliteModalRef(component, disponibilite);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.disponibiliteModalRef(component, new Disponibilite());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    disponibiliteModalRef(component: Component, disponibilite: Disponibilite): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.disponibilite = disponibilite;
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
