package factory.service;

import factory.domain.Salle;
import factory.repository.SalleRepository;
import factory.repository.search.SalleSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Salle.
 */
@Service
@Transactional
public class SalleService {

    private final Logger log = LoggerFactory.getLogger(SalleService.class);

    private final SalleRepository salleRepository;

    private final SalleSearchRepository salleSearchRepository;

    public SalleService(SalleRepository salleRepository, SalleSearchRepository salleSearchRepository) {
        this.salleRepository = salleRepository;
        this.salleSearchRepository = salleSearchRepository;
    }

    /**
     * Save a salle.
     *
     * @param salle the entity to save
     * @return the persisted entity
     */
    public Salle save(Salle salle) {
        log.debug("Request to save Salle : {}", salle);
        Salle result = salleRepository.save(salle);
        salleSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the salles.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Salle> findAll(Pageable pageable) {
        log.debug("Request to get all Salles");
        return salleRepository.findAll(pageable);
    }

    /**
     * Get one salle by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Salle findOne(Long id) {
        log.debug("Request to get Salle : {}", id);
        return salleRepository.findOne(id);
    }

    /**
     * Delete the salle by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Salle : {}", id);
        salleRepository.delete(id);
        salleSearchRepository.delete(id);
    }

    /**
     * Search for the salle corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Salle> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Salles for query {}", query);
        Page<Salle> result = salleSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
