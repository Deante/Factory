package factory.repository.search;

import factory.domain.Technicien;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Technicien entity.
 */
public interface TechnicienSearchRepository extends ElasticsearchRepository<Technicien, Long> {
}
