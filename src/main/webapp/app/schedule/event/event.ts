export class MyEvent {
    id: number;
    title: string;
    start: string;
    end: string;
    allDay = true;
}
    color: string;
    gestionnaire: string;
    allDay = true;
}

export interface IGestionnaireEvent {
    salleCode: string;
    salleCapacity: number;
    stagiaireCount: number;
    formateursList: string[];
}

export class GestionnaireEvent extends MyEvent implements IGestionnaireEvent {
    salleCode: string;
    salleCapacity: number;
    stagiaireCount: number;
    formateursList: string[];
}
>>>>>>> sonny
