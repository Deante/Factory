import { Salle } from './../entities/salle';
import { Component, OnInit, NgModule } from '@angular/core';
import { JhiLanguageService } from 'ng-jhipster';
import {Message} from 'primeng/components/common/api';
import {EventService} from './service/event.service';
import {MyEvent, GestionnaireEvent} from './event/event';
import { FormationService, Formation } from '../entities/formation';
import { ResponseWrapper, Principal, Account, User } from '../shared';
import { Stagiaire } from '../entities/stagiaire';
import { Formateur } from '../entities/formateur';
import { Gestionnaire } from '../entities/gestionnaire';

@Component({
    selector: 'jhi-schedule',
    templateUrl: './schedule.component.html',
    styles: []
})
export class ScheduleComponent implements OnInit {
    account: Account;
    msgs: Message[] = [];
    activeIndex = 0;
    events: any[];
    headerConfig: any;
    event;
    dialogVisible = false;
    idGen = 100;
    fr: any;
    userHasGestionnaireRole: boolean;
    userHasAdminRole: boolean;

    constructor(private eventService: EventService
        , private principal: Principal
        , private formationService: FormationService) { }

    ngOnInit() {
        // this.eventService.getEvents().subscribe((events: any) => {this.events = events.data; });

        this.principal.identity().then((account) => {
            this.account = account;
            this.userHasAdminRole = this.isUserAdmin();
            this.userHasGestionnaireRole = this.isUserGestionnaire();
            console.log('user account', this.account);
            console.log('isUserGestionnaire: ' + this.userHasGestionnaireRole);
        });

        this.headerConfig = {
            left: 'prev,next today',
            center: 'title',
            right: 'month,agendaWeek,agendaDay'
        };

        this.fr = {
            firstDayOfWeek: 1,
            dayNames: ['Dimanche', 'Lundi', 'Mardi', 'Mercredi', 'Jeudi', 'Vendredi', 'Samedi'],
            dayNamesShort: ['Dim', 'Lun', 'Mar', 'Mer', 'Jeu', 'Ven', 'Sam'],
            dayNamesMin: ['DIM', 'LUN', 'MAR', 'MER ', 'JEU', 'VEN ', 'SAM'],
            monthNames: [
                'Janvier', 'Février', 'Mars', 'Avril', 'Mai', 'Juin', 'Juillet',
                'Aôut', 'Septembre', 'Octobre', 'Novembre', 'Décembre'
            ],
            monthNamesShort: ['Jan', 'Fev', 'Mar', 'Avr', 'Mai', 'Jui', 'Juil', 'Aou', 'Sep', 'Oct', 'Nov', 'Dec']
        };
    }

    private isUserGestionnaire() {
        return (this.account.authorities.find((role) => role === 'ROLE_GESTIONNAIRE') !== undefined);
    }

    private isUserAdmin() {
        return (this.account.authorities.find((role) => role === 'ROLE_ADMIN') !== undefined);
    }

    loadEvents(event: any) {
        const start = event.view.start;
        const end = event.view.end;
        // In real time the service call filtered based on start and end dates
        // this.eventService.getEvents().subscribe((events: any) => {this.events = events.data; });
        this.eventService.getFormationEvents(this.formationService).subscribe((response: ResponseWrapper) => {
            if (this.userHasAdminRole || this.userHasGestionnaireRole) {
                console.log('bdd response', response);
                this.events = this.loadGestionnaireEvent(response);
                console.log('events', this.events);
            }
        });
    }

    // gestionnaire
    private loadGestionnaireEvent(response: ResponseWrapper): Array<any> {
        const result: Array<any> = Array<any>();
        let formation: Formation;
        for (formation of response.json) {
            const e = new GestionnaireEvent();
            e.id = formation.id;
            e.title = formation.nom;
            e.start = formation.dateDebutForm;
            e.end = formation.dateFinForm;
            const gestionnaire: Gestionnaire = formation.gestionnaire;
            if (gestionnaire !== undefined && gestionnaire !== null) {
                const gestionnaireUser: User = gestionnaire.user;
                if (gestionnaireUser !== undefined && gestionnaireUser !== null) {
                    if (gestionnaireUser.lastName !== undefined
                        && gestionnaireUser.lastName !== null
                        && gestionnaireUser.lastName !== ''
                        && gestionnaireUser.firstName !== undefined
                        && gestionnaireUser.firstName !== null
                        && gestionnaireUser.firstName !== '' ) {
                        e.gestionnaire = gestionnaireUser.lastName + ' ' + gestionnaireUser.firstName;
                    } else {
                        e.gestionnaire = 'gestionnaire ' + gestionnaire.id;
                    }
                }
            } else {
                e.color = 'red';
            }
            const salle: Salle = formation.salle;
            if (salle !== undefined && salle !== null) {
                e.salleCode = (salle.code !== null ? salle.code : '');
                e.salleCapacity = (salle.capacite !== null ? salle.capacite : 0);
                e.color = (salle.code !== null && salle.capacite !== null  && salle.capacite > 0 ? 'blue' : 'red');
            } else {
                e.salleCode = '';
                e.salleCapacity = 0;
                e.color = 'red';
            }
            const stagiaires: Stagiaire[] = formation.stagiaires;
            if (stagiaires !== undefined && stagiaires !== null) {
                e.stagiaireCount = stagiaires.length;
                if (salle !== undefined && salle !== null && e.stagiaireCount <= salle.capacite) {
                    e.color = (e.color !== 'red') ? 'blue' : 'red';
                } else {
                    e.color = 'red';
                }
            } else {
                e.color = 'red';
            }
            const formateurs: Formateur[] = formation.formateurs;
            if (formateurs !== undefined && formateurs !== null) {
                for (const formateur of formateurs) {
                    const formateurUser: User = formateur.user;
                    let formateurFullName: string;
                    if (formateurUser !== undefined && formateurUser !== null) {
                        formateurFullName = formateurUser.lastName + ' ' + formateurUser.firstName;
                    } else {
                        formateurFullName = 'formateur ' + formateur.id;
                    }
                    e.formateursList.push(formateurFullName);
                }
                if (e.formateursList.length > 0) {
                    e.color = (e.color !== 'red' ? 'blue' : 'red');
                }
            } else {
                e.color = 'red';
            }

            result.push(e);
        }
        return result;
    }

