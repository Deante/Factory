package factory.repository.search;

import factory.domain.Gestionnaire;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Gestionnaire entity.
 */
public interface GestionnaireSearchRepository extends ElasticsearchRepository<Gestionnaire, Long> {
}
