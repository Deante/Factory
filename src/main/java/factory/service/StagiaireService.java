package factory.service;

import factory.domain.Stagiaire;
import factory.domain.Views;
import factory.repository.StagiaireRepository;
import factory.repository.search.StagiaireSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

import com.fasterxml.jackson.annotation.JsonView;

/**
 * Service Implementation for managing Stagiaire.
 */
@Service
@Transactional
public class StagiaireService {

    private final Logger log = LoggerFactory.getLogger(StagiaireService.class);

    private final StagiaireRepository stagiaireRepository;

    private final StagiaireSearchRepository stagiaireSearchRepository;

    public StagiaireService(StagiaireRepository stagiaireRepository, StagiaireSearchRepository stagiaireSearchRepository) {
        this.stagiaireRepository = stagiaireRepository;
        this.stagiaireSearchRepository = stagiaireSearchRepository;
    }

    /**
     * Save a stagiaire.
     *
     * @param stagiaire the entity to save
     * @return the persisted entity
     */
    public Stagiaire save(Stagiaire stagiaire) {
        log.debug("Request to save Stagiaire : {}", stagiaire);
        Stagiaire result = stagiaireRepository.save(stagiaire);
        stagiaireSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the stagiaires.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    @JsonView(Views.Stagiaire.class)
    public Page<Stagiaire> findAll(Pageable pageable) {
        log.debug("Request to get all Stagiaires");
        return stagiaireRepository.findAll(pageable);
    }

    /**
     * Get one stagiaire by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    @JsonView(Views.Stagiaire.class)
    public Stagiaire findOne(Long id) {
        log.debug("Request to get Stagiaire : {}", id);
        return stagiaireRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the stagiaire by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Stagiaire : {}", id);
        stagiaireRepository.delete(id);
        stagiaireSearchRepository.delete(id);
    }

    /**
     * Search for the stagiaire corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Stagiaire> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Stagiaires for query {}", query);
        Page<Stagiaire> result = stagiaireSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
