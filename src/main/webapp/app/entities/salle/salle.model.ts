import { BaseEntity } from './../../shared';

export class Salle implements BaseEntity {
    constructor(public id?: number
                , public code?: string
                , public capacite?: number
                , public etage?: number
                , public projecteur?: BaseEntity
                , public formations?: BaseEntity[]) {
    }
}
