import { BaseEntity } from './../../shared';

export const enum NiveauEnum {
    'DEBUTANT',
    'INTERMEDIAIRE',
    'AVANCE'
}

export class Stagiaire implements BaseEntity {
    constructor(
        public id?: number,
        public niveau?: NiveauEnum,
        public ordinateurs?: BaseEntity[],
        public formation?: BaseEntity,
        public ordinateur?: BaseEntity,
    ) {
    }
}
