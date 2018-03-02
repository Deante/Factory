package factory.web.rest;

import factory.FactoryApp;

import factory.domain.Formation;
import factory.repository.FormationRepository;
import factory.service.FormationService;
import factory.repository.search.FormationSearchRepository;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static factory.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the FormationResource REST controller.
 *
 * @see FormationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FactoryApp.class)
public class FormationResourceIntTest {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_DEBUT_FORM = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DEBUT_FORM = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_FIN_FORM = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_FIN_FORM = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_OBJECTIFS = "AAAAAAAAAA";
    private static final String UPDATED_OBJECTIFS = "BBBBBBBBBB";

    @Autowired
    private FormationRepository formationRepository;

    @Autowired
    private FormationService formationService;

    @Autowired
    private FormationSearchRepository formationSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restFormationMockMvc;

    private Formation formation;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FormationResource formationResource = new FormationResource(formationService);
        this.restFormationMockMvc = MockMvcBuilders.standaloneSetup(formationResource)
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
    public static Formation createEntity(EntityManager em) {
        Formation formation = new Formation()
            .nom(DEFAULT_NOM)
            .dateDebutForm(DEFAULT_DATE_DEBUT_FORM)
            .dateFinForm(DEFAULT_DATE_FIN_FORM)
            .objectifs(DEFAULT_OBJECTIFS);
        return formation;
    }

    @Before
    public void initTest() {
        formationSearchRepository.deleteAll();
        formation = createEntity(em);
    }

