export class MyEvent {
    id: number;
    title: string;
    start: string;
    end: string;
    color: string;
    textColor: string;
    allDay = true;
}

export class FormationEvent extends MyEvent {
    salleCode: string;
    salleCapacity: number;
    projecteurState: string;
    stagiaireCount: number;
}

export class TechnicianEvent extends MyEvent {
    computerUsed: number;
    computerStock: number;
    projecteurUsed: number;
    projecteurStock: number;
}

export class FormerEvent extends MyEvent {

}
