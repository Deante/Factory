package factory.service;

import factory.domain.Formateur;
import factory.repository.FormateurRepository;
import factory.repository.search.FormateurSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Formateur.
 */
@Service
@Transactional
public class FormateurService {

    private final Logger log = LoggerFactory.getLogger(FormateurService.class);

    private final FormateurRepository formateurRepository;

    private final FormateurSearchRepository formateurSearchRepository;

    public FormateurService(FormateurRepository formateurRepository, FormateurSearchRepository formateurSearchRepository) {
        this.formateurRepository = formateurRepository;
        this.formateurSearchRepository = formateurSearchRepository;
    }

    /**
     * Save a formateur.
     *
     * @param formateur the entity to save
     * @return the persisted entity
     */
    public Formateur save(Formateur formateur) {
        log.debug("Request to save Formateur : {}", formateur);
        Formateur result = formateurRepository.save(formateur);
        formateurSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the formateurs.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Formateur> findAll(Pageable pageable) {
        log.debug("Request to get all Formateurs");
        return formateurRepository.findAll(pageable);
    }

    /**
     * Get one formateur by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Formateur findOne(Long id) {
        log.debug("Request to get Formateur : {}", id);
        return formateurRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the formateur by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Formateur : {}", id);
        formateurRepository.delete(id);
        formateurSearchRepository.delete(id);
    }

    /**
     * Search for the formateur corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Formateur> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Formateurs for query {}", query);
        Page<Formateur> result = formateurSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
