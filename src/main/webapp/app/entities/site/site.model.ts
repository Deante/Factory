import { BaseEntity } from './../../shared';

export class Site implements BaseEntity {
    constructor(
        public id?: number,
        public nom?: string,
        public voie?: string,
        public complement?: string,
        public codePostal?: string,
        public ville?: string,
        public telephone?: string,
        public departements?: BaseEntity[],
    ) {
    }
}
