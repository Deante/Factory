package factory.service;

import factory.domain.Departement;
import factory.repository.DepartementRepository;
import factory.repository.search.DepartementSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Departement.
 */
@Service
@Transactional
public class DepartementService {

    private final Logger log = LoggerFactory.getLogger(DepartementService.class);

    private final DepartementRepository departementRepository;

    private final DepartementSearchRepository departementSearchRepository;

    public DepartementService(DepartementRepository departementRepository, DepartementSearchRepository departementSearchRepository) {
        this.departementRepository = departementRepository;
        this.departementSearchRepository = departementSearchRepository;
    }

    /**
     * Save a departement.
     *
     * @param departement the entity to save
     * @return the persisted entity
     */
    public Departement save(Departement departement) {
        log.debug("Request to save Departement : {}", departement);
        Departement result = departementRepository.save(departement);
        departementSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the departements.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Departement> findAll(Pageable pageable) {
        log.debug("Request to get all Departements");
        return departementRepository.findAll(pageable);
    }

    /**
     * Get one departement by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Departement findOne(Long id) {
        log.debug("Request to get Departement : {}", id);
        return departementRepository.findOne(id);
    }

    /**
     * Delete the departement by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Departement : {}", id);
        departementRepository.delete(id);
        departementSearchRepository.delete(id);
    }

    /**
     * Search for the departement corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Departement> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Departements for query {}", query);
        Page<Departement> result = departementSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
