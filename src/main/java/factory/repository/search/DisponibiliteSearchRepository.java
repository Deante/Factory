package factory.repository.search;

import factory.domain.Disponibilite;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Disponibilite entity.
 */
public interface DisponibiliteSearchRepository extends ElasticsearchRepository<Disponibilite, Long> {
}
