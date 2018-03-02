package factory.web.rest;

import factory.FactoryApp;

import factory.domain.Ordinateur;
import factory.repository.OrdinateurRepository;
import factory.service.OrdinateurService;
import factory.repository.search.OrdinateurSearchRepository;
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

import factory.domain.enumeration.EtatMaterielEnum;
/**
 * Test class for the OrdinateurResource REST controller.
 *
 * @see OrdinateurResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FactoryApp.class)
public class OrdinateurResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBB";

    private static final String DEFAULT_PROCESSEUR = "AAAAAAAAAA";
    private static final String UPDATED_PROCESSEUR = "BBBBBBBBBB";

    private static final Integer DEFAULT_MEMOIRE = 8;
    private static final Integer UPDATED_MEMOIRE = 9;

    private static final Integer DEFAULT_DISQUE = 128;
    private static final Integer UPDATED_DISQUE = 129;

    private static final String DEFAULT_ANNEE_ACHAT = "AAAAAAAAAA";
    private static final String UPDATED_ANNEE_ACHAT = "BBBBBBBBBB";

    private static final Double DEFAULT_COUT_JOUR = 1D;
    private static final Double UPDATED_COUT_JOUR = 2D;

    private static final EtatMaterielEnum DEFAULT_ETAT = EtatMaterielEnum.OK;
    private static final EtatMaterielEnum UPDATED_ETAT = EtatMaterielEnum.ABIME;

    @Autowired
    private OrdinateurRepository ordinateurRepository;

    @Autowired
    private OrdinateurService ordinateurService;

    @Autowired
    private OrdinateurSearchRepository ordinateurSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restOrdinateurMockMvc;

    private Ordinateur ordinateur;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final OrdinateurResource ordinateurResource = new OrdinateurResource(ordinateurService);
        this.restOrdinateurMockMvc = MockMvcBuilders.standaloneSetup(ordinateurResource)
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
    public static Ordinateur createEntity(EntityManager em) {
        Ordinateur ordinateur = new Ordinateur()
            .code(DEFAULT_CODE)
            .processeur(DEFAULT_PROCESSEUR)
            .memoire(DEFAULT_MEMOIRE)
            .disque(DEFAULT_DISQUE)
            .anneeAchat(DEFAULT_ANNEE_ACHAT)
            .coutJour(DEFAULT_COUT_JOUR)
            .etat(DEFAULT_ETAT);
        return ordinateur;
    }

    @Before
    public void initTest() {
        ordinateurSearchRepository.deleteAll();
        ordinateur = createEntity(em);
    }

    @Test
    @Transactional
    public void createOrdinateur() throws Exception {
        int databaseSizeBeforeCreate = ordinateurRepository.findAll().size();

        // Create the Ordinateur
        restOrdinateurMockMvc.perform(post("/api/ordinateurs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ordinateur)))
            .andExpect(status().isCreated());

        // Validate the Ordinateur in the database
        List<Ordinateur> ordinateurList = ordinateurRepository.findAll();
        assertThat(ordinateurList).hasSize(databaseSizeBeforeCreate + 1);
        Ordinateur testOrdinateur = ordinateurList.get(ordinateurList.size() - 1);
        assertThat(testOrdinateur.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testOrdinateur.getProcesseur()).isEqualTo(DEFAULT_PROCESSEUR);
        assertThat(testOrdinateur.getMemoire()).isEqualTo(DEFAULT_MEMOIRE);
        assertThat(testOrdinateur.getDisque()).isEqualTo(DEFAULT_DISQUE);
        assertThat(testOrdinateur.getAnneeAchat()).isEqualTo(DEFAULT_ANNEE_ACHAT);
        assertThat(testOrdinateur.getCoutJour()).isEqualTo(DEFAULT_COUT_JOUR);
        assertThat(testOrdinateur.getEtat()).isEqualTo(DEFAULT_ETAT);

        // Validate the Ordinateur in Elasticsearch
        Ordinateur ordinateurEs = ordinateurSearchRepository.findOne(testOrdinateur.getId());
        assertThat(ordinateurEs).isEqualToIgnoringGivenFields(testOrdinateur);
    }

    @Test
    @Transactional
    public void createOrdinateurWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = ordinateurRepository.findAll().size();

        // Create the Ordinateur with an existing ID
        ordinateur.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrdinateurMockMvc.perform(post("/api/ordinateurs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ordinateur)))
            .andExpect(status().isBadRequest());

        // Validate the Ordinateur in the database
        List<Ordinateur> ordinateurList = ordinateurRepository.findAll();
        assertThat(ordinateurList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = ordinateurRepository.findAll().size();
        // set the field null
        ordinateur.setCode(null);

        // Create the Ordinateur, which fails.

        restOrdinateurMockMvc.perform(post("/api/ordinateurs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ordinateur)))
            .andExpect(status().isBadRequest());

        List<Ordinateur> ordinateurList = ordinateurRepository.findAll();
        assertThat(ordinateurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllOrdinateurs() throws Exception {
        // Initialize the database
        ordinateurRepository.saveAndFlush(ordinateur);

        // Get all the ordinateurList
        restOrdinateurMockMvc.perform(get("/api/ordinateurs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ordinateur.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].processeur").value(hasItem(DEFAULT_PROCESSEUR.toString())))
            .andExpect(jsonPath("$.[*].memoire").value(hasItem(DEFAULT_MEMOIRE)))
            .andExpect(jsonPath("$.[*].disque").value(hasItem(DEFAULT_DISQUE)))
            .andExpect(jsonPath("$.[*].anneeAchat").value(hasItem(DEFAULT_ANNEE_ACHAT.toString())))
            .andExpect(jsonPath("$.[*].coutJour").value(hasItem(DEFAULT_COUT_JOUR.doubleValue())))
            .andExpect(jsonPath("$.[*].etat").value(hasItem(DEFAULT_ETAT.toString())));
    }

    @Test
    @Transactional
    public void getOrdinateur() throws Exception {
        // Initialize the database
        ordinateurRepository.saveAndFlush(ordinateur);

        // Get the ordinateur
        restOrdinateurMockMvc.perform(get("/api/ordinateurs/{id}", ordinateur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(ordinateur.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.processeur").value(DEFAULT_PROCESSEUR.toString()))
            .andExpect(jsonPath("$.memoire").value(DEFAULT_MEMOIRE))
            .andExpect(jsonPath("$.disque").value(DEFAULT_DISQUE))
            .andExpect(jsonPath("$.anneeAchat").value(DEFAULT_ANNEE_ACHAT.toString()))
            .andExpect(jsonPath("$.coutJour").value(DEFAULT_COUT_JOUR.doubleValue()))
            .andExpect(jsonPath("$.etat").value(DEFAULT_ETAT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingOrdinateur() throws Exception {
        // Get the ordinateur
        restOrdinateurMockMvc.perform(get("/api/ordinateurs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOrdinateur() throws Exception {
        // Initialize the database
        ordinateurService.save(ordinateur);

        int databaseSizeBeforeUpdate = ordinateurRepository.findAll().size();

        // Update the ordinateur
        Ordinateur updatedOrdinateur = ordinateurRepository.findOne(ordinateur.getId());
        // Disconnect from session so that the updates on updatedOrdinateur are not directly saved in db
        em.detach(updatedOrdinateur);
        updatedOrdinateur
            .code(UPDATED_CODE)
            .processeur(UPDATED_PROCESSEUR)
            .memoire(UPDATED_MEMOIRE)
            .disque(UPDATED_DISQUE)
            .anneeAchat(UPDATED_ANNEE_ACHAT)
            .coutJour(UPDATED_COUT_JOUR)
            .etat(UPDATED_ETAT);

        restOrdinateurMockMvc.perform(put("/api/ordinateurs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedOrdinateur)))
            .andExpect(status().isOk());

        // Validate the Ordinateur in the database
        List<Ordinateur> ordinateurList = ordinateurRepository.findAll();
        assertThat(ordinateurList).hasSize(databaseSizeBeforeUpdate);
        Ordinateur testOrdinateur = ordinateurList.get(ordinateurList.size() - 1);
        assertThat(testOrdinateur.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testOrdinateur.getProcesseur()).isEqualTo(UPDATED_PROCESSEUR);
        assertThat(testOrdinateur.getMemoire()).isEqualTo(UPDATED_MEMOIRE);
        assertThat(testOrdinateur.getDisque()).isEqualTo(UPDATED_DISQUE);
        assertThat(testOrdinateur.getAnneeAchat()).isEqualTo(UPDATED_ANNEE_ACHAT);
        assertThat(testOrdinateur.getCoutJour()).isEqualTo(UPDATED_COUT_JOUR);
        assertThat(testOrdinateur.getEtat()).isEqualTo(UPDATED_ETAT);

        // Validate the Ordinateur in Elasticsearch
        Ordinateur ordinateurEs = ordinateurSearchRepository.findOne(testOrdinateur.getId());
        assertThat(ordinateurEs).isEqualToIgnoringGivenFields(testOrdinateur);
    }

    @Test
    @Transactional
    public void updateNonExistingOrdinateur() throws Exception {
        int databaseSizeBeforeUpdate = ordinateurRepository.findAll().size();

        // Create the Ordinateur

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restOrdinateurMockMvc.perform(put("/api/ordinateurs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ordinateur)))
            .andExpect(status().isCreated());

        // Validate the Ordinateur in the database
        List<Ordinateur> ordinateurList = ordinateurRepository.findAll();
        assertThat(ordinateurList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteOrdinateur() throws Exception {
        // Initialize the database
        ordinateurService.save(ordinateur);

        int databaseSizeBeforeDelete = ordinateurRepository.findAll().size();

        // Get the ordinateur
        restOrdinateurMockMvc.perform(delete("/api/ordinateurs/{id}", ordinateur.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean ordinateurExistsInEs = ordinateurSearchRepository.exists(ordinateur.getId());
        assertThat(ordinateurExistsInEs).isFalse();

        // Validate the database is empty
        List<Ordinateur> ordinateurList = ordinateurRepository.findAll();
        assertThat(ordinateurList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchOrdinateur() throws Exception {
        // Initialize the database
        ordinateurService.save(ordinateur);

        // Search the ordinateur
        restOrdinateurMockMvc.perform(get("/api/_search/ordinateurs?query=id:" + ordinateur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ordinateur.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].processeur").value(hasItem(DEFAULT_PROCESSEUR.toString())))
            .andExpect(jsonPath("$.[*].memoire").value(hasItem(DEFAULT_MEMOIRE)))
            .andExpect(jsonPath("$.[*].disque").value(hasItem(DEFAULT_DISQUE)))
            .andExpect(jsonPath("$.[*].anneeAchat").value(hasItem(DEFAULT_ANNEE_ACHAT.toString())))
            .andExpect(jsonPath("$.[*].coutJour").value(hasItem(DEFAULT_COUT_JOUR.doubleValue())))
            .andExpect(jsonPath("$.[*].etat").value(hasItem(DEFAULT_ETAT.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Ordinateur.class);
        Ordinateur ordinateur1 = new Ordinateur();
        ordinateur1.setId(1L);
        Ordinateur ordinateur2 = new Ordinateur();
        ordinateur2.setId(ordinateur1.getId());
        assertThat(ordinateur1).isEqualTo(ordinateur2);
        ordinateur2.setId(2L);
        assertThat(ordinateur1).isNotEqualTo(ordinateur2);
        ordinateur1.setId(null);
        assertThat(ordinateur1).isNotEqualTo(ordinateur2);
    }
}
