import { BaseEntity, User } from './../../shared';

export class Formateur implements BaseEntity {
    constructor(
        public id?: number,
        public user?: User,
        public competences?: BaseEntity[],
        public formations?: BaseEntity[],
        public modules?: BaseEntity[],
        public disponibilites?: BaseEntity[],
    ) {
    }
}
