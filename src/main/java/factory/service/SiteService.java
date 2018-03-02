package factory.service;

import factory.domain.Site;
import factory.repository.SiteRepository;
import factory.repository.search.SiteSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Site.
 */
@Service
@Transactional
public class SiteService {

    private final Logger log = LoggerFactory.getLogger(SiteService.class);

    private final SiteRepository siteRepository;

    private final SiteSearchRepository siteSearchRepository;

    public SiteService(SiteRepository siteRepository, SiteSearchRepository siteSearchRepository) {
        this.siteRepository = siteRepository;
        this.siteSearchRepository = siteSearchRepository;
    }

    /**
     * Save a site.
     *
     * @param site the entity to save
     * @return the persisted entity
     */
    public Site save(Site site) {
        log.debug("Request to save Site : {}", site);
        Site result = siteRepository.save(site);
        siteSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the sites.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Site> findAll(Pageable pageable) {
        log.debug("Request to get all Sites");
        return siteRepository.findAll(pageable);
    }

    /**
     * Get one site by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Site findOne(Long id) {
        log.debug("Request to get Site : {}", id);
        return siteRepository.findOne(id);
    }

    /**
     * Delete the site by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Site : {}", id);
        siteRepository.delete(id);
        siteSearchRepository.delete(id);
    }

    /**
     * Search for the site corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Site> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Sites for query {}", query);
        Page<Site> result = siteSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
