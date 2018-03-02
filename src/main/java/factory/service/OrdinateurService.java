package factory.service;

import factory.domain.Ordinateur;
import factory.repository.OrdinateurRepository;
import factory.repository.search.OrdinateurSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Ordinateur.
 */
@Service
@Transactional
public class OrdinateurService {

    private final Logger log = LoggerFactory.getLogger(OrdinateurService.class);

    private final OrdinateurRepository ordinateurRepository;

    private final OrdinateurSearchRepository ordinateurSearchRepository;

    public OrdinateurService(OrdinateurRepository ordinateurRepository, OrdinateurSearchRepository ordinateurSearchRepository) {
        this.ordinateurRepository = ordinateurRepository;
        this.ordinateurSearchRepository = ordinateurSearchRepository;
    }

    /**
     * Save a ordinateur.
     *
     * @param ordinateur the entity to save
     * @return the persisted entity
     */
    public Ordinateur save(Ordinateur ordinateur) {
        log.debug("Request to save Ordinateur : {}", ordinateur);
        Ordinateur result = ordinateurRepository.save(ordinateur);
        ordinateurSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the ordinateurs.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Ordinateur> findAll(Pageable pageable) {
        log.debug("Request to get all Ordinateurs");
        return ordinateurRepository.findAll(pageable);
    }

    /**
     * Get one ordinateur by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Ordinateur findOne(Long id) {
        log.debug("Request to get Ordinateur : {}", id);
        return ordinateurRepository.findOne(id);
    }

    /**
     * Delete the ordinateur by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Ordinateur : {}", id);
        ordinateurRepository.delete(id);
        ordinateurSearchRepository.delete(id);
    }

    /**
     * Search for the ordinateur corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Ordinateur> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Ordinateurs for query {}", query);
        Page<Ordinateur> result = ordinateurSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
