package factory.web.rest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;

import factory.domain.Formation;
import factory.service.FormationService;
import factory.web.rest.errors.BadRequestAlertException;
import factory.web.rest.util.HeaderUtil;
import factory.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;


/**
 * REST controller for managing Formation.
 */
@RestController
@RequestMapping("/api")
public class FormationResource {

	private final Logger log = LoggerFactory.getLogger(FormationResource.class);

	private static final String ENTITY_NAME = "formation";

	private final FormationService formationService;

	public FormationResource(FormationService formationService) {
		this.formationService = formationService;
	}

	/**
	 * POST /formations : Create a new formation.
	 *
	 * @param formation
	 *            the formation to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         formation, or with status 400 (Bad Request) if the formation has
	 *         already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/formations")
	@Timed
	public ResponseEntity<Formation> createFormation(@Valid @RequestBody Formation formation)
			throws URISyntaxException {
		log.debug("REST request to save Formation : {}", formation);
		if (formation.getId() != null) {
			throw new BadRequestAlertException("A new formation cannot already have an ID", ENTITY_NAME, "idexists");
		}
		Formation result = formationService.save(formation);
		return ResponseEntity.created(new URI("/api/formations/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
	}

	/**
	 * PUT /formations : Updates an existing formation.
	 *
	 * @param formation
	 *            the formation to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         formation, or with status 400 (Bad Request) if the formation is not
	 *         valid, or with status 500 (Internal Server Error) if the formation
	 *         couldn't be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/formations")
	@Timed
	public ResponseEntity<Formation> updateFormation(@Valid @RequestBody Formation formation)
			throws URISyntaxException {
		log.debug("REST request to update Formation : {}", formation);
		if (formation.getId() == null) {
			return createFormation(formation);
		}
		Formation result = formationService.save(formation);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, formation.getId().toString())).body(result);
	}

	/**
	 * GET /formations : get all the formations.
	 *
	 * @param pageable
	 *            the pagination information
	 * @return the ResponseEntity with status 200 (OK) and the list of formations in
	 *         body
	 */
	@GetMapping("/formations")
	@Timed
	public ResponseEntity<List<Formation>> getAllFormations(Pageable pageable) {
		log.debug("REST request to get a page of Formations");
		Page<Formation> page = formationService.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/formations");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET /formations/:id : get the "id" formation.
	 *
	 * @param id
	 *            the id of the formation to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the formation,
	 *         or with status 404 (Not Found)
	 */
	@GetMapping("/formations/{id}")
	@Timed
	public ResponseEntity<Formation> getFormation(@PathVariable Long id) {
		log.debug("REST request to get Formation : {}", id);
		Formation formation = formationService.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(formation));
	}

	/**
	 * DELETE /formations/:id : delete the "id" formation.
	 *
	 * @param id
	 *            the id of the formation to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/formations/{id}")
	@Timed
	public ResponseEntity<Void> deleteFormation(@PathVariable Long id) {
		log.debug("REST request to delete Formation : {}", id);
		formationService.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

	/**
	 * SEARCH /_search/formations?query=:query : search for the formation
	 * corresponding to the query.
	 *
	 * @param query
	 *            the query of the formation search
	 * @param pageable
	 *            the pagination information
	 * @return the result of the search
	 */
	@GetMapping("/_search/formations")
	@Timed
	public ResponseEntity<List<Formation>> searchFormations(@RequestParam String query, Pageable pageable) {
		log.debug("REST request to search for a page of Formations for query {}", query);
		Page<Formation> page = formationService.search(query, pageable);
		HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page,
				"/api/_search/formations");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET /formations/:id/pdf : get the pdf of the "id" formation.
	 *
	 * @param id
	 *            the id of the formation to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the pdf of
	 *         formation, or with status 404 (Not Found)
	 */
	@GetMapping("/formations/{id}/pdf")
	@Timed
	public ResponseEntity<byte[]> getFormationPdf(@PathVariable Long id) {
		log.debug("REST request to get pdf of Formation : {}", id);
		Formation formation = formationService.findOne(id);
		File file = null;

		try {
			file = formationService.createPdf(formation);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Path path = Paths.get(file.toURI());
		byte[] contents = null;
		try {
			contents = Files.readAllBytes(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.parseMediaType("application/pdf"));
	    String filename = "output.pdf";
	    headers.setContentDispositionFormData(filename, filename);
	    headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
	    ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(contents, headers, HttpStatus.OK);
	    
	    return response;
		
	}

}
