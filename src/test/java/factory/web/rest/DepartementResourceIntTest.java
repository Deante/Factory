package factory.web.rest;

import factory.FactoryApp;

import factory.domain.Departement;
import factory.repository.DepartementRepository;
import factory.service.DepartementService;
import factory.repository.search.DepartementSearchRepository;
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
 * Test class for the DepartementResource REST controller.
 *
 * @see DepartementResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FactoryApp.class)
public class DepartementResourceIntTest {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final Integer DEFAULT_ETAGES = 1;
    private static final Integer UPDATED_ETAGES = 2;

    @Autowired
    private DepartementRepository departementRepository;

    @Autowired
    private DepartementService departementService;

    @Autowired
    private DepartementSearchRepository departementSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDepartementMockMvc;

    private Departement departement;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DepartementResource departementResource = new DepartementResource(departementService);
        this.restDepartementMockMvc = MockMvcBuilders.standaloneSetup(departementResource)
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
    public static Departement createEntity(EntityManager em) {
        Departement departement = new Departement()
            .nom(DEFAULT_NOM)
            .etages(DEFAULT_ETAGES);
        return departement;
    }

    @Before
    public void initTest() {
        departementSearchRepository.deleteAll();
        departement = createEntity(em);
    }

    @Test
    @Transactional
    public void createDepartement() throws Exception {
        int databaseSizeBeforeCreate = departementRepository.findAll().size();

        // Create the Departement
        restDepartementMockMvc.perform(post("/api/departements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(departement)))
            .andExpect(status().isCreated());

        // Validate the Departement in the database
        List<Departement> departementList = departementRepository.findAll();
        assertThat(departementList).hasSize(databaseSizeBeforeCreate + 1);
        Departement testDepartement = departementList.get(departementList.size() - 1);
        assertThat(testDepartement.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testDepartement.getEtages()).isEqualTo(DEFAULT_ETAGES);

        // Validate the Departement in Elasticsearch
        Departement departementEs = departementSearchRepository.findOne(testDepartement.getId());
        assertThat(departementEs).isEqualToIgnoringGivenFields(testDepartement);
    }

    @Test
    @Transactional
    public void createDepartementWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = departementRepository.findAll().size();

        // Create the Departement with an existing ID
        departement.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDepartementMockMvc.perform(post("/api/departements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(departement)))
            .andExpect(status().isBadRequest());

        // Validate the Departement in the database
        List<Departement> departementList = departementRepository.findAll();
        assertThat(departementList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = departementRepository.findAll().size();
        // set the field null
        departement.setNom(null);

        // Create the Departement, which fails.

        restDepartementMockMvc.perform(post("/api/departements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(departement)))
            .andExpect(status().isBadRequest());

        List<Departement> departementList = departementRepository.findAll();
        assertThat(departementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDepartements() throws Exception {
        // Initialize the database
        departementRepository.saveAndFlush(departement);

        // Get all the departementList
        restDepartementMockMvc.perform(get("/api/departements?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(departement.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
            .andExpect(jsonPath("$.[*].etages").value(hasItem(DEFAULT_ETAGES)));
    }

    @Test
    @Transactional
    public void getDepartement() throws Exception {
        // Initialize the database
        departementRepository.saveAndFlush(departement);

        // Get the departement
        restDepartementMockMvc.perform(get("/api/departements/{id}", departement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(departement.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM.toString()))
            .andExpect(jsonPath("$.etages").value(DEFAULT_ETAGES));
    }

    @Test
    @Transactional
    public void getNonExistingDepartement() throws Exception {
        // Get the departement
        restDepartementMockMvc.perform(get("/api/departements/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDepartement() throws Exception {
        // Initialize the database
        departementService.save(departement);

        int databaseSizeBeforeUpdate = departementRepository.findAll().size();

        // Update the departement
        Departement updatedDepartement = departementRepository.findOne(departement.getId());
        // Disconnect from session so that the updates on updatedDepartement are not directly saved in db
        em.detach(updatedDepartement);
        updatedDepartement
            .nom(UPDATED_NOM)
            .etages(UPDATED_ETAGES);

        restDepartementMockMvc.perform(put("/api/departements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDepartement)))
            .andExpect(status().isOk());

        // Validate the Departement in the database
        List<Departement> departementList = departementRepository.findAll();
        assertThat(departementList).hasSize(databaseSizeBeforeUpdate);
        Departement testDepartement = departementList.get(departementList.size() - 1);
        assertThat(testDepartement.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testDepartement.getEtages()).isEqualTo(UPDATED_ETAGES);

        // Validate the Departement in Elasticsearch
        Departement departementEs = departementSearchRepository.findOne(testDepartement.getId());
        assertThat(departementEs).isEqualToIgnoringGivenFields(testDepartement);
    }

    @Test
    @Transactional
    public void updateNonExistingDepartement() throws Exception {
        int databaseSizeBeforeUpdate = departementRepository.findAll().size();

        // Create the Departement

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDepartementMockMvc.perform(put("/api/departements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(departement)))
            .andExpect(status().isCreated());

        // Validate the Departement in the database
        List<Departement> departementList = departementRepository.findAll();
        assertThat(departementList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteDepartement() throws Exception {
        // Initialize the database
        departementService.save(departement);

        int databaseSizeBeforeDelete = departementRepository.findAll().size();

        // Get the departement
        restDepartementMockMvc.perform(delete("/api/departements/{id}", departement.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean departementExistsInEs = departementSearchRepository.exists(departement.getId());
        assertThat(departementExistsInEs).isFalse();

        // Validate the database is empty
        List<Departement> departementList = departementRepository.findAll();
        assertThat(departementList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchDepartement() throws Exception {
        // Initialize the database
        departementService.save(departement);

        // Search the departement
        restDepartementMockMvc.perform(get("/api/_search/departements?query=id:" + departement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(departement.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
            .andExpect(jsonPath("$.[*].etages").value(hasItem(DEFAULT_ETAGES)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Departement.class);
        Departement departement1 = new Departement();
        departement1.setId(1L);
        Departement departement2 = new Departement();
        departement2.setId(departement1.getId());
        assertThat(departement1).isEqualTo(departement2);
        departement2.setId(2L);
        assertThat(departement1).isNotEqualTo(departement2);
        departement1.setId(null);
        assertThat(departement1).isNotEqualTo(departement2);
    }
}
