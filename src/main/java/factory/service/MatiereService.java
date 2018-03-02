package factory.service;

import factory.domain.Matiere;
import factory.repository.MatiereRepository;
import factory.repository.search.MatiereSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Matiere.
 */
@Service
@Transactional
public class MatiereService {

    private final Logger log = LoggerFactory.getLogger(MatiereService.class);

    private final MatiereRepository matiereRepository;

    private final MatiereSearchRepository matiereSearchRepository;

    public MatiereService(MatiereRepository matiereRepository, MatiereSearchRepository matiereSearchRepository) {
        this.matiereRepository = matiereRepository;
        this.matiereSearchRepository = matiereSearchRepository;
    }

    /**
     * Save a matiere.
     *
     * @param matiere the entity to save
     * @return the persisted entity
     */
    public Matiere save(Matiere matiere) {
        log.debug("Request to save Matiere : {}", matiere);
        Matiere result = matiereRepository.save(matiere);
        matiereSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the matieres.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Matiere> findAll(Pageable pageable) {
        log.debug("Request to get all Matieres");
        return matiereRepository.findAll(pageable);
    }

    /**
     * Get one matiere by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Matiere findOne(Long id) {
        log.debug("Request to get Matiere : {}", id);
        return matiereRepository.findOne(id);
    }

    /**
     * Delete the matiere by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Matiere : {}", id);
        matiereRepository.delete(id);
        matiereSearchRepository.delete(id);
    }

    /**
     * Search for the matiere corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Matiere> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Matieres for query {}", query);
        Page<Matiere> result = matiereSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
