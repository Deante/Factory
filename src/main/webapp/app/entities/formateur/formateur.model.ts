import { BaseEntity, User } from './../../shared';

export class Formateur implements BaseEntity {
    constructor(
        public id?: number,
        public dateDebutDispo?: any,
        public dateFinDispo?: any,
        public user?: User,
        public competences?: BaseEntity[],
        public formations?: BaseEntity[],
    ) {
    }
}
