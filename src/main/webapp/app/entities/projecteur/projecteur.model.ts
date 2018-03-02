import { BaseEntity } from './../../shared';

export const enum EtatMaterielEnum {
    'OK',
    'ABIME',
    'INUTILISABLE'
}

export class Projecteur implements BaseEntity {
    constructor(
        public id?: number,
        public code?: string,
        public coutJour?: number,
        public etat?: EtatMaterielEnum,
        public salle?: BaseEntity,
    ) {
    }
}
