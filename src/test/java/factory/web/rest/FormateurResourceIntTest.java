package factory.web.rest;

import factory.FactoryApp;

import factory.domain.Formateur;
import factory.repository.FormateurRepository;
import factory.service.FormateurService;
import factory.repository.search.FormateurSearchRepository;
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
 * Test class for the FormateurResource REST controller.
 *
 * @see FormateurResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FactoryApp.class)
public class FormateurResourceIntTest {

    private static final LocalDate DEFAULT_DATE_DEBUT_DISPO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DEBUT_DISPO = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_FIN_DISPO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_FIN_DISPO = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private FormateurRepository formateurRepository;

    @Autowired
    private FormateurService formateurService;

    @Autowired
    private FormateurSearchRepository formateurSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restFormateurMockMvc;

    private Formateur formateur;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FormateurResource formateurResource = new FormateurResource(formateurService);
        this.restFormateurMockMvc = MockMvcBuilders.standaloneSetup(formateurResource)
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
    public static Formateur createEntity(EntityManager em) {
        Formateur formateur = new Formateur()
            .dateDebutDispo(DEFAULT_DATE_DEBUT_DISPO)
            .dateFinDispo(DEFAULT_DATE_FIN_DISPO);
        return formateur;
    }

    @Before
    public void initTest() {
        formateurSearchRepository.deleteAll();
        formateur = createEntity(em);
    }

    @Test
    @Transactional
    public void createFormateur() throws Exception {
        int databaseSizeBeforeCreate = formateurRepository.findAll().size();

        // Create the Formateur
        restFormateurMockMvc.perform(post("/api/formateurs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(formateur)))
            .andExpect(status().isCreated());

        // Validate the Formateur in the database
        List<Formateur> formateurList = formateurRepository.findAll();
        assertThat(formateurList).hasSize(databaseSizeBeforeCreate + 1);
        Formateur testFormateur = formateurList.get(formateurList.size() - 1);
        assertThat(testFormateur.getDateDebutDispo()).isEqualTo(DEFAULT_DATE_DEBUT_DISPO);
        assertThat(testFormateur.getDateFinDispo()).isEqualTo(DEFAULT_DATE_FIN_DISPO);

        // Validate the Formateur in Elasticsearch
        Formateur formateurEs = formateurSearchRepository.findOne(testFormateur.getId());
        assertThat(formateurEs).isEqualToIgnoringGivenFields(testFormateur);
    }

    @Test
    @Transactional
    public void createFormateurWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = formateurRepository.findAll().size();

        // Create the Formateur with an existing ID
        formateur.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFormateurMockMvc.perform(post("/api/formateurs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(formateur)))
            .andExpect(status().isBadRequest());

        // Validate the Formateur in the database
        List<Formateur> formateurList = formateurRepository.findAll();
        assertThat(formateurList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllFormateurs() throws Exception {
        // Initialize the database
        formateurRepository.saveAndFlush(formateur);

        // Get all the formateurList
        restFormateurMockMvc.perform(get("/api/formateurs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(formateur.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateDebutDispo").value(hasItem(DEFAULT_DATE_DEBUT_DISPO.toString())))
            .andExpect(jsonPath("$.[*].dateFinDispo").value(hasItem(DEFAULT_DATE_FIN_DISPO.toString())));
    }

    @Test
    @Transactional
    public void getFormateur() throws Exception {
        // Initialize the database
        formateurRepository.saveAndFlush(formateur);

        // Get the formateur
        restFormateurMockMvc.perform(get("/api/formateurs/{id}", formateur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(formateur.getId().intValue()))
            .andExpect(jsonPath("$.dateDebutDispo").value(DEFAULT_DATE_DEBUT_DISPO.toString()))
            .andExpect(jsonPath("$.dateFinDispo").value(DEFAULT_DATE_FIN_DISPO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingFormateur() throws Exception {
        // Get the formateur
        restFormateurMockMvc.perform(get("/api/formateurs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFormateur() throws Exception {
        // Initialize the database
        formateurService.save(formateur);

        int databaseSizeBeforeUpdate = formateurRepository.findAll().size();

        // Update the formateur
        Formateur updatedFormateur = formateurRepository.findOne(formateur.getId());
        // Disconnect from session so that the updates on updatedFormateur are not directly saved in db
        em.detach(updatedFormateur);
        updatedFormateur
            .dateDebutDispo(UPDATED_DATE_DEBUT_DISPO)
            .dateFinDispo(UPDATED_DATE_FIN_DISPO);

        restFormateurMockMvc.perform(put("/api/formateurs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedFormateur)))
            .andExpect(status().isOk());

        // Validate the Formateur in the database
        List<Formateur> formateurList = formateurRepository.findAll();
        assertThat(formateurList).hasSize(databaseSizeBeforeUpdate);
        Formateur testFormateur = formateurList.get(formateurList.size() - 1);
        assertThat(testFormateur.getDateDebutDispo()).isEqualTo(UPDATED_DATE_DEBUT_DISPO);
        assertThat(testFormateur.getDateFinDispo()).isEqualTo(UPDATED_DATE_FIN_DISPO);

        // Validate the Formateur in Elasticsearch
        Formateur formateurEs = formateurSearchRepository.findOne(testFormateur.getId());
        assertThat(formateurEs).isEqualToIgnoringGivenFields(testFormateur);
    }

    @Test
    @Transactional
    public void updateNonExistingFormateur() throws Exception {
        int databaseSizeBeforeUpdate = formateurRepository.findAll().size();

        // Create the Formateur

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restFormateurMockMvc.perform(put("/api/formateurs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(formateur)))
            .andExpect(status().isCreated());

        // Validate the Formateur in the database
        List<Formateur> formateurList = formateurRepository.findAll();
        assertThat(formateurList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteFormateur() throws Exception {
        // Initialize the database
        formateurService.save(formateur);

        int databaseSizeBeforeDelete = formateurRepository.findAll().size();

        // Get the formateur
        restFormateurMockMvc.perform(delete("/api/formateurs/{id}", formateur.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean formateurExistsInEs = formateurSearchRepository.exists(formateur.getId());
        assertThat(formateurExistsInEs).isFalse();

        // Validate the database is empty
        List<Formateur> formateurList = formateurRepository.findAll();
        assertThat(formateurList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchFormateur() throws Exception {
        // Initialize the database
        formateurService.save(formateur);

        // Search the formateur
        restFormateurMockMvc.perform(get("/api/_search/formateurs?query=id:" + formateur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(formateur.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateDebutDispo").value(hasItem(DEFAULT_DATE_DEBUT_DISPO.toString())))
            .andExpect(jsonPath("$.[*].dateFinDispo").value(hasItem(DEFAULT_DATE_FIN_DISPO.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Formateur.class);
        Formateur formateur1 = new Formateur();
        formateur1.setId(1L);
        Formateur formateur2 = new Formateur();
        formateur2.setId(formateur1.getId());
        assertThat(formateur1).isEqualTo(formateur2);
        formateur2.setId(2L);
        assertThat(formateur1).isNotEqualTo(formateur2);
        formateur1.setId(null);
        assertThat(formateur1).isNotEqualTo(formateur2);
    }
}
