import { BaseEntity } from './../../shared';

export const enum NiveauEnum {
    'DEBUTANT',
    'INTERMEDIAIRE',
    'AVANCE'
}

export class Competence implements BaseEntity {
    constructor(
        public id?: number,
        public nom?: string,
        public niveau?: NiveauEnum,
        public matieres?: BaseEntity[],
        public formateurs?: BaseEntity[],
    ) {
    }
}
