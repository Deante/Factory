import { BaseEntity } from './../../shared';

export const enum EtatMaterielEnum {
    'OK',
    'ABIME',
    'INUTILISABLE'
}

export class Ordinateur implements BaseEntity {
    constructor(
        public id?: number,
        public code?: string,
        public processeur?: string,
        public memoire?: number,
        public disque?: number,
        public anneeAchat?: string,
        public coutJour?: number,
        public etat?: EtatMaterielEnum,
        public stagiaires?: BaseEntity[],
    ) {
    }
}
