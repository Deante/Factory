import { BaseEntity, User } from './../../shared';

export const enum NiveauEnum {
    'DEBUTANT',
    'INTERMEDIAIRE',
    'AVANCE'
}

export class Stagiaire implements BaseEntity {
    constructor(
        public id?: number,
        public niveau?: NiveauEnum,
        public user?: User,
        public formation?: BaseEntity,
        public ordinateur?: BaseEntity,
    ) {
    }
}
