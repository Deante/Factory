import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { FactorySiteModule } from './site/site.module';
import { FactoryDepartementModule } from './departement/departement.module';
import { FactoryCompetenceModule } from './competence/competence.module';
import { FactoryModuleModule } from './module/module.module';
import { FactoryFormationModule } from './formation/formation.module';
import { FactoryGestionnaireModule } from './gestionnaire/gestionnaire.module';
import { FactoryFormateurModule } from './formateur/formateur.module';
import { FactoryTechnicienModule } from './technicien/technicien.module';
import { FactoryMatiereModule } from './matiere/matiere.module';
import { FactorySalleModule } from './salle/salle.module';
import { FactoryStagiaireModule } from './stagiaire/stagiaire.module';
import { FactoryProjecteurModule } from './projecteur/projecteur.module';
import { FactoryOrdinateurModule } from './ordinateur/ordinateur.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        FactorySiteModule,
        FactoryDepartementModule,
        FactoryCompetenceModule,
        FactoryModuleModule,
        FactoryFormationModule,
        FactoryGestionnaireModule,
        FactoryFormateurModule,
        FactoryTechnicienModule,
        FactoryMatiereModule,
        FactorySalleModule,
        FactoryStagiaireModule,
        FactoryProjecteurModule,
        FactoryOrdinateurModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class FactoryEntityModule {}
