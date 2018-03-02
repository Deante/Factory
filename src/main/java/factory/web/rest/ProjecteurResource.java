package factory.web.rest;

import com.codahale.metrics.annotation.Timed;
import factory.domain.Projecteur;
import factory.service.ProjecteurService;
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
 * REST controller for managing Projecteur.
 */
@RestController
@RequestMapping("/api")
public class ProjecteurResource {

    private final Logger log = LoggerFactory.getLogger(ProjecteurResource.class);

    private static final String ENTITY_NAME = "projecteur";

    private final ProjecteurService projecteurService;

    public ProjecteurResource(ProjecteurService projecteurService) {
        this.projecteurService = projecteurService;
    }

    /**
     * POST  /projecteurs : Create a new projecteur.
     *
     * @param projecteur the projecteur to create
     * @return the ResponseEntity with status 201 (Created) and with body the new projecteur, or with status 400 (Bad Request) if the projecteur has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/projecteurs")
    @Timed
    public ResponseEntity<Projecteur> createProjecteur(@Valid @RequestBody Projecteur projecteur) throws URISyntaxException {
        log.debug("REST request to save Projecteur : {}", projecteur);
        if (projecteur.getId() != null) {
            throw new BadRequestAlertException("A new projecteur cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Projecteur result = projecteurService.save(projecteur);
        return ResponseEntity.created(new URI("/api/projecteurs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /projecteurs : Updates an existing projecteur.
     *
     * @param projecteur the projecteur to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated projecteur,
     * or with status 400 (Bad Request) if the projecteur is not valid,
     * or with status 500 (Internal Server Error) if the projecteur couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/projecteurs")
    @Timed
    public ResponseEntity<Projecteur> updateProjecteur(@Valid @RequestBody Projecteur projecteur) throws URISyntaxException {
        log.debug("REST request to update Projecteur : {}", projecteur);
        if (projecteur.getId() == null) {
            return createProjecteur(projecteur);
        }
        Projecteur result = projecteurService.save(projecteur);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, projecteur.getId().toString()))
            .body(result);
    }

    /**
     * GET  /projecteurs : get all the projecteurs.
     *
     * @param pageable the pagination information
     * @param filter the filter of the request
     * @return the ResponseEntity with status 200 (OK) and the list of projecteurs in body
     */
    @GetMapping("/projecteurs")
    @Timed
    public ResponseEntity<List<Projecteur>> getAllProjecteurs(Pageable pageable, @RequestParam(required = false) String filter) {
        if ("salle-is-null".equals(filter)) {
            log.debug("REST request to get all Projecteurs where salle is null");
            return new ResponseEntity<>(projecteurService.findAllWhereSalleIsNull(),
                    HttpStatus.OK);
        }
        log.debug("REST request to get a page of Projecteurs");
        Page<Projecteur> page = projecteurService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/projecteurs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /projecteurs/:id : get the "id" projecteur.
     *
     * @param id the id of the projecteur to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the projecteur, or with status 404 (Not Found)
     */
    @GetMapping("/projecteurs/{id}")
    @Timed
    public ResponseEntity<Projecteur> getProjecteur(@PathVariable Long id) {
        log.debug("REST request to get Projecteur : {}", id);
        Projecteur projecteur = projecteurService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(projecteur));
    }

    /**
     * DELETE  /projecteurs/:id : delete the "id" projecteur.
     *
     * @param id the id of the projecteur to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/projecteurs/{id}")
    @Timed
    public ResponseEntity<Void> deleteProjecteur(@PathVariable Long id) {
        log.debug("REST request to delete Projecteur : {}", id);
        projecteurService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/projecteurs?query=:query : search for the projecteur corresponding
     * to the query.
     *
     * @param query the query of the projecteur search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/projecteurs")
    @Timed
    public ResponseEntity<List<Projecteur>> searchProjecteurs(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Projecteurs for query {}", query);
        Page<Projecteur> page = projecteurService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/projecteurs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
