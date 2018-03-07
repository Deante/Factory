package factory.service;

import factory.domain.Disponibilite;
import factory.repository.DisponibiliteRepository;
import factory.repository.search.DisponibiliteSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Disponibilite.
 */
@Service
@Transactional
public class DisponibiliteService {

    private final Logger log = LoggerFactory.getLogger(DisponibiliteService.class);

    private final DisponibiliteRepository disponibiliteRepository;

    private final DisponibiliteSearchRepository disponibiliteSearchRepository;

    public DisponibiliteService(DisponibiliteRepository disponibiliteRepository, DisponibiliteSearchRepository disponibiliteSearchRepository) {
        this.disponibiliteRepository = disponibiliteRepository;
        this.disponibiliteSearchRepository = disponibiliteSearchRepository;
    }

    /**
     * Save a disponibilite.
     *
     * @param disponibilite the entity to save
     * @return the persisted entity
     */
    public Disponibilite save(Disponibilite disponibilite) {
        log.debug("Request to save Disponibilite : {}", disponibilite);
        Disponibilite result = disponibiliteRepository.save(disponibilite);
        disponibiliteSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the disponibilites.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Disponibilite> findAll(Pageable pageable) {
        log.debug("Request to get all Disponibilites");
        return disponibiliteRepository.findAll(pageable);
    }

    /**
     * Get one disponibilite by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Disponibilite findOne(Long id) {
        log.debug("Request to get Disponibilite : {}", id);
        return disponibiliteRepository.findOne(id);
    }

    /**
     * Delete the disponibilite by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Disponibilite : {}", id);
        disponibiliteRepository.delete(id);
        disponibiliteSearchRepository.delete(id);
    }

    /**
     * Search for the disponibilite corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Disponibilite> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Disponibilites for query {}", query);
        Page<Disponibilite> result = disponibiliteSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
