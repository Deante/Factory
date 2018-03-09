import { Salle } from './../entities/salle/salle.model';
import { Component, OnInit, NgModule } from '@angular/core';
import { JhiLanguageService } from 'ng-jhipster';
import {Message} from 'primeng/components/common/api';
import {EventService} from './service/event.service';
import {MyEvent} from './event/event';
import { FormationService, Formation } from '../entities/formation';
import { ResponseWrapper } from '../shared';

@Component({
    selector: 'jhi-schedule',
    templateUrl: './schedule.component.html',
    styles: []
})
export class ScheduleComponent implements OnInit {
    msgs: Message[] = [];
    activeIndex = 0;
    events: any[];
    headerConfig: any;
    event: MyEvent;
    dialogVisible = false;
    idGen = 100;
    fr: any;

    constructor(private eventService: EventService
                , private formationService: FormationService) { }

    ngOnInit() {
        // this.eventService.getEvents().subscribe((events: any) => {this.events = events.data; });
        this.eventService.getFormationEvents(this.formationService).subscribe((events: any) => { this.events = this.loadFormationEvent(events) });

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
        this.eventService.getFormationEvents(this.formationService).subscribe((response: ResponseWrapper) => { this.events = this.loadFormationEvent(response) });
    }

    private loadFormationEvent(response: ResponseWrapper): Array<any> {
        const result: Array<any> = Array<any>();
        for (const f of response.json) {
            const e: MyEvent = new MyEvent();
            e.id = f.id;
            e.title = f.nom;
            e.start = f.dateDebutForm;
            e.end = f.dateFinForm;
<<<<<<< HEAD
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
=======
>>>>>>> parent of b9ae5c8... Merge remote-tracking branch 'origin/master'
            result.push(e);
        }
        return result;
    }

<<<<<<< HEAD
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

=======
>>>>>>> parent of b9ae5c8... Merge remote-tracking branch 'origin/master'
    handleDayClick(event: any) {
        this.event = new MyEvent();
        this.event.start = event.date.format();
        this.dialogVisible = true;
    }

    handleEventClick(e: any) {
        this.event = new MyEvent();
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
        this.dialogVisible = true;
    }

    handleEventMouseover(event: any) {
        this.msgs.length = 0;
        this.msgs.push({severity: 'info', summary: 'Event mouse over'});
    }

    onEventMouseout(event: any) {
        this.msgs.length = 0;
        this.msgs.push({severity: 'info', summary: 'Event mouse over'});
    }

    onEventDragStart(event: any) {
        this.msgs.length = 0;
        this.msgs.push({severity: 'info', summary: 'Event mouse over'});
    }

    onEventDragStop(event: any) {
        this.msgs.length = 0;
        this.msgs.push({severity: 'info', summary: 'Event mouse over'});
    }

    onEventDrop(event: any) {
        this.msgs.length = 0;
        this.msgs.push({severity: 'info', summary: 'Event mouse over'});
    }

    onEventResizeStart(event: any) {
        this.msgs.length = 0;
        this.msgs.push({severity: 'info', summary: 'Event mouse over'});
    }

    onEventResizeStop(event: any) {
        this.msgs.length = 0;
        this.msgs.push({severity: 'info', summary: 'Event mouse over'});
    }

    onEventResize(event: any) {
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

    onChangeStep(label: string) {
        this.msgs.length = 0;
        this.msgs.push({severity: 'info', summary: label});
    }

}
