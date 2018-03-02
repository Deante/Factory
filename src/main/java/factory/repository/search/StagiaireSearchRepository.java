package factory.repository.search;

import factory.domain.Stagiaire;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Stagiaire entity.
 */
public interface StagiaireSearchRepository extends ElasticsearchRepository<Stagiaire, Long> {
}
