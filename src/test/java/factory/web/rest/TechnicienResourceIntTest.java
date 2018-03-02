package factory.web.rest;

import factory.FactoryApp;

import factory.domain.Technicien;
import factory.repository.TechnicienRepository;
import factory.service.TechnicienService;
import factory.repository.search.TechnicienSearchRepository;
import factory.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static factory.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the TechnicienResource REST controller.
 *
 * @see TechnicienResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FactoryApp.class)
public class TechnicienResourceIntTest {

    @Autowired
    private TechnicienRepository technicienRepository;

    @Autowired
    private TechnicienService technicienService;

    @Autowired
    private TechnicienSearchRepository technicienSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTechnicienMockMvc;

    private Technicien technicien;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TechnicienResource technicienResource = new TechnicienResource(technicienService);
        this.restTechnicienMockMvc = MockMvcBuilders.standaloneSetup(technicienResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Technicien createEntity(EntityManager em) {
        Technicien technicien = new Technicien();
        return technicien;
    }

    @Before
    public void initTest() {
        technicienSearchRepository.deleteAll();
        technicien = createEntity(em);
    }

    @Test
    @Transactional
    public void createTechnicien() throws Exception {
        int databaseSizeBeforeCreate = technicienRepository.findAll().size();

        // Create the Technicien
        restTechnicienMockMvc.perform(post("/api/techniciens")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(technicien)))
            .andExpect(status().isCreated());

        // Validate the Technicien in the database
        List<Technicien> technicienList = technicienRepository.findAll();
        assertThat(technicienList).hasSize(databaseSizeBeforeCreate + 1);
        Technicien testTechnicien = technicienList.get(technicienList.size() - 1);

        // Validate the Technicien in Elasticsearch
        Technicien technicienEs = technicienSearchRepository.findOne(testTechnicien.getId());
        assertThat(technicienEs).isEqualToIgnoringGivenFields(testTechnicien);
    }

    @Test
    @Transactional
    public void createTechnicienWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = technicienRepository.findAll().size();

        // Create the Technicien with an existing ID
        technicien.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTechnicienMockMvc.perform(post("/api/techniciens")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(technicien)))
            .andExpect(status().isBadRequest());

        // Validate the Technicien in the database
        List<Technicien> technicienList = technicienRepository.findAll();
        assertThat(technicienList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllTechniciens() throws Exception {
        // Initialize the database
        technicienRepository.saveAndFlush(technicien);

        // Get all the technicienList
        restTechnicienMockMvc.perform(get("/api/techniciens?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(technicien.getId().intValue())));
    }

    @Test
    @Transactional
    public void getTechnicien() throws Exception {
        // Initialize the database
        technicienRepository.saveAndFlush(technicien);

        // Get the technicien
        restTechnicienMockMvc.perform(get("/api/techniciens/{id}", technicien.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(technicien.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingTechnicien() throws Exception {
        // Get the technicien
        restTechnicienMockMvc.perform(get("/api/techniciens/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTechnicien() throws Exception {
        // Initialize the database
        technicienService.save(technicien);

        int databaseSizeBeforeUpdate = technicienRepository.findAll().size();

        // Update the technicien
        Technicien updatedTechnicien = technicienRepository.findOne(technicien.getId());
        // Disconnect from session so that the updates on updatedTechnicien are not directly saved in db
        em.detach(updatedTechnicien);

        restTechnicienMockMvc.perform(put("/api/techniciens")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTechnicien)))
            .andExpect(status().isOk());

        // Validate the Technicien in the database
        List<Technicien> technicienList = technicienRepository.findAll();
        assertThat(technicienList).hasSize(databaseSizeBeforeUpdate);
        Technicien testTechnicien = technicienList.get(technicienList.size() - 1);

        // Validate the Technicien in Elasticsearch
        Technicien technicienEs = technicienSearchRepository.findOne(testTechnicien.getId());
        assertThat(technicienEs).isEqualToIgnoringGivenFields(testTechnicien);
    }

    @Test
    @Transactional
    public void updateNonExistingTechnicien() throws Exception {
        int databaseSizeBeforeUpdate = technicienRepository.findAll().size();

        // Create the Technicien

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTechnicienMockMvc.perform(put("/api/techniciens")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(technicien)))
            .andExpect(status().isCreated());

        // Validate the Technicien in the database
        List<Technicien> technicienList = technicienRepository.findAll();
        assertThat(technicienList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTechnicien() throws Exception {
        // Initialize the database
        technicienService.save(technicien);

        int databaseSizeBeforeDelete = technicienRepository.findAll().size();

        // Get the technicien
        restTechnicienMockMvc.perform(delete("/api/techniciens/{id}", technicien.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean technicienExistsInEs = technicienSearchRepository.exists(technicien.getId());
        assertThat(technicienExistsInEs).isFalse();

        // Validate the database is empty
        List<Technicien> technicienList = technicienRepository.findAll();
        assertThat(technicienList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTechnicien() throws Exception {
        // Initialize the database
        technicienService.save(technicien);

        // Search the technicien
        restTechnicienMockMvc.perform(get("/api/_search/techniciens?query=id:" + technicien.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(technicien.getId().intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Technicien.class);
        Technicien technicien1 = new Technicien();
        technicien1.setId(1L);
        Technicien technicien2 = new Technicien();
        technicien2.setId(technicien1.getId());
        assertThat(technicien1).isEqualTo(technicien2);
        technicien2.setId(2L);
        assertThat(technicien1).isNotEqualTo(technicien2);
        technicien1.setId(null);
        assertThat(technicien1).isNotEqualTo(technicien2);
    }
}
