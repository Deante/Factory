import { BaseEntity } from './../../shared';

export class Departement implements BaseEntity {
    constructor(
        public id?: number,
        public nom?: string,
        public etages?: number,
        public site?: BaseEntity,
        public formations?: BaseEntity[],
    ) {
    }
}
