package factory.repository.search;

import factory.domain.Module;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Module entity.
 */
public interface ModuleSearchRepository extends ElasticsearchRepository<Module, Long> {
}
