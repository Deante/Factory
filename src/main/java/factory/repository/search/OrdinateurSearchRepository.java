package factory.repository.search;

import factory.domain.Ordinateur;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Ordinateur entity.
 */
public interface OrdinateurSearchRepository extends ElasticsearchRepository<Ordinateur, Long> {
}
