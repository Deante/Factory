package factory.repository.search;

import factory.domain.Competence;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Competence entity.
 */
public interface CompetenceSearchRepository extends ElasticsearchRepository<Competence, Long> {
}
