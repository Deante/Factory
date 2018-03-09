import { Salle } from './../entities/salle/salle.model';
import { Component, OnInit, NgModule } from '@angular/core';
import { JhiLanguageService } from 'ng-jhipster';
import { Message } from 'primeng/components/common/api';
import { EventService } from './service/event.service';
import { MyEvent, FormationEvent, TechnicianEvent, FormerEvent } from './event/event';
import { FormationService, Formation } from '../entities/formation';
import { ResponseWrapper } from '../shared';
import { Account, Principal } from '../shared/';
import { Projecteur, EtatMaterielEnum } from '../entities/projecteur';

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
    event: MyEvent;
    dialogVisible = false;
    idGen = 100;
    fr: any;

    constructor(private principal: Principal
                , private eventService: EventService
                , private formationService: FormationService) { }

    ngOnInit() {
        this.principal.identity().then((account) => {
            this.account = account;
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

    loadEvents(event: any) {
        const start = event.view.start;
        const end = event.view.end;
        // In real time the service call filtered based on start and end dates
        // this.eventService.getEvents().subscribe((events: any) => {this.events = events.data; });
        this.eventService.getFormations(this.formationService).subscribe((response: ResponseWrapper) => { this.events = this.loadFormationEvents(response) });
    }

    // loads all events
    private loadFormationEvents(response: ResponseWrapper): Array<any> {
        const result: Array<any> = Array<any>();
        let f: Formation;
        for (f of response.json) {
            const e = new FormationEvent();
            console.log(f);
            e.id = f.id;
            e.title = f.nom;
            e.start = f.dateDebutForm;
            e.end = f.dateFinForm;
            const salle: Salle = f.salle; // f.salle is of type BaseEntity so need cast to get real object pointed to by BaseEntity.id
            const projecteur: Projecteur = (salle != null ? salle.projecteur : null);
            e.salleCode = (salle != null ? salle.code : '');
            e.salleCapacity = (salle != null ? salle.capacite : 0);
            e.stagiaireCount = (f.stagiaires != null ? f.stagiaires.length : 0);
            // here we set the color whether or not there is a resource problem in the formation
            e.color = (salle != null && f.stagiaires != null ?
                        (e.stagiaireCount <= e.salleCapacity && e.stagiaireCount > 0 ?
                            (projecteur != null && projecteur.etat !== EtatMaterielEnum.INUTILISABLE ? 'blue' : 'red')
                            : 'red')
                        : 'red');
            // event text's color, doesn't work :/
            e.textColor = 'white';
            result.push(e);
        }
        return result;
    }

    // when schedule event is clicked
    private getEventFromSchedule(event: any): MyEvent {
        let e;
        if (event.calEvent instanceof FormationEvent) {
            e = new FormationEvent();
            e.salleCode = event.calEvent.salleCode;
            e.salleCapacity = event.calEvent.salleCapacity;
            e.stagiaireCount = e.calEvent.stagiaireCount;
            e.prjecteurState = e.calEvent.prjecteurState;
        } else if (event.calEvent instanceof TechnicianEvent) {
            e = new TechnicianEvent();
            e.computerUsed = event.calEvent.computerUsed;
            e.computerStock = event.calEvent.computerStock;
            e.projecteurUsed = event.calEvent.projecteurUsed;
            e.projecteurStock = event.calEvent.projecteurStock;
        } else {
            e = (event.calEvent instanceof FormerEvent ? new FormerEvent() : new MyEvent());
        }

        e.id = event.calEvent.id;
        e.title = event.calEvent.title;
        e.start = (event.view.name === 'month' ? event.calEvent.start.stripTime().format() : event.calEvent.start.format());
        e.end = (event.calEvent.end ? event.calEvent.end.stripTime().format() : e.start);
        e.allDay = event.calEvent.allDay;
        return e;
    }

    handleDayClick(event: any) {
        this.event = null;
        this.dialogVisible = false;
    }

    handleEventClick(e: any) {
        this.event = this.getEventFromSchedule(e);
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

    handleChangeStep(label: string) {
        this.msgs.length = 0;
        this.msgs.push({severity: 'info', summary: label});
    }

    // formation are created from formation entity menu
    saveEvent() {
        // update
        if (this.event.id) {
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

    // formation are deleted from formation entity menu
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

    closeEventDetailModal() {
        this.dialogVisible = false;
    }
}
