package factory.service;

import factory.domain.Projecteur;
import factory.repository.ProjecteurRepository;
import factory.repository.search.ProjecteurSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Projecteur.
 */
@Service
@Transactional
public class ProjecteurService {

    private final Logger log = LoggerFactory.getLogger(ProjecteurService.class);

    private final ProjecteurRepository projecteurRepository;

    private final ProjecteurSearchRepository projecteurSearchRepository;

    public ProjecteurService(ProjecteurRepository projecteurRepository, ProjecteurSearchRepository projecteurSearchRepository) {
        this.projecteurRepository = projecteurRepository;
        this.projecteurSearchRepository = projecteurSearchRepository;
    }

    /**
     * Save a projecteur.
     *
     * @param projecteur the entity to save
     * @return the persisted entity
     */
    public Projecteur save(Projecteur projecteur) {
        log.debug("Request to save Projecteur : {}", projecteur);
        Projecteur result = projecteurRepository.save(projecteur);
        projecteurSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the projecteurs.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Projecteur> findAll(Pageable pageable) {
        log.debug("Request to get all Projecteurs");
        return projecteurRepository.findAll(pageable);
    }


    /**
     *  get all the projecteurs where Salle is null.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<Projecteur> findAllWhereSalleIsNull() {
        log.debug("Request to get all projecteurs where Salle is null");
        return StreamSupport
            .stream(projecteurRepository.findAll().spliterator(), false)
            .filter(projecteur -> projecteur.getSalle() == null)
            .collect(Collectors.toList());
    }

    /**
     * Get one projecteur by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Projecteur findOne(Long id) {
        log.debug("Request to get Projecteur : {}", id);
        return projecteurRepository.findOne(id);
    }

    /**
     * Delete the projecteur by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Projecteur : {}", id);
        projecteurRepository.delete(id);
        projecteurSearchRepository.delete(id);
    }

    /**
     * Search for the projecteur corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Projecteur> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Projecteurs for query {}", query);
        Page<Projecteur> result = projecteurSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
