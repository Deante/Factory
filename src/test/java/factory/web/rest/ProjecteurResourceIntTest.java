package factory.web.rest;

import factory.FactoryApp;

import factory.domain.Projecteur;
import factory.repository.ProjecteurRepository;
import factory.service.ProjecteurService;
import factory.repository.search.ProjecteurSearchRepository;
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
 * Test class for the ProjecteurResource REST controller.
 *
 * @see ProjecteurResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FactoryApp.class)
public class ProjecteurResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBB";

    private static final Double DEFAULT_COUT_JOUR = 1D;
    private static final Double UPDATED_COUT_JOUR = 2D;

    private static final EtatMaterielEnum DEFAULT_ETAT = EtatMaterielEnum.OK;
    private static final EtatMaterielEnum UPDATED_ETAT = EtatMaterielEnum.ABIME;

    @Autowired
    private ProjecteurRepository projecteurRepository;

    @Autowired
    private ProjecteurService projecteurService;

    @Autowired
    private ProjecteurSearchRepository projecteurSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restProjecteurMockMvc;

    private Projecteur projecteur;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProjecteurResource projecteurResource = new ProjecteurResource(projecteurService);
        this.restProjecteurMockMvc = MockMvcBuilders.standaloneSetup(projecteurResource)
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
    public static Projecteur createEntity(EntityManager em) {
        Projecteur projecteur = new Projecteur()
            .code(DEFAULT_CODE)
            .coutJour(DEFAULT_COUT_JOUR)
            .etat(DEFAULT_ETAT);
        return projecteur;
    }

    @Before
    public void initTest() {
        projecteurSearchRepository.deleteAll();
        projecteur = createEntity(em);
    }

    @Test
    @Transactional
    public void createProjecteur() throws Exception {
        int databaseSizeBeforeCreate = projecteurRepository.findAll().size();

        // Create the Projecteur
        restProjecteurMockMvc.perform(post("/api/projecteurs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projecteur)))
            .andExpect(status().isCreated());

        // Validate the Projecteur in the database
        List<Projecteur> projecteurList = projecteurRepository.findAll();
        assertThat(projecteurList).hasSize(databaseSizeBeforeCreate + 1);
        Projecteur testProjecteur = projecteurList.get(projecteurList.size() - 1);
        assertThat(testProjecteur.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testProjecteur.getCoutJour()).isEqualTo(DEFAULT_COUT_JOUR);
        assertThat(testProjecteur.getEtat()).isEqualTo(DEFAULT_ETAT);

        // Validate the Projecteur in Elasticsearch
        Projecteur projecteurEs = projecteurSearchRepository.findOne(testProjecteur.getId());
        assertThat(projecteurEs).isEqualToIgnoringGivenFields(testProjecteur);
    }

    @Test
    @Transactional
    public void createProjecteurWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = projecteurRepository.findAll().size();

        // Create the Projecteur with an existing ID
        projecteur.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProjecteurMockMvc.perform(post("/api/projecteurs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projecteur)))
            .andExpect(status().isBadRequest());

        // Validate the Projecteur in the database
        List<Projecteur> projecteurList = projecteurRepository.findAll();
        assertThat(projecteurList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = projecteurRepository.findAll().size();
        // set the field null
        projecteur.setCode(null);

        // Create the Projecteur, which fails.

        restProjecteurMockMvc.perform(post("/api/projecteurs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projecteur)))
            .andExpect(status().isBadRequest());

        List<Projecteur> projecteurList = projecteurRepository.findAll();
        assertThat(projecteurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProjecteurs() throws Exception {
        // Initialize the database
        projecteurRepository.saveAndFlush(projecteur);

        // Get all the projecteurList
        restProjecteurMockMvc.perform(get("/api/projecteurs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(projecteur.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].coutJour").value(hasItem(DEFAULT_COUT_JOUR.doubleValue())))
            .andExpect(jsonPath("$.[*].etat").value(hasItem(DEFAULT_ETAT.toString())));
    }

    @Test
    @Transactional
    public void getProjecteur() throws Exception {
        // Initialize the database
        projecteurRepository.saveAndFlush(projecteur);

        // Get the projecteur
        restProjecteurMockMvc.perform(get("/api/projecteurs/{id}", projecteur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(projecteur.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.coutJour").value(DEFAULT_COUT_JOUR.doubleValue()))
            .andExpect(jsonPath("$.etat").value(DEFAULT_ETAT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingProjecteur() throws Exception {
        // Get the projecteur
        restProjecteurMockMvc.perform(get("/api/projecteurs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProjecteur() throws Exception {
        // Initialize the database
        projecteurService.save(projecteur);

        int databaseSizeBeforeUpdate = projecteurRepository.findAll().size();

        // Update the projecteur
        Projecteur updatedProjecteur = projecteurRepository.findOne(projecteur.getId());
        // Disconnect from session so that the updates on updatedProjecteur are not directly saved in db
        em.detach(updatedProjecteur);
        updatedProjecteur
            .code(UPDATED_CODE)
            .coutJour(UPDATED_COUT_JOUR)
            .etat(UPDATED_ETAT);

        restProjecteurMockMvc.perform(put("/api/projecteurs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedProjecteur)))
            .andExpect(status().isOk());

        // Validate the Projecteur in the database
        List<Projecteur> projecteurList = projecteurRepository.findAll();
        assertThat(projecteurList).hasSize(databaseSizeBeforeUpdate);
        Projecteur testProjecteur = projecteurList.get(projecteurList.size() - 1);
        assertThat(testProjecteur.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testProjecteur.getCoutJour()).isEqualTo(UPDATED_COUT_JOUR);
        assertThat(testProjecteur.getEtat()).isEqualTo(UPDATED_ETAT);

        // Validate the Projecteur in Elasticsearch
        Projecteur projecteurEs = projecteurSearchRepository.findOne(testProjecteur.getId());
        assertThat(projecteurEs).isEqualToIgnoringGivenFields(testProjecteur);
    }

    @Test
    @Transactional
    public void updateNonExistingProjecteur() throws Exception {
        int databaseSizeBeforeUpdate = projecteurRepository.findAll().size();

        // Create the Projecteur

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restProjecteurMockMvc.perform(put("/api/projecteurs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projecteur)))
            .andExpect(status().isCreated());

        // Validate the Projecteur in the database
        List<Projecteur> projecteurList = projecteurRepository.findAll();
        assertThat(projecteurList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteProjecteur() throws Exception {
        // Initialize the database
        projecteurService.save(projecteur);

        int databaseSizeBeforeDelete = projecteurRepository.findAll().size();

        // Get the projecteur
        restProjecteurMockMvc.perform(delete("/api/projecteurs/{id}", projecteur.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean projecteurExistsInEs = projecteurSearchRepository.exists(projecteur.getId());
        assertThat(projecteurExistsInEs).isFalse();

        // Validate the database is empty
        List<Projecteur> projecteurList = projecteurRepository.findAll();
        assertThat(projecteurList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchProjecteur() throws Exception {
        // Initialize the database
        projecteurService.save(projecteur);

        // Search the projecteur
        restProjecteurMockMvc.perform(get("/api/_search/projecteurs?query=id:" + projecteur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(projecteur.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].coutJour").value(hasItem(DEFAULT_COUT_JOUR.doubleValue())))
            .andExpect(jsonPath("$.[*].etat").value(hasItem(DEFAULT_ETAT.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Projecteur.class);
        Projecteur projecteur1 = new Projecteur();
        projecteur1.setId(1L);
        Projecteur projecteur2 = new Projecteur();
        projecteur2.setId(projecteur1.getId());
        assertThat(projecteur1).isEqualTo(projecteur2);
        projecteur2.setId(2L);
        assertThat(projecteur1).isNotEqualTo(projecteur2);
        projecteur1.setId(null);
        assertThat(projecteur1).isNotEqualTo(projecteur2);
    }
}
