package factory.web.rest;

import factory.FactoryApp;

import factory.domain.Stagiaire;
import factory.repository.StagiaireRepository;
import factory.service.StagiaireService;
import factory.repository.search.StagiaireSearchRepository;
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

import factory.domain.enumeration.NiveauEnum;
/**
 * Test class for the StagiaireResource REST controller.
 *
 * @see StagiaireResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FactoryApp.class)
public class StagiaireResourceIntTest {

    private static final NiveauEnum DEFAULT_NIVEAU = NiveauEnum.DEBUTANT;
    private static final NiveauEnum UPDATED_NIVEAU = NiveauEnum.INTERMEDIAIRE;

    @Autowired
    private StagiaireRepository stagiaireRepository;

    @Autowired
    private StagiaireService stagiaireService;

    @Autowired
    private StagiaireSearchRepository stagiaireSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restStagiaireMockMvc;

    private Stagiaire stagiaire;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final StagiaireResource stagiaireResource = new StagiaireResource(stagiaireService);
        this.restStagiaireMockMvc = MockMvcBuilders.standaloneSetup(stagiaireResource)
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
    public static Stagiaire createEntity(EntityManager em) {
        Stagiaire stagiaire = new Stagiaire()
            .niveau(DEFAULT_NIVEAU);
        return stagiaire;
    }

    @Before
    public void initTest() {
        stagiaireSearchRepository.deleteAll();
        stagiaire = createEntity(em);
    }

    @Test
    @Transactional
    public void createStagiaire() throws Exception {
        int databaseSizeBeforeCreate = stagiaireRepository.findAll().size();

        // Create the Stagiaire
        restStagiaireMockMvc.perform(post("/api/stagiaires")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stagiaire)))
            .andExpect(status().isCreated());

        // Validate the Stagiaire in the database
        List<Stagiaire> stagiaireList = stagiaireRepository.findAll();
        assertThat(stagiaireList).hasSize(databaseSizeBeforeCreate + 1);
        Stagiaire testStagiaire = stagiaireList.get(stagiaireList.size() - 1);
        assertThat(testStagiaire.getNiveau()).isEqualTo(DEFAULT_NIVEAU);

        // Validate the Stagiaire in Elasticsearch
        Stagiaire stagiaireEs = stagiaireSearchRepository.findOne(testStagiaire.getId());
        assertThat(stagiaireEs).isEqualToIgnoringGivenFields(testStagiaire);
    }

    @Test
    @Transactional
    public void createStagiaireWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = stagiaireRepository.findAll().size();

        // Create the Stagiaire with an existing ID
        stagiaire.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStagiaireMockMvc.perform(post("/api/stagiaires")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stagiaire)))
            .andExpect(status().isBadRequest());

        // Validate the Stagiaire in the database
        List<Stagiaire> stagiaireList = stagiaireRepository.findAll();
        assertThat(stagiaireList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllStagiaires() throws Exception {
        // Initialize the database
        stagiaireRepository.saveAndFlush(stagiaire);

        // Get all the stagiaireList
        restStagiaireMockMvc.perform(get("/api/stagiaires?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stagiaire.getId().intValue())))
            .andExpect(jsonPath("$.[*].niveau").value(hasItem(DEFAULT_NIVEAU.toString())));
    }

    @Test
    @Transactional
    public void getStagiaire() throws Exception {
        // Initialize the database
        stagiaireRepository.saveAndFlush(stagiaire);

        // Get the stagiaire
        restStagiaireMockMvc.perform(get("/api/stagiaires/{id}", stagiaire.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(stagiaire.getId().intValue()))
            .andExpect(jsonPath("$.niveau").value(DEFAULT_NIVEAU.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingStagiaire() throws Exception {
        // Get the stagiaire
        restStagiaireMockMvc.perform(get("/api/stagiaires/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStagiaire() throws Exception {
        // Initialize the database
        stagiaireService.save(stagiaire);

        int databaseSizeBeforeUpdate = stagiaireRepository.findAll().size();

        // Update the stagiaire
        Stagiaire updatedStagiaire = stagiaireRepository.findOne(stagiaire.getId());
        // Disconnect from session so that the updates on updatedStagiaire are not directly saved in db
        em.detach(updatedStagiaire);
        updatedStagiaire
            .niveau(UPDATED_NIVEAU);

        restStagiaireMockMvc.perform(put("/api/stagiaires")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedStagiaire)))
            .andExpect(status().isOk());

        // Validate the Stagiaire in the database
        List<Stagiaire> stagiaireList = stagiaireRepository.findAll();
        assertThat(stagiaireList).hasSize(databaseSizeBeforeUpdate);
        Stagiaire testStagiaire = stagiaireList.get(stagiaireList.size() - 1);
        assertThat(testStagiaire.getNiveau()).isEqualTo(UPDATED_NIVEAU);

        // Validate the Stagiaire in Elasticsearch
        Stagiaire stagiaireEs = stagiaireSearchRepository.findOne(testStagiaire.getId());
        assertThat(stagiaireEs).isEqualToIgnoringGivenFields(testStagiaire);
    }

    @Test
    @Transactional
    public void updateNonExistingStagiaire() throws Exception {
        int databaseSizeBeforeUpdate = stagiaireRepository.findAll().size();

        // Create the Stagiaire

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restStagiaireMockMvc.perform(put("/api/stagiaires")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stagiaire)))
            .andExpect(status().isCreated());

        // Validate the Stagiaire in the database
        List<Stagiaire> stagiaireList = stagiaireRepository.findAll();
        assertThat(stagiaireList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteStagiaire() throws Exception {
        // Initialize the database
        stagiaireService.save(stagiaire);

        int databaseSizeBeforeDelete = stagiaireRepository.findAll().size();

        // Get the stagiaire
        restStagiaireMockMvc.perform(delete("/api/stagiaires/{id}", stagiaire.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean stagiaireExistsInEs = stagiaireSearchRepository.exists(stagiaire.getId());
        assertThat(stagiaireExistsInEs).isFalse();

        // Validate the database is empty
        List<Stagiaire> stagiaireList = stagiaireRepository.findAll();
        assertThat(stagiaireList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchStagiaire() throws Exception {
        // Initialize the database
        stagiaireService.save(stagiaire);

        // Search the stagiaire
        restStagiaireMockMvc.perform(get("/api/_search/stagiaires?query=id:" + stagiaire.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stagiaire.getId().intValue())))
            .andExpect(jsonPath("$.[*].niveau").value(hasItem(DEFAULT_NIVEAU.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Stagiaire.class);
        Stagiaire stagiaire1 = new Stagiaire();
        stagiaire1.setId(1L);
        Stagiaire stagiaire2 = new Stagiaire();
        stagiaire2.setId(stagiaire1.getId());
        assertThat(stagiaire1).isEqualTo(stagiaire2);
        stagiaire2.setId(2L);
        assertThat(stagiaire1).isNotEqualTo(stagiaire2);
        stagiaire1.setId(null);
        assertThat(stagiaire1).isNotEqualTo(stagiaire2);
    }
}