    @Test
    @Transactional
    public void createFormation() throws Exception {
        int databaseSizeBeforeCreate = formationRepository.findAll().size();

        // Create the Formation
        restFormationMockMvc.perform(post("/api/formations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(formation)))
            .andExpect(status().isCreated());

        // Validate the Formation in the database
        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeCreate + 1);
        Formation testFormation = formationList.get(formationList.size() - 1);
        assertThat(testFormation.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testFormation.getDateDebutForm()).isEqualTo(DEFAULT_DATE_DEBUT_FORM);
        assertThat(testFormation.getDateFinForm()).isEqualTo(DEFAULT_DATE_FIN_FORM);
        assertThat(testFormation.getObjectifs()).isEqualTo(DEFAULT_OBJECTIFS);

        // Validate the Formation in Elasticsearch
        Formation formationEs = formationSearchRepository.findOne(testFormation.getId());
        assertThat(formationEs).isEqualToIgnoringGivenFields(testFormation);
    }

    @Test
    @Transactional
    public void createFormationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = formationRepository.findAll().size();

        // Create the Formation with an existing ID
        formation.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFormationMockMvc.perform(post("/api/formations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(formation)))
            .andExpect(status().isBadRequest());

        // Validate the Formation in the database
        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = formationRepository.findAll().size();
        // set the field null
        formation.setNom(null);

        // Create the Formation, which fails.

        restFormationMockMvc.perform(post("/api/formations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(formation)))
            .andExpect(status().isBadRequest());

        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateDebutFormIsRequired() throws Exception {
        int databaseSizeBeforeTest = formationRepository.findAll().size();
        // set the field null
        formation.setDateDebutForm(null);

        // Create the Formation, which fails.

        restFormationMockMvc.perform(post("/api/formations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(formation)))
            .andExpect(status().isBadRequest());

        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateFinFormIsRequired() throws Exception {
        int databaseSizeBeforeTest = formationRepository.findAll().size();
        // set the field null
        formation.setDateFinForm(null);

        // Create the Formation, which fails.

        restFormationMockMvc.perform(post("/api/formations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(formation)))
            .andExpect(status().isBadRequest());

        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFormations() throws Exception {
        // Initialize the database
        formationRepository.saveAndFlush(formation);

        // Get all the formationList
        restFormationMockMvc.perform(get("/api/formations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(formation.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
            .andExpect(jsonPath("$.[*].dateDebutForm").value(hasItem(DEFAULT_DATE_DEBUT_FORM.toString())))
            .andExpect(jsonPath("$.[*].dateFinForm").value(hasItem(DEFAULT_DATE_FIN_FORM.toString())))
            .andExpect(jsonPath("$.[*].objectifs").value(hasItem(DEFAULT_OBJECTIFS.toString())));
    }

    @Test
    @Transactional
    public void getFormation() throws Exception {
        // Initialize the database
        formationRepository.saveAndFlush(formation);

        // Get the formation
        restFormationMockMvc.perform(get("/api/formations/{id}", formation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(formation.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM.toString()))
            .andExpect(jsonPath("$.dateDebutForm").value(DEFAULT_DATE_DEBUT_FORM.toString()))
            .andExpect(jsonPath("$.dateFinForm").value(DEFAULT_DATE_FIN_FORM.toString()))
            .andExpect(jsonPath("$.objectifs").value(DEFAULT_OBJECTIFS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingFormation() throws Exception {
        // Get the formation
        restFormationMockMvc.perform(get("/api/formations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFormation() throws Exception {
        // Initialize the database
        formationService.save(formation);

        int databaseSizeBeforeUpdate = formationRepository.findAll().size();

        // Update the formation
        Formation updatedFormation = formationRepository.findOne(formation.getId());
        // Disconnect from session so that the updates on updatedFormation are not directly saved in db
        em.detach(updatedFormation);
        updatedFormation
            .nom(UPDATED_NOM)
            .dateDebutForm(UPDATED_DATE_DEBUT_FORM)
            .dateFinForm(UPDATED_DATE_FIN_FORM)
            .objectifs(UPDATED_OBJECTIFS);

        restFormationMockMvc.perform(put("/api/formations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedFormation)))
            .andExpect(status().isOk());

        // Validate the Formation in the database
        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeUpdate);
        Formation testFormation = formationList.get(formationList.size() - 1);
        assertThat(testFormation.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testFormation.getDateDebutForm()).isEqualTo(UPDATED_DATE_DEBUT_FORM);
        assertThat(testFormation.getDateFinForm()).isEqualTo(UPDATED_DATE_FIN_FORM);
        assertThat(testFormation.getObjectifs()).isEqualTo(UPDATED_OBJECTIFS);

        // Validate the Formation in Elasticsearch
        Formation formationEs = formationSearchRepository.findOne(testFormation.getId());
        assertThat(formationEs).isEqualToIgnoringGivenFields(testFormation);
    }

    @Test
    @Transactional
    public void updateNonExistingFormation() throws Exception {
        int databaseSizeBeforeUpdate = formationRepository.findAll().size();

        // Create the Formation

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restFormationMockMvc.perform(put("/api/formations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(formation)))
            .andExpect(status().isCreated());

        // Validate the Formation in the database
        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteFormation() throws Exception {
        // Initialize the database
        formationService.save(formation);

        int databaseSizeBeforeDelete = formationRepository.findAll().size();

        // Get the formation
        restFormationMockMvc.perform(delete("/api/formations/{id}", formation.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean formationExistsInEs = formationSearchRepository.exists(formation.getId());
        assertThat(formationExistsInEs).isFalse();

        // Validate the database is empty
        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchFormation() throws Exception {
        // Initialize the database
        formationService.save(formation);

        // Search the formation
        restFormationMockMvc.perform(get("/api/_search/formations?query=id:" + formation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(formation.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
            .andExpect(jsonPath("$.[*].dateDebutForm").value(hasItem(DEFAULT_DATE_DEBUT_FORM.toString())))
            .andExpect(jsonPath("$.[*].dateFinForm").value(hasItem(DEFAULT_DATE_FIN_FORM.toString())))
            .andExpect(jsonPath("$.[*].objectifs").value(hasItem(DEFAULT_OBJECTIFS.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Formation.class);
        Formation formation1 = new Formation();
        formation1.setId(1L);
        Formation formation2 = new Formation();
        formation2.setId(formation1.getId());
        assertThat(formation1).isEqualTo(formation2);
        formation2.setId(2L);
        assertThat(formation1).isNotEqualTo(formation2);
        formation1.setId(null);
        assertThat(formation1).isNotEqualTo(formation2);
    }
}
