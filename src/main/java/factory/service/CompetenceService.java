package factory.service;

import factory.domain.Competence;
import factory.repository.CompetenceRepository;
import factory.repository.search.CompetenceSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Competence.
 */
@Service
@Transactional
public class CompetenceService {

    private final Logger log = LoggerFactory.getLogger(CompetenceService.class);

    private final CompetenceRepository competenceRepository;

    private final CompetenceSearchRepository competenceSearchRepository;

    public CompetenceService(CompetenceRepository competenceRepository, CompetenceSearchRepository competenceSearchRepository) {
        this.competenceRepository = competenceRepository;
        this.competenceSearchRepository = competenceSearchRepository;
    }

    /**
     * Save a competence.
     *
     * @param competence the entity to save
     * @return the persisted entity
     */
    public Competence save(Competence competence) {
        log.debug("Request to save Competence : {}", competence);
        Competence result = competenceRepository.save(competence);
        competenceSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the competences.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Competence> findAll(Pageable pageable) {
        log.debug("Request to get all Competences");
        return competenceRepository.findAll(pageable);
    }

    /**
     * Get one competence by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Competence findOne(Long id) {
        log.debug("Request to get Competence : {}", id);
        return competenceRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the competence by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Competence : {}", id);
        competenceRepository.delete(id);
        competenceSearchRepository.delete(id);
    }

    /**
     * Search for the competence corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Competence> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Competences for query {}", query);
        Page<Competence> result = competenceSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
