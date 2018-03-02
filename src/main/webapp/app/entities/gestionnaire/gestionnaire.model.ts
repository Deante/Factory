import { BaseEntity, User } from './../../shared';

export class Gestionnaire implements BaseEntity {
    constructor(
        public id?: number,
        public user?: User,
        public formations?: BaseEntity[],
    ) {
    }
}
