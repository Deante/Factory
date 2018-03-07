import { BaseEntity } from './../../shared';

export const enum CouleurEnum {
    'ROUGE',
    'VERT',
    'BLEU',
    'JAUNE',
    'NOIR'
}

export class Module implements BaseEntity {
    constructor(
        public id?: number,
        public titre?: string,
        public duree?: number,
        public couleur?: CouleurEnum,
        public prerequis?: string,
        public contenu?: string,
        public formations?: BaseEntity[],
        public matieres?: BaseEntity[],
        public formateurs?: BaseEntity[],
    ) {
    }
}
