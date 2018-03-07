import { BaseEntity } from './../../shared';

export class Disponibilite implements BaseEntity {
    constructor(
        public id?: number,
        public dateDebut?: any,
        public dateFin?: any,
        public formateur?: BaseEntity,
    ) {
    }
}