    private getGestionnaireEvent(e: any) {
        this.event = new GestionnaireEvent();
        this.event.title = e.calEvent.title;

        const start = e.calEvent.start;
        const end = e.calEvent.end;
        if (e.view.name === 'month') {
            start.stripTime();
        }

        if (end) {
            end.stripTime();
            this.event.end = end.format();
        }

        this.event.id = e.calEvent.id;
        this.event.start = start.format();
        this.event.allDay = e.calEvent.allDay;
        this.event.gestionnaire = e.calEvent.gestionnaire;
        this.event.salleCode = e.calEvent.salleCode;
        this.event.color = e.calEvent.color;
        this.event.salleCapacity = e.calEvent.salleCapacity;
        this.event.stagiaireCount = e.calEvent.stagiaireCount;
        this.event.formateursList = e.calEvent.formateursList;
        console.log('event detail', this.event);
    }

    handleDayClick(event: any) {
        this.event = null;
        this.dialogVisible = false;

        // technician events here
        /*
            need request to get all formations running at this date
        */
    }

    handleEventClick(e: any) {
        if (this.userHasAdminRole || this.userHasGestionnaireRole) {
            this.getGestionnaireEvent(e);
        }
        this.dialogVisible = true;
    }

    handleEventMouseover(event: any) {
        this.msgs.length = 0;
        this.msgs.push({severity: 'info', summary: 'Event mouse over'});
    }

    handleEventMouseout(event: any) {
        this.msgs.length = 0;
        this.msgs.push({severity: 'info', summary: 'Event mouse over'});
    }

    handleEventDragStart(event: any) {
        this.msgs.length = 0;
        this.msgs.push({severity: 'info', summary: 'Event mouse over'});
    }

    handleEventDragStop(event: any) {
        this.msgs.length = 0;
        this.msgs.push({severity: 'info', summary: 'Event mouse over'});
    }

    handleEventDrop(event: any) {
        this.msgs.length = 0;
        this.msgs.push({severity: 'info', summary: 'Event mouse over'});
    }

    handleEventResizeStart(event: any) {
        this.msgs.length = 0;
        this.msgs.push({severity: 'info', summary: 'Event mouse over'});
    }

    handleEventResizeStop(event: any) {
        this.msgs.length = 0;
        this.msgs.push({severity: 'info', summary: 'Event mouse over'});
    }

    handleEventResize(event: any) {
        this.msgs.length = 0;
        this.msgs.push({severity: 'info', summary: 'Event mouse over'});
    }

    handleDrop(event: any) {
        this.msgs.length = 0;
        this.msgs.push({severity: 'info', summary: 'Event mouse over'});
    }

    handleViewDestroy(event: any) {
        this.msgs.length = 0;
        this.msgs.push({severity: 'info', summary: 'The view is about to be removed from the DOM'});
    }

    HandleChangeStep(label: string) {
        this.msgs.length = 0;
        this.msgs.push({severity: 'info', summary: label});
    }

    saveEvent() {
        // update
        if ( this.event.id) {
            const index: number = this.findEventIndexById(this.event.id);
            if (index >= 0) {
                this.events[index] = this.event;
            }
        } else {
            this.event.id = this.idGen++;
            this.events.push(this.event);
            this.event = null;
        }

        this.dialogVisible = false;
    }

    deleteEvent() {
        const index: number = this.findEventIndexById(this.event.id);
        if (index >= 0) {
            this.events.splice(index, 1);
        }
        this.dialogVisible = false;
    }

    findEventIndexById(id: number) {
        let index = -1;
        for (let i = 0; i < this.events.length; i++) {
            if (id === this.events[i].id) {
                index = i;
                break;
            }
        }
        return index;
    }

    closeEventDialog() {
        this.dialogVisible = false;
    }

}
