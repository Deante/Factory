import { BaseEntity } from './../../shared';

export class Matiere implements BaseEntity {
    constructor(
        public id?: number,
        public nom?: string,
        public modules?: BaseEntity[],
        public competences?: BaseEntity[],
    ) {
    }
}
