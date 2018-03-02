package factory.web.rest;

import com.codahale.metrics.annotation.Timed;
import factory.domain.Salle;
import factory.service.SalleService;
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
 * REST controller for managing Salle.
 */
@RestController
@RequestMapping("/api")
public class SalleResource {

    private final Logger log = LoggerFactory.getLogger(SalleResource.class);

    private static final String ENTITY_NAME = "salle";

    private final SalleService salleService;

    public SalleResource(SalleService salleService) {
        this.salleService = salleService;
    }

    /**
     * POST  /salles : Create a new salle.
     *
     * @param salle the salle to create
     * @return the ResponseEntity with status 201 (Created) and with body the new salle, or with status 400 (Bad Request) if the salle has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/salles")
    @Timed
    public ResponseEntity<Salle> createSalle(@Valid @RequestBody Salle salle) throws URISyntaxException {
        log.debug("REST request to save Salle : {}", salle);
        if (salle.getId() != null) {
            throw new BadRequestAlertException("A new salle cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Salle result = salleService.save(salle);
        return ResponseEntity.created(new URI("/api/salles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /salles : Updates an existing salle.
     *
     * @param salle the salle to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated salle,
     * or with status 400 (Bad Request) if the salle is not valid,
     * or with status 500 (Internal Server Error) if the salle couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/salles")
    @Timed
    public ResponseEntity<Salle> updateSalle(@Valid @RequestBody Salle salle) throws URISyntaxException {
        log.debug("REST request to update Salle : {}", salle);
        if (salle.getId() == null) {
            return createSalle(salle);
        }
        Salle result = salleService.save(salle);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, salle.getId().toString()))
            .body(result);
    }

    /**
     * GET  /salles : get all the salles.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of salles in body
     */
    @GetMapping("/salles")
    @Timed
    public ResponseEntity<List<Salle>> getAllSalles(Pageable pageable) {
        log.debug("REST request to get a page of Salles");
        Page<Salle> page = salleService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/salles");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /salles/:id : get the "id" salle.
     *
     * @param id the id of the salle to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the salle, or with status 404 (Not Found)
     */
    @GetMapping("/salles/{id}")
    @Timed
    public ResponseEntity<Salle> getSalle(@PathVariable Long id) {
        log.debug("REST request to get Salle : {}", id);
        Salle salle = salleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(salle));
    }

    /**
     * DELETE  /salles/:id : delete the "id" salle.
     *
     * @param id the id of the salle to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/salles/{id}")
    @Timed
    public ResponseEntity<Void> deleteSalle(@PathVariable Long id) {
        log.debug("REST request to delete Salle : {}", id);
        salleService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/salles?query=:query : search for the salle corresponding
     * to the query.
     *
     * @param query the query of the salle search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/salles")
    @Timed
    public ResponseEntity<List<Salle>> searchSalles(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Salles for query {}", query);
        Page<Salle> page = salleService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/salles");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
