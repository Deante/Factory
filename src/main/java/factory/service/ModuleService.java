package factory.service;

import factory.domain.Module;
import factory.repository.ModuleRepository;
import factory.repository.search.ModuleSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Module.
 */
@Service
@Transactional
public class ModuleService {

    private final Logger log = LoggerFactory.getLogger(ModuleService.class);

    private final ModuleRepository moduleRepository;

    private final ModuleSearchRepository moduleSearchRepository;

    public ModuleService(ModuleRepository moduleRepository, ModuleSearchRepository moduleSearchRepository) {
        this.moduleRepository = moduleRepository;
        this.moduleSearchRepository = moduleSearchRepository;
    }

    /**
     * Save a module.
     *
     * @param module the entity to save
     * @return the persisted entity
     */
    public Module save(Module module) {
        log.debug("Request to save Module : {}", module);
        Module result = moduleRepository.save(module);
        moduleSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the modules.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Module> findAll(Pageable pageable) {
        log.debug("Request to get all Modules");
        return moduleRepository.findAll(pageable);
    }

    /**
     * Get one module by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Module findOne(Long id) {
        log.debug("Request to get Module : {}", id);
        return moduleRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the module by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Module : {}", id);
        moduleRepository.delete(id);
        moduleSearchRepository.delete(id);
    }

    /**
     * Search for the module corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Module> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Modules for query {}", query);
        Page<Module> result = moduleSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
