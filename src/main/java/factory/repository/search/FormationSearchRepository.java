package factory.repository.search;

import factory.domain.Formation;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Formation entity.
 */
public interface FormationSearchRepository extends ElasticsearchRepository<Formation, Long> {
}
