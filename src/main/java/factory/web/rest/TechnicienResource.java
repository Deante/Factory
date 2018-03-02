package factory.web.rest;

import com.codahale.metrics.annotation.Timed;
import factory.domain.Technicien;
import factory.service.TechnicienService;
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
 * REST controller for managing Technicien.
 */
@RestController
@RequestMapping("/api")
public class TechnicienResource {

    private final Logger log = LoggerFactory.getLogger(TechnicienResource.class);

    private static final String ENTITY_NAME = "technicien";

    private final TechnicienService technicienService;

    public TechnicienResource(TechnicienService technicienService) {
        this.technicienService = technicienService;
    }

    /**
     * POST  /techniciens : Create a new technicien.
     *
     * @param technicien the technicien to create
     * @return the ResponseEntity with status 201 (Created) and with body the new technicien, or with status 400 (Bad Request) if the technicien has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/techniciens")
    @Timed
    public ResponseEntity<Technicien> createTechnicien(@RequestBody Technicien technicien) throws URISyntaxException {
        log.debug("REST request to save Technicien : {}", technicien);
        if (technicien.getId() != null) {
            throw new BadRequestAlertException("A new technicien cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Technicien result = technicienService.save(technicien);
        return ResponseEntity.created(new URI("/api/techniciens/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /techniciens : Updates an existing technicien.
     *
     * @param technicien the technicien to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated technicien,
     * or with status 400 (Bad Request) if the technicien is not valid,
     * or with status 500 (Internal Server Error) if the technicien couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/techniciens")
    @Timed
    public ResponseEntity<Technicien> updateTechnicien(@RequestBody Technicien technicien) throws URISyntaxException {
        log.debug("REST request to update Technicien : {}", technicien);
        if (technicien.getId() == null) {
            return createTechnicien(technicien);
        }
        Technicien result = technicienService.save(technicien);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, technicien.getId().toString()))
            .body(result);
    }

    /**
     * GET  /techniciens : get all the techniciens.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of techniciens in body
     */
    @GetMapping("/techniciens")
    @Timed
    public ResponseEntity<List<Technicien>> getAllTechniciens(Pageable pageable) {
        log.debug("REST request to get a page of Techniciens");
        Page<Technicien> page = technicienService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/techniciens");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /techniciens/:id : get the "id" technicien.
     *
     * @param id the id of the technicien to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the technicien, or with status 404 (Not Found)
     */
    @GetMapping("/techniciens/{id}")
    @Timed
    public ResponseEntity<Technicien> getTechnicien(@PathVariable Long id) {
        log.debug("REST request to get Technicien : {}", id);
        Technicien technicien = technicienService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(technicien));
    }

    /**
     * DELETE  /techniciens/:id : delete the "id" technicien.
     *
     * @param id the id of the technicien to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/techniciens/{id}")
    @Timed
    public ResponseEntity<Void> deleteTechnicien(@PathVariable Long id) {
        log.debug("REST request to delete Technicien : {}", id);
        technicienService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/techniciens?query=:query : search for the technicien corresponding
     * to the query.
     *
     * @param query the query of the technicien search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/techniciens")
    @Timed
    public ResponseEntity<List<Technicien>> searchTechniciens(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Techniciens for query {}", query);
        Page<Technicien> page = technicienService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/techniciens");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
