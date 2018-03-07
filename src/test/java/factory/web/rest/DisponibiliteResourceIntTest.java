package factory.web.rest;

import factory.FactoryApp;

import factory.domain.Disponibilite;
import factory.repository.DisponibiliteRepository;
import factory.service.DisponibiliteService;
import factory.repository.search.DisponibiliteSearchRepository;
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
 * Test class for the DisponibiliteResource REST controller.
 *
 * @see DisponibiliteResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FactoryApp.class)
public class DisponibiliteResourceIntTest {

    private static final LocalDate DEFAULT_DATE_DEBUT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DEBUT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_FIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_FIN = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private DisponibiliteRepository disponibiliteRepository;

    @Autowired
    private DisponibiliteService disponibiliteService;

    @Autowired
    private DisponibiliteSearchRepository disponibiliteSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDisponibiliteMockMvc;

    private Disponibilite disponibilite;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DisponibiliteResource disponibiliteResource = new DisponibiliteResource(disponibiliteService);
        this.restDisponibiliteMockMvc = MockMvcBuilders.standaloneSetup(disponibiliteResource)
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
    public static Disponibilite createEntity(EntityManager em) {
        Disponibilite disponibilite = new Disponibilite()
            .dateDebut(DEFAULT_DATE_DEBUT)
            .dateFin(DEFAULT_DATE_FIN);
        return disponibilite;
    }

    @Before
    public void initTest() {
        disponibiliteSearchRepository.deleteAll();
        disponibilite = createEntity(em);
    }

    @Test
    @Transactional
    public void createDisponibilite() throws Exception {
        int databaseSizeBeforeCreate = disponibiliteRepository.findAll().size();

        // Create the Disponibilite
        restDisponibiliteMockMvc.perform(post("/api/disponibilites")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(disponibilite)))
            .andExpect(status().isCreated());

        // Validate the Disponibilite in the database
        List<Disponibilite> disponibiliteList = disponibiliteRepository.findAll();
        assertThat(disponibiliteList).hasSize(databaseSizeBeforeCreate + 1);
        Disponibilite testDisponibilite = disponibiliteList.get(disponibiliteList.size() - 1);
        assertThat(testDisponibilite.getDateDebut()).isEqualTo(DEFAULT_DATE_DEBUT);
        assertThat(testDisponibilite.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);

