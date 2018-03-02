package factory.repository.search;

import factory.domain.Departement;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Departement entity.
 */
public interface DepartementSearchRepository extends ElasticsearchRepository<Departement, Long> {
}
