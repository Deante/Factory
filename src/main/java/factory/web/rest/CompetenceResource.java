package factory.web.rest;

import com.codahale.metrics.annotation.Timed;
import factory.domain.Competence;
import factory.service.CompetenceService;
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
 * REST controller for managing Competence.
 */
@RestController
@RequestMapping("/api")
public class CompetenceResource {

    private final Logger log = LoggerFactory.getLogger(CompetenceResource.class);

    private static final String ENTITY_NAME = "competence";

    private final CompetenceService competenceService;

    public CompetenceResource(CompetenceService competenceService) {
        this.competenceService = competenceService;
    }

    /**
     * POST  /competences : Create a new competence.
     *
     * @param competence the competence to create
     * @return the ResponseEntity with status 201 (Created) and with body the new competence, or with status 400 (Bad Request) if the competence has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/competences")
    @Timed
    public ResponseEntity<Competence> createCompetence(@Valid @RequestBody Competence competence) throws URISyntaxException {
        log.debug("REST request to save Competence : {}", competence);
        if (competence.getId() != null) {
            throw new BadRequestAlertException("A new competence cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Competence result = competenceService.save(competence);
        return ResponseEntity.created(new URI("/api/competences/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /competences : Updates an existing competence.
     *
     * @param competence the competence to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated competence,
     * or with status 400 (Bad Request) if the competence is not valid,
     * or with status 500 (Internal Server Error) if the competence couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/competences")
    @Timed
    public ResponseEntity<Competence> updateCompetence(@Valid @RequestBody Competence competence) throws URISyntaxException {
        log.debug("REST request to update Competence : {}", competence);
        if (competence.getId() == null) {
            return createCompetence(competence);
        }
        Competence result = competenceService.save(competence);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, competence.getId().toString()))
            .body(result);
    }

    /**
     * GET  /competences : get all the competences.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of competences in body
     */
    @GetMapping("/competences")
    @Timed
    public ResponseEntity<List<Competence>> getAllCompetences(Pageable pageable) {
        log.debug("REST request to get a page of Competences");
        Page<Competence> page = competenceService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/competences");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /competences/:id : get the "id" competence.
     *
     * @param id the id of the competence to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the competence, or with status 404 (Not Found)
     */
    @GetMapping("/competences/{id}")
    @Timed
    public ResponseEntity<Competence> getCompetence(@PathVariable Long id) {
        log.debug("REST request to get Competence : {}", id);
        Competence competence = competenceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(competence));
    }

    /**
     * DELETE  /competences/:id : delete the "id" competence.
     *
     * @param id the id of the competence to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/competences/{id}")
    @Timed
    public ResponseEntity<Void> deleteCompetence(@PathVariable Long id) {
        log.debug("REST request to delete Competence : {}", id);
        competenceService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/competences?query=:query : search for the competence corresponding
     * to the query.
     *
     * @param query the query of the competence search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/competences")
    @Timed
    public ResponseEntity<List<Competence>> searchCompetences(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Competences for query {}", query);
        Page<Competence> page = competenceService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/competences");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
