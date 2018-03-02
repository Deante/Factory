package factory.service;

import factory.domain.Gestionnaire;
import factory.repository.GestionnaireRepository;
import factory.repository.search.GestionnaireSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Gestionnaire.
 */
@Service
@Transactional
public class GestionnaireService {

    private final Logger log = LoggerFactory.getLogger(GestionnaireService.class);

    private final GestionnaireRepository gestionnaireRepository;

    private final GestionnaireSearchRepository gestionnaireSearchRepository;

    public GestionnaireService(GestionnaireRepository gestionnaireRepository, GestionnaireSearchRepository gestionnaireSearchRepository) {
        this.gestionnaireRepository = gestionnaireRepository;
        this.gestionnaireSearchRepository = gestionnaireSearchRepository;
    }

    /**
     * Save a gestionnaire.
     *
     * @param gestionnaire the entity to save
     * @return the persisted entity
     */
    public Gestionnaire save(Gestionnaire gestionnaire) {
        log.debug("Request to save Gestionnaire : {}", gestionnaire);
        Gestionnaire result = gestionnaireRepository.save(gestionnaire);
        gestionnaireSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the gestionnaires.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Gestionnaire> findAll(Pageable pageable) {
        log.debug("Request to get all Gestionnaires");
        return gestionnaireRepository.findAll(pageable);
    }

    /**
     * Get one gestionnaire by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Gestionnaire findOne(Long id) {
        log.debug("Request to get Gestionnaire : {}", id);
        return gestionnaireRepository.findOne(id);
    }

    /**
     * Delete the gestionnaire by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Gestionnaire : {}", id);
        gestionnaireRepository.delete(id);
        gestionnaireSearchRepository.delete(id);
    }

    /**
     * Search for the gestionnaire corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Gestionnaire> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Gestionnaires for query {}", query);
        Page<Gestionnaire> result = gestionnaireSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
