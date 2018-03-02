package factory.web.rest;

import com.codahale.metrics.annotation.Timed;
import factory.domain.Ordinateur;
import factory.service.OrdinateurService;
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
 * REST controller for managing Ordinateur.
 */
@RestController
@RequestMapping("/api")
public class OrdinateurResource {

    private final Logger log = LoggerFactory.getLogger(OrdinateurResource.class);

    private static final String ENTITY_NAME = "ordinateur";

    private final OrdinateurService ordinateurService;

    public OrdinateurResource(OrdinateurService ordinateurService) {
        this.ordinateurService = ordinateurService;
    }

    /**
     * POST  /ordinateurs : Create a new ordinateur.
     *
     * @param ordinateur the ordinateur to create
     * @return the ResponseEntity with status 201 (Created) and with body the new ordinateur, or with status 400 (Bad Request) if the ordinateur has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/ordinateurs")
    @Timed
    public ResponseEntity<Ordinateur> createOrdinateur(@Valid @RequestBody Ordinateur ordinateur) throws URISyntaxException {
        log.debug("REST request to save Ordinateur : {}", ordinateur);
        if (ordinateur.getId() != null) {
            throw new BadRequestAlertException("A new ordinateur cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Ordinateur result = ordinateurService.save(ordinateur);
        return ResponseEntity.created(new URI("/api/ordinateurs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /ordinateurs : Updates an existing ordinateur.
     *
     * @param ordinateur the ordinateur to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated ordinateur,
     * or with status 400 (Bad Request) if the ordinateur is not valid,
     * or with status 500 (Internal Server Error) if the ordinateur couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/ordinateurs")
    @Timed
    public ResponseEntity<Ordinateur> updateOrdinateur(@Valid @RequestBody Ordinateur ordinateur) throws URISyntaxException {
        log.debug("REST request to update Ordinateur : {}", ordinateur);
        if (ordinateur.getId() == null) {
            return createOrdinateur(ordinateur);
        }
        Ordinateur result = ordinateurService.save(ordinateur);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, ordinateur.getId().toString()))
            .body(result);
    }

    /**
     * GET  /ordinateurs : get all the ordinateurs.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of ordinateurs in body
     */
    @GetMapping("/ordinateurs")
    @Timed
    public ResponseEntity<List<Ordinateur>> getAllOrdinateurs(Pageable pageable) {
        log.debug("REST request to get a page of Ordinateurs");
        Page<Ordinateur> page = ordinateurService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/ordinateurs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /ordinateurs/:id : get the "id" ordinateur.
     *
     * @param id the id of the ordinateur to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the ordinateur, or with status 404 (Not Found)
     */
    @GetMapping("/ordinateurs/{id}")
    @Timed
    public ResponseEntity<Ordinateur> getOrdinateur(@PathVariable Long id) {
        log.debug("REST request to get Ordinateur : {}", id);
        Ordinateur ordinateur = ordinateurService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(ordinateur));
    }

    /**
     * DELETE  /ordinateurs/:id : delete the "id" ordinateur.
     *
     * @param id the id of the ordinateur to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/ordinateurs/{id}")
    @Timed
    public ResponseEntity<Void> deleteOrdinateur(@PathVariable Long id) {
        log.debug("REST request to delete Ordinateur : {}", id);
        ordinateurService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/ordinateurs?query=:query : search for the ordinateur corresponding
     * to the query.
     *
     * @param query the query of the ordinateur search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/ordinateurs")
    @Timed
    public ResponseEntity<List<Ordinateur>> searchOrdinateurs(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Ordinateurs for query {}", query);
        Page<Ordinateur> page = ordinateurService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/ordinateurs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
