package factory.service;

import factory.domain.Formation;
import factory.repository.FormationRepository;
import factory.repository.search.FormationSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Formation.
 */
@Service
@Transactional
public class FormationService {

    private final Logger log = LoggerFactory.getLogger(FormationService.class);

    private final FormationRepository formationRepository;

    private final FormationSearchRepository formationSearchRepository;

    public FormationService(FormationRepository formationRepository, FormationSearchRepository formationSearchRepository) {
        this.formationRepository = formationRepository;
        this.formationSearchRepository = formationSearchRepository;
    }

    /**
     * Save a formation.
     *
     * @param formation the entity to save
     * @return the persisted entity
     */
    public Formation save(Formation formation) {
        log.debug("Request to save Formation : {}", formation);
        Formation result = formationRepository.save(formation);
        formationSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the formations.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Formation> findAll(Pageable pageable) {
        log.debug("Request to get all Formations");
        return formationRepository.findAll(pageable);
    }

    /**
     * Get one formation by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Formation findOne(Long id) {
        log.debug("Request to get Formation : {}", id);
        return formationRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the formation by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Formation : {}", id);
        formationRepository.delete(id);
        formationSearchRepository.delete(id);
    }

    /**
     * Search for the formation corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Formation> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Formations for query {}", query);
        Page<Formation> result = formationSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
