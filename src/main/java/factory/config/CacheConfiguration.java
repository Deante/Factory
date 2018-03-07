package factory.config;

import io.github.jhipster.config.JHipsterProperties;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;
import org.ehcache.jsr107.Eh107Configuration;

import java.util.concurrent.TimeUnit;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
@AutoConfigureAfter(value = { MetricsConfiguration.class })
@AutoConfigureBefore(value = { WebConfigurer.class, DatabaseConfiguration.class })
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(Expirations.timeToLiveExpiration(Duration.of(ehcache.getTimeToLiveSeconds(), TimeUnit.SECONDS)))
                .build());
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            cm.createCache(factory.repository.UserRepository.USERS_BY_LOGIN_CACHE, jcacheConfiguration);
            cm.createCache(factory.repository.UserRepository.USERS_BY_EMAIL_CACHE, jcacheConfiguration);
            cm.createCache(factory.domain.User.class.getName(), jcacheConfiguration);
            cm.createCache(factory.domain.Authority.class.getName(), jcacheConfiguration);
            cm.createCache(factory.domain.User.class.getName() + ".authorities", jcacheConfiguration);
            cm.createCache(factory.domain.Site.class.getName(), jcacheConfiguration);
            cm.createCache(factory.domain.Site.class.getName() + ".departements", jcacheConfiguration);
            cm.createCache(factory.domain.Departement.class.getName(), jcacheConfiguration);
            cm.createCache(factory.domain.Departement.class.getName() + ".formations", jcacheConfiguration);
            cm.createCache(factory.domain.Competence.class.getName(), jcacheConfiguration);
            cm.createCache(factory.domain.Competence.class.getName() + ".matieres", jcacheConfiguration);
            cm.createCache(factory.domain.Competence.class.getName() + ".formateurs", jcacheConfiguration);
            cm.createCache(factory.domain.Module.class.getName(), jcacheConfiguration);
            cm.createCache(factory.domain.Module.class.getName() + ".formations", jcacheConfiguration);
            cm.createCache(factory.domain.Module.class.getName() + ".matieres", jcacheConfiguration);
            cm.createCache(factory.domain.Formation.class.getName(), jcacheConfiguration);
            cm.createCache(factory.domain.Formation.class.getName() + ".formateurs", jcacheConfiguration);
            cm.createCache(factory.domain.Formation.class.getName() + ".stagiaires", jcacheConfiguration);
            cm.createCache(factory.domain.Formation.class.getName() + ".modules", jcacheConfiguration);
            cm.createCache(factory.domain.Gestionnaire.class.getName(), jcacheConfiguration);
            cm.createCache(factory.domain.Gestionnaire.class.getName() + ".formations", jcacheConfiguration);
            cm.createCache(factory.domain.Formateur.class.getName(), jcacheConfiguration);
            cm.createCache(factory.domain.Formateur.class.getName() + ".competences", jcacheConfiguration);
            cm.createCache(factory.domain.Formateur.class.getName() + ".formations", jcacheConfiguration);
            cm.createCache(factory.domain.Technicien.class.getName(), jcacheConfiguration);
            cm.createCache(factory.domain.Technicien.class.getName() + ".formations", jcacheConfiguration);
            cm.createCache(factory.domain.Matiere.class.getName(), jcacheConfiguration);
            cm.createCache(factory.domain.Matiere.class.getName() + ".modules", jcacheConfiguration);
            cm.createCache(factory.domain.Matiere.class.getName() + ".competences", jcacheConfiguration);
            cm.createCache(factory.domain.Salle.class.getName(), jcacheConfiguration);
            cm.createCache(factory.domain.Salle.class.getName() + ".formations", jcacheConfiguration);
            cm.createCache(factory.domain.Stagiaire.class.getName(), jcacheConfiguration);
            cm.createCache(factory.domain.Projecteur.class.getName(), jcacheConfiguration);
            cm.createCache(factory.domain.Ordinateur.class.getName(), jcacheConfiguration);
            cm.createCache(factory.domain.Ordinateur.class.getName() + ".stagiaires", jcacheConfiguration);
            cm.createCache(factory.domain.Stagiaire.class.getName() + ".ordinateurs", jcacheConfiguration);
            cm.createCache(factory.domain.Module.class.getName() + ".formateurs", jcacheConfiguration);
            cm.createCache(factory.domain.Formateur.class.getName() + ".modules", jcacheConfiguration);
            cm.createCache(factory.domain.Disponibilite.class.getName(), jcacheConfiguration);
            cm.createCache(factory.domain.Formateur.class.getName() + ".disponibilites", jcacheConfiguration);
            // jhipster-needle-ehcache-add-entry
        };
    }
}
