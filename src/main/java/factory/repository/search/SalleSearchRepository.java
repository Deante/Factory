package factory.repository.search;

import factory.domain.Salle;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Salle entity.
 */
public interface SalleSearchRepository extends ElasticsearchRepository<Salle, Long> {
}