        // Validate the Disponibilite in Elasticsearch
        Disponibilite disponibiliteEs = disponibiliteSearchRepository.findOne(testDisponibilite.getId());
        assertThat(disponibiliteEs).isEqualToIgnoringGivenFields(testDisponibilite);
    }

    @Test
    @Transactional
    public void createDisponibiliteWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = disponibiliteRepository.findAll().size();

        // Create the Disponibilite with an existing ID
        disponibilite.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDisponibiliteMockMvc.perform(post("/api/disponibilites")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(disponibilite)))
            .andExpect(status().isBadRequest());

        // Validate the Disponibilite in the database
        List<Disponibilite> disponibiliteList = disponibiliteRepository.findAll();
        assertThat(disponibiliteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDateDebutIsRequired() throws Exception {
        int databaseSizeBeforeTest = disponibiliteRepository.findAll().size();
        // set the field null
        disponibilite.setDateDebut(null);

        // Create the Disponibilite, which fails.

        restDisponibiliteMockMvc.perform(post("/api/disponibilites")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(disponibilite)))
            .andExpect(status().isBadRequest());

        List<Disponibilite> disponibiliteList = disponibiliteRepository.findAll();
        assertThat(disponibiliteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateFinIsRequired() throws Exception {
        int databaseSizeBeforeTest = disponibiliteRepository.findAll().size();
        // set the field null
        disponibilite.setDateFin(null);

        // Create the Disponibilite, which fails.

        restDisponibiliteMockMvc.perform(post("/api/disponibilites")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(disponibilite)))
            .andExpect(status().isBadRequest());

        List<Disponibilite> disponibiliteList = disponibiliteRepository.findAll();
        assertThat(disponibiliteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDisponibilites() throws Exception {
        // Initialize the database
        disponibiliteRepository.saveAndFlush(disponibilite);

        // Get all the disponibiliteList
        restDisponibiliteMockMvc.perform(get("/api/disponibilites?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(disponibilite.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())));
    }

    @Test
    @Transactional
    public void getDisponibilite() throws Exception {
        // Initialize the database
        disponibiliteRepository.saveAndFlush(disponibilite);

        // Get the disponibilite
        restDisponibiliteMockMvc.perform(get("/api/disponibilites/{id}", disponibilite.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(disponibilite.getId().intValue()))
            .andExpect(jsonPath("$.dateDebut").value(DEFAULT_DATE_DEBUT.toString()))
            .andExpect(jsonPath("$.dateFin").value(DEFAULT_DATE_FIN.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDisponibilite() throws Exception {
        // Get the disponibilite
        restDisponibiliteMockMvc.perform(get("/api/disponibilites/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDisponibilite() throws Exception {
        // Initialize the database
        disponibiliteService.save(disponibilite);

        int databaseSizeBeforeUpdate = disponibiliteRepository.findAll().size();

        // Update the disponibilite
        Disponibilite updatedDisponibilite = disponibiliteRepository.findOne(disponibilite.getId());
        // Disconnect from session so that the updates on updatedDisponibilite are not directly saved in db
        em.detach(updatedDisponibilite);
        updatedDisponibilite
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN);

        restDisponibiliteMockMvc.perform(put("/api/disponibilites")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDisponibilite)))
            .andExpect(status().isOk());

        // Validate the Disponibilite in the database
        List<Disponibilite> disponibiliteList = disponibiliteRepository.findAll();
        assertThat(disponibiliteList).hasSize(databaseSizeBeforeUpdate);
        Disponibilite testDisponibilite = disponibiliteList.get(disponibiliteList.size() - 1);
        assertThat(testDisponibilite.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testDisponibilite.getDateFin()).isEqualTo(UPDATED_DATE_FIN);

        // Validate the Disponibilite in Elasticsearch
        Disponibilite disponibiliteEs = disponibiliteSearchRepository.findOne(testDisponibilite.getId());
        assertThat(disponibiliteEs).isEqualToIgnoringGivenFields(testDisponibilite);
    }

    @Test
    @Transactional
    public void updateNonExistingDisponibilite() throws Exception {
        int databaseSizeBeforeUpdate = disponibiliteRepository.findAll().size();

        // Create the Disponibilite

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDisponibiliteMockMvc.perform(put("/api/disponibilites")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(disponibilite)))
            .andExpect(status().isCreated());

        // Validate the Disponibilite in the database
        List<Disponibilite> disponibiliteList = disponibiliteRepository.findAll();
        assertThat(disponibiliteList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteDisponibilite() throws Exception {
        // Initialize the database
        disponibiliteService.save(disponibilite);

        int databaseSizeBeforeDelete = disponibiliteRepository.findAll().size();

        // Get the disponibilite
        restDisponibiliteMockMvc.perform(delete("/api/disponibilites/{id}", disponibilite.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean disponibiliteExistsInEs = disponibiliteSearchRepository.exists(disponibilite.getId());
        assertThat(disponibiliteExistsInEs).isFalse();

        // Validate the database is empty
        List<Disponibilite> disponibiliteList = disponibiliteRepository.findAll();
        assertThat(disponibiliteList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchDisponibilite() throws Exception {
        // Initialize the database
        disponibiliteService.save(disponibilite);

        // Search the disponibilite
        restDisponibiliteMockMvc.perform(get("/api/_search/disponibilites?query=id:" + disponibilite.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(disponibilite.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Disponibilite.class);
        Disponibilite disponibilite1 = new Disponibilite();
        disponibilite1.setId(1L);
        Disponibilite disponibilite2 = new Disponibilite();
        disponibilite2.setId(disponibilite1.getId());
        assertThat(disponibilite1).isEqualTo(disponibilite2);
        disponibilite2.setId(2L);
        assertThat(disponibilite1).isNotEqualTo(disponibilite2);
        disponibilite1.setId(null);
        assertThat(disponibilite1).isNotEqualTo(disponibilite2);
    }
}
