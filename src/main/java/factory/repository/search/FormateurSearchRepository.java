package factory.repository.search;

import factory.domain.Formateur;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Formateur entity.
 */
public interface FormateurSearchRepository extends ElasticsearchRepository<Formateur, Long> {
}
