package factory.repository.search;

import factory.domain.Projecteur;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Projecteur entity.
 */
public interface ProjecteurSearchRepository extends ElasticsearchRepository<Projecteur, Long> {
}
