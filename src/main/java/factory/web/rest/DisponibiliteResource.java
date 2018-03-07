package factory.web.rest;

import com.codahale.metrics.annotation.Timed;
import factory.domain.Disponibilite;
import factory.service.DisponibiliteService;
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
 * REST controller for managing Disponibilite.
 */
@RestController
@RequestMapping("/api")
public class DisponibiliteResource {

    private final Logger log = LoggerFactory.getLogger(DisponibiliteResource.class);

    private static final String ENTITY_NAME = "disponibilite";

    private final DisponibiliteService disponibiliteService;

    public DisponibiliteResource(DisponibiliteService disponibiliteService) {
        this.disponibiliteService = disponibiliteService;
    }

    /**
     * POST  /disponibilites : Create a new disponibilite.
     *
     * @param disponibilite the disponibilite to create
     * @return the ResponseEntity with status 201 (Created) and with body the new disponibilite, or with status 400 (Bad Request) if the disponibilite has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/disponibilites")
    @Timed
    public ResponseEntity<Disponibilite> createDisponibilite(@Valid @RequestBody Disponibilite disponibilite) throws URISyntaxException {
        log.debug("REST request to save Disponibilite : {}", disponibilite);
        if (disponibilite.getId() != null) {
            throw new BadRequestAlertException("A new disponibilite cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Disponibilite result = disponibiliteService.save(disponibilite);
        return ResponseEntity.created(new URI("/api/disponibilites/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /disponibilites : Updates an existing disponibilite.
     *
     * @param disponibilite the disponibilite to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated disponibilite,
     * or with status 400 (Bad Request) if the disponibilite is not valid,
     * or with status 500 (Internal Server Error) if the disponibilite couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/disponibilites")
    @Timed
    public ResponseEntity<Disponibilite> updateDisponibilite(@Valid @RequestBody Disponibilite disponibilite) throws URISyntaxException {
        log.debug("REST request to update Disponibilite : {}", disponibilite);
        if (disponibilite.getId() == null) {
            return createDisponibilite(disponibilite);
        }
        Disponibilite result = disponibiliteService.save(disponibilite);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, disponibilite.getId().toString()))
            .body(result);
    }

    /**
     * GET  /disponibilites : get all the disponibilites.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of disponibilites in body
     */
    @GetMapping("/disponibilites")
    @Timed
    public ResponseEntity<List<Disponibilite>> getAllDisponibilites(Pageable pageable) {
        log.debug("REST request to get a page of Disponibilites");
        Page<Disponibilite> page = disponibiliteService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/disponibilites");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /disponibilites/:id : get the "id" disponibilite.
     *
     * @param id the id of the disponibilite to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the disponibilite, or with status 404 (Not Found)
     */
    @GetMapping("/disponibilites/{id}")
    @Timed
    public ResponseEntity<Disponibilite> getDisponibilite(@PathVariable Long id) {
        log.debug("REST request to get Disponibilite : {}", id);
        Disponibilite disponibilite = disponibiliteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(disponibilite));
    }

    /**
     * DELETE  /disponibilites/:id : delete the "id" disponibilite.
     *
     * @param id the id of the disponibilite to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/disponibilites/{id}")
    @Timed
    public ResponseEntity<Void> deleteDisponibilite(@PathVariable Long id) {
        log.debug("REST request to delete Disponibilite : {}", id);
        disponibiliteService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/disponibilites?query=:query : search for the disponibilite corresponding
     * to the query.
     *
     * @param query the query of the disponibilite search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/disponibilites")
    @Timed
    public ResponseEntity<List<Disponibilite>> searchDisponibilites(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Disponibilites for query {}", query);
        Page<Disponibilite> page = disponibiliteService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/disponibilites");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
