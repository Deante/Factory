import { BaseEntity } from './../../shared';

export class Formation implements BaseEntity {
    constructor(
        public id?: number,
        public nom?: string,
        public dateDebutForm?: any,
        public dateFinForm?: any,
        public objectifs?: string,
        public departement?: BaseEntity,
        public formateurs?: BaseEntity[],
        public gestionnaire?: BaseEntity,
        public stagiaires?: BaseEntity[],
        public salle?: BaseEntity,
        public technicien?: BaseEntity,
        public modules?: BaseEntity[],
    ) {
    }
}
