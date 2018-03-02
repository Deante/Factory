import { BaseEntity, User } from './../../shared';

export class Technicien implements BaseEntity {
    constructor(
        public id?: number,
        public user?: User,
        public formations?: BaseEntity[],
    ) {
    }
}
