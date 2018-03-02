package factory.web.rest;

import factory.FactoryApp;

import factory.domain.Gestionnaire;
import factory.repository.GestionnaireRepository;
import factory.service.GestionnaireService;
import factory.repository.search.GestionnaireSearchRepository;
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
 * Test class for the GestionnaireResource REST controller.
 *
 * @see GestionnaireResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FactoryApp.class)
public class GestionnaireResourceIntTest {

    @Autowired
    private GestionnaireRepository gestionnaireRepository;

    @Autowired
    private GestionnaireService gestionnaireService;

    @Autowired
    private GestionnaireSearchRepository gestionnaireSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restGestionnaireMockMvc;

    private Gestionnaire gestionnaire;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final GestionnaireResource gestionnaireResource = new GestionnaireResource(gestionnaireService);
        this.restGestionnaireMockMvc = MockMvcBuilders.standaloneSetup(gestionnaireResource)
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
    public static Gestionnaire createEntity(EntityManager em) {
        Gestionnaire gestionnaire = new Gestionnaire();
        return gestionnaire;
    }

    @Before
    public void initTest() {
        gestionnaireSearchRepository.deleteAll();
        gestionnaire = createEntity(em);
    }

    @Test
    @Transactional
    public void createGestionnaire() throws Exception {
        int databaseSizeBeforeCreate = gestionnaireRepository.findAll().size();

        // Create the Gestionnaire
        restGestionnaireMockMvc.perform(post("/api/gestionnaires")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(gestionnaire)))
            .andExpect(status().isCreated());

        // Validate the Gestionnaire in the database
        List<Gestionnaire> gestionnaireList = gestionnaireRepository.findAll();
        assertThat(gestionnaireList).hasSize(databaseSizeBeforeCreate + 1);
        Gestionnaire testGestionnaire = gestionnaireList.get(gestionnaireList.size() - 1);

        // Validate the Gestionnaire in Elasticsearch
        Gestionnaire gestionnaireEs = gestionnaireSearchRepository.findOne(testGestionnaire.getId());
        assertThat(gestionnaireEs).isEqualToIgnoringGivenFields(testGestionnaire);
    }

    @Test
    @Transactional
    public void createGestionnaireWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = gestionnaireRepository.findAll().size();

        // Create the Gestionnaire with an existing ID
        gestionnaire.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restGestionnaireMockMvc.perform(post("/api/gestionnaires")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(gestionnaire)))
            .andExpect(status().isBadRequest());

        // Validate the Gestionnaire in the database
        List<Gestionnaire> gestionnaireList = gestionnaireRepository.findAll();
        assertThat(gestionnaireList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllGestionnaires() throws Exception {
        // Initialize the database
        gestionnaireRepository.saveAndFlush(gestionnaire);

        // Get all the gestionnaireList
        restGestionnaireMockMvc.perform(get("/api/gestionnaires?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gestionnaire.getId().intValue())));
    }

    @Test
    @Transactional
    public void getGestionnaire() throws Exception {
        // Initialize the database
        gestionnaireRepository.saveAndFlush(gestionnaire);

        // Get the gestionnaire
        restGestionnaireMockMvc.perform(get("/api/gestionnaires/{id}", gestionnaire.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(gestionnaire.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingGestionnaire() throws Exception {
        // Get the gestionnaire
        restGestionnaireMockMvc.perform(get("/api/gestionnaires/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGestionnaire() throws Exception {
        // Initialize the database
        gestionnaireService.save(gestionnaire);

        int databaseSizeBeforeUpdate = gestionnaireRepository.findAll().size();

        // Update the gestionnaire
        Gestionnaire updatedGestionnaire = gestionnaireRepository.findOne(gestionnaire.getId());
        // Disconnect from session so that the updates on updatedGestionnaire are not directly saved in db
        em.detach(updatedGestionnaire);

        restGestionnaireMockMvc.perform(put("/api/gestionnaires")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedGestionnaire)))
            .andExpect(status().isOk());

        // Validate the Gestionnaire in the database
        List<Gestionnaire> gestionnaireList = gestionnaireRepository.findAll();
        assertThat(gestionnaireList).hasSize(databaseSizeBeforeUpdate);
        Gestionnaire testGestionnaire = gestionnaireList.get(gestionnaireList.size() - 1);

        // Validate the Gestionnaire in Elasticsearch
        Gestionnaire gestionnaireEs = gestionnaireSearchRepository.findOne(testGestionnaire.getId());
        assertThat(gestionnaireEs).isEqualToIgnoringGivenFields(testGestionnaire);
    }

    @Test
    @Transactional
    public void updateNonExistingGestionnaire() throws Exception {
        int databaseSizeBeforeUpdate = gestionnaireRepository.findAll().size();

        // Create the Gestionnaire

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restGestionnaireMockMvc.perform(put("/api/gestionnaires")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(gestionnaire)))
            .andExpect(status().isCreated());

        // Validate the Gestionnaire in the database
        List<Gestionnaire> gestionnaireList = gestionnaireRepository.findAll();
        assertThat(gestionnaireList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteGestionnaire() throws Exception {
        // Initialize the database
        gestionnaireService.save(gestionnaire);

        int databaseSizeBeforeDelete = gestionnaireRepository.findAll().size();

        // Get the gestionnaire
        restGestionnaireMockMvc.perform(delete("/api/gestionnaires/{id}", gestionnaire.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean gestionnaireExistsInEs = gestionnaireSearchRepository.exists(gestionnaire.getId());
        assertThat(gestionnaireExistsInEs).isFalse();

        // Validate the database is empty
        List<Gestionnaire> gestionnaireList = gestionnaireRepository.findAll();
        assertThat(gestionnaireList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchGestionnaire() throws Exception {
        // Initialize the database
        gestionnaireService.save(gestionnaire);

        // Search the gestionnaire
        restGestionnaireMockMvc.perform(get("/api/_search/gestionnaires?query=id:" + gestionnaire.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gestionnaire.getId().intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Gestionnaire.class);
        Gestionnaire gestionnaire1 = new Gestionnaire();
        gestionnaire1.setId(1L);
        Gestionnaire gestionnaire2 = new Gestionnaire();
        gestionnaire2.setId(gestionnaire1.getId());
        assertThat(gestionnaire1).isEqualTo(gestionnaire2);
        gestionnaire2.setId(2L);
        assertThat(gestionnaire1).isNotEqualTo(gestionnaire2);
        gestionnaire1.setId(null);
        assertThat(gestionnaire1).isNotEqualTo(gestionnaire2);
    }
}
