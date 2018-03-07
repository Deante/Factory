package factory.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.annotation.JsonView;

import factory.domain.Stagiaire;
import factory.domain.Views;
import factory.service.StagiaireService;
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

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Stagiaire.
 */
@RestController
@RequestMapping("/api")
public class StagiaireResource {

    private final Logger log = LoggerFactory.getLogger(StagiaireResource.class);

    private static final String ENTITY_NAME = "stagiaire";

    private final StagiaireService stagiaireService;

    public StagiaireResource(StagiaireService stagiaireService) {
        this.stagiaireService = stagiaireService;
    }

    /**
     * POST  /stagiaires : Create a new stagiaire.
     *
     * @param stagiaire the stagiaire to create
     * @return the ResponseEntity with status 201 (Created) and with body the new stagiaire, or with status 400 (Bad Request) if the stagiaire has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/stagiaires")
    @Timed
    public ResponseEntity<Stagiaire> createStagiaire(@RequestBody Stagiaire stagiaire) throws URISyntaxException {
        log.debug("REST request to save Stagiaire : {}", stagiaire);
        if (stagiaire.getId() != null) {
            throw new BadRequestAlertException("A new stagiaire cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Stagiaire result = stagiaireService.save(stagiaire);
        return ResponseEntity.created(new URI("/api/stagiaires/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /stagiaires : Updates an existing stagiaire.
     *
     * @param stagiaire the stagiaire to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated stagiaire,
     * or with status 400 (Bad Request) if the stagiaire is not valid,
     * or with status 500 (Internal Server Error) if the stagiaire couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/stagiaires")
    @Timed
    public ResponseEntity<Stagiaire> updateStagiaire(@RequestBody Stagiaire stagiaire) throws URISyntaxException {
        log.debug("REST request to update Stagiaire : {}", stagiaire);
        if (stagiaire.getId() == null) {
            return createStagiaire(stagiaire);
        }
        Stagiaire result = stagiaireService.save(stagiaire);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, stagiaire.getId().toString()))
            .body(result);
    }

    /**
     * GET  /stagiaires : get all the stagiaires.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of stagiaires in body
     */
    @GetMapping("/stagiaires")
    @Timed
    @JsonView(Views.Stagiaire.class)
    public ResponseEntity<List<Stagiaire>> getAllStagiaires(Pageable pageable) {
        log.debug("REST request to get a page of Stagiaires");
        Page<Stagiaire> page = stagiaireService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/stagiaires");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /stagiaires/:id : get the "id" stagiaire.
     *
     * @param id the id of the stagiaire to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the stagiaire, or with status 404 (Not Found)
     */
    @GetMapping("/stagiaires/{id}")
    @Timed
    @JsonView(Views.Stagiaire.class)
    public ResponseEntity<Stagiaire> getStagiaire(@PathVariable Long id) {
        log.debug("REST request to get Stagiaire : {}", id);
        Stagiaire stagiaire = stagiaireService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(stagiaire));
    }

    /**
     * DELETE  /stagiaires/:id : delete the "id" stagiaire.
     *
     * @param id the id of the stagiaire to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/stagiaires/{id}")
    @Timed
    public ResponseEntity<Void> deleteStagiaire(@PathVariable Long id) {
        log.debug("REST request to delete Stagiaire : {}", id);
        stagiaireService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/stagiaires?query=:query : search for the stagiaire corresponding
     * to the query.
     *
     * @param query the query of the stagiaire search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/stagiaires")
    @Timed
    public ResponseEntity<List<Stagiaire>> searchStagiaires(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Stagiaires for query {}", query);
        Page<Stagiaire> page = stagiaireService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/stagiaires");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
