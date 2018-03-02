package factory.web.rest;

import com.codahale.metrics.annotation.Timed;
import factory.domain.Module;
import factory.service.ModuleService;
import factory.web.rest.errors.BadRequestAlertException;
import factory.web.rest.util.HeaderUtil;
import factory.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Module.
 */
@RestController
@RequestMapping("/api")
public class ModuleResource {

    private final Logger log = LoggerFactory.getLogger(ModuleResource.class);

    private static final String ENTITY_NAME = "module";

    private final ModuleService moduleService;

    public ModuleResource(ModuleService moduleService) {
        this.moduleService = moduleService;
    }

    /**
     * POST  /modules : Create a new module.
     *
     * @param module the module to create
     * @return the ResponseEntity with status 201 (Created) and with body the new module, or with status 400 (Bad Request) if the module has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/modules")
    @Timed
    public ResponseEntity<Module> createModule(@Valid @RequestBody Module module) throws URISyntaxException {
        log.debug("REST request to save Module : {}", module);
        if (module.getId() != null) {
            throw new BadRequestAlertException("A new module cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Module result = moduleService.save(module);
        return ResponseEntity.created(new URI("/api/modules/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /modules : Updates an existing module.
     *
     * @param module the module to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated module,
     * or with status 400 (Bad Request) if the module is not valid,
     * or with status 500 (Internal Server Error) if the module couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/modules")
    @Timed
    public ResponseEntity<Module> updateModule(@Valid @RequestBody Module module) throws URISyntaxException {
        log.debug("REST request to update Module : {}", module);
        if (module.getId() == null) {
            return createModule(module);
        }
        Module result = moduleService.save(module);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, module.getId().toString()))
            .body(result);
    }

    /**
     * GET  /modules : get all the modules.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of modules in body
     */
    @GetMapping("/modules")
    @Timed
    public ResponseEntity<List<Module>> getAllModules(Pageable pageable) {
        log.debug("REST request to get a page of Modules");
        Page<Module> page = moduleService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/modules");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /modules/:id : get the "id" module.
     *
     * @param id the id of the module to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the module, or with status 404 (Not Found)
     */
    @GetMapping("/modules/{id}")
    @Timed
    public ResponseEntity<Module> getModule(@PathVariable Long id) {
        log.debug("REST request to get Module : {}", id);
        Module module = moduleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(module));
    }

    /**
     * DELETE  /modules/:id : delete the "id" module.
     *
     * @param id the id of the module to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/modules/{id}")
    @Timed
    public ResponseEntity<Void> deleteModule(@PathVariable Long id) {
        log.debug("REST request to delete Module : {}", id);
        moduleService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/modules?query=:query : search for the module corresponding
     * to the query.
     *
     * @param query the query of the module search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/modules")
    @Timed
    public ResponseEntity<List<Module>> searchModules(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Modules for query {}", query);
        Page<Module> page = moduleService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/modules");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
