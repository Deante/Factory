import { Component, OnInit } from '@angular/core';
import { JhiLanguageService } from 'ng-jhipster';
import {Message} from 'primeng/components/common/api';
import {EventService} from './service/event.service';
import {MyEvent} from './event/event';

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

    constructor(private eventService: EventService) { }

    ngOnInit() {
        this.eventService.getEvents().subscribe((events: any) => {this.events = events.data; });

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
        this.eventService.getEvents().subscribe((events: any) => {this.events = events.data; });
    }

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
