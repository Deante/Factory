package factory.service;

import factory.domain.Technicien;
import factory.repository.TechnicienRepository;
import factory.repository.search.TechnicienSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Technicien.
 */
@Service
@Transactional
public class TechnicienService {

    private final Logger log = LoggerFactory.getLogger(TechnicienService.class);

    private final TechnicienRepository technicienRepository;

    private final TechnicienSearchRepository technicienSearchRepository;

    public TechnicienService(TechnicienRepository technicienRepository, TechnicienSearchRepository technicienSearchRepository) {
        this.technicienRepository = technicienRepository;
        this.technicienSearchRepository = technicienSearchRepository;
    }

    /**
     * Save a technicien.
     *
     * @param technicien the entity to save
     * @return the persisted entity
     */
    public Technicien save(Technicien technicien) {
        log.debug("Request to save Technicien : {}", technicien);
        Technicien result = technicienRepository.save(technicien);
        technicienSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the techniciens.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Technicien> findAll(Pageable pageable) {
        log.debug("Request to get all Techniciens");
        return technicienRepository.findAll(pageable);
    }

    /**
     * Get one technicien by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Technicien findOne(Long id) {
        log.debug("Request to get Technicien : {}", id);
        return technicienRepository.findOne(id);
    }

    /**
     * Delete the technicien by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Technicien : {}", id);
        technicienRepository.delete(id);
        technicienSearchRepository.delete(id);
    }

    /**
     * Search for the technicien corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Technicien> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Techniciens for query {}", query);
        Page<Technicien> result = technicienSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
