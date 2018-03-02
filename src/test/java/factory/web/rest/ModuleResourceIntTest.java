package factory.web.rest;

import factory.FactoryApp;

import factory.domain.Module;
import factory.repository.ModuleRepository;
import factory.service.ModuleService;
import factory.repository.search.ModuleSearchRepository;
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

import factory.domain.enumeration.CouleurEnum;
/**
 * Test class for the ModuleResource REST controller.
 *
 * @see ModuleResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FactoryApp.class)
public class ModuleResourceIntTest {

    private static final String DEFAULT_TITRE = "AAAAAAAAAA";
    private static final String UPDATED_TITRE = "BBBBBBBBBB";

    private static final Double DEFAULT_DUREE = 1D;
    private static final Double UPDATED_DUREE = 2D;

    private static final CouleurEnum DEFAULT_COULEUR = CouleurEnum.ROUGE;
    private static final CouleurEnum UPDATED_COULEUR = CouleurEnum.VERT;

    private static final String DEFAULT_PREREQUIS = "AAAAAAAAAA";
    private static final String UPDATED_PREREQUIS = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENU = "AAAAAAAAAA";
    private static final String UPDATED_CONTENU = "BBBBBBBBBB";

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private ModuleSearchRepository moduleSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restModuleMockMvc;

    private Module module;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ModuleResource moduleResource = new ModuleResource(moduleService);
        this.restModuleMockMvc = MockMvcBuilders.standaloneSetup(moduleResource)
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
    public static Module createEntity(EntityManager em) {
        Module module = new Module()
            .titre(DEFAULT_TITRE)
            .duree(DEFAULT_DUREE)
            .couleur(DEFAULT_COULEUR)
            .prerequis(DEFAULT_PREREQUIS)
            .contenu(DEFAULT_CONTENU);
        return module;
    }

    @Before
    public void initTest() {
        moduleSearchRepository.deleteAll();
        module = createEntity(em);
    }

    @Test
    @Transactional
    public void createModule() throws Exception {
        int databaseSizeBeforeCreate = moduleRepository.findAll().size();

        // Create the Module
        restModuleMockMvc.perform(post("/api/modules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(module)))
            .andExpect(status().isCreated());

        // Validate the Module in the database
        List<Module> moduleList = moduleRepository.findAll();
        assertThat(moduleList).hasSize(databaseSizeBeforeCreate + 1);
        Module testModule = moduleList.get(moduleList.size() - 1);
        assertThat(testModule.getTitre()).isEqualTo(DEFAULT_TITRE);
        assertThat(testModule.getDuree()).isEqualTo(DEFAULT_DUREE);
        assertThat(testModule.getCouleur()).isEqualTo(DEFAULT_COULEUR);
        assertThat(testModule.getPrerequis()).isEqualTo(DEFAULT_PREREQUIS);
        assertThat(testModule.getContenu()).isEqualTo(DEFAULT_CONTENU);

        // Validate the Module in Elasticsearch
        Module moduleEs = moduleSearchRepository.findOne(testModule.getId());
        assertThat(moduleEs).isEqualToIgnoringGivenFields(testModule);
    }

    @Test
    @Transactional
    public void createModuleWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = moduleRepository.findAll().size();

        // Create the Module with an existing ID
        module.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restModuleMockMvc.perform(post("/api/modules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(module)))
            .andExpect(status().isBadRequest());

        // Validate the Module in the database
        List<Module> moduleList = moduleRepository.findAll();
        assertThat(moduleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTitreIsRequired() throws Exception {
        int databaseSizeBeforeTest = moduleRepository.findAll().size();
        // set the field null
        module.setTitre(null);

        // Create the Module, which fails.

        restModuleMockMvc.perform(post("/api/modules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(module)))
            .andExpect(status().isBadRequest());

        List<Module> moduleList = moduleRepository.findAll();
        assertThat(moduleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDureeIsRequired() throws Exception {
        int databaseSizeBeforeTest = moduleRepository.findAll().size();
        // set the field null
        module.setDuree(null);

        // Create the Module, which fails.

        restModuleMockMvc.perform(post("/api/modules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(module)))
            .andExpect(status().isBadRequest());

        List<Module> moduleList = moduleRepository.findAll();
        assertThat(moduleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllModules() throws Exception {
        // Initialize the database
        moduleRepository.saveAndFlush(module);

        // Get all the moduleList
        restModuleMockMvc.perform(get("/api/modules?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(module.getId().intValue())))
            .andExpect(jsonPath("$.[*].titre").value(hasItem(DEFAULT_TITRE.toString())))
            .andExpect(jsonPath("$.[*].duree").value(hasItem(DEFAULT_DUREE.doubleValue())))
            .andExpect(jsonPath("$.[*].couleur").value(hasItem(DEFAULT_COULEUR.toString())))
            .andExpect(jsonPath("$.[*].prerequis").value(hasItem(DEFAULT_PREREQUIS.toString())))
            .andExpect(jsonPath("$.[*].contenu").value(hasItem(DEFAULT_CONTENU.toString())));
    }

    @Test
    @Transactional
    public void getModule() throws Exception {
        // Initialize the database
        moduleRepository.saveAndFlush(module);

        // Get the module
        restModuleMockMvc.perform(get("/api/modules/{id}", module.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(module.getId().intValue()))
            .andExpect(jsonPath("$.titre").value(DEFAULT_TITRE.toString()))
            .andExpect(jsonPath("$.duree").value(DEFAULT_DUREE.doubleValue()))
            .andExpect(jsonPath("$.couleur").value(DEFAULT_COULEUR.toString()))
            .andExpect(jsonPath("$.prerequis").value(DEFAULT_PREREQUIS.toString()))
            .andExpect(jsonPath("$.contenu").value(DEFAULT_CONTENU.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingModule() throws Exception {
        // Get the module
        restModuleMockMvc.perform(get("/api/modules/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateModule() throws Exception {
        // Initialize the database
        moduleService.save(module);

        int databaseSizeBeforeUpdate = moduleRepository.findAll().size();

        // Update the module
        Module updatedModule = moduleRepository.findOne(module.getId());
        // Disconnect from session so that the updates on updatedModule are not directly saved in db
        em.detach(updatedModule);
        updatedModule
            .titre(UPDATED_TITRE)
            .duree(UPDATED_DUREE)
            .couleur(UPDATED_COULEUR)
            .prerequis(UPDATED_PREREQUIS)
            .contenu(UPDATED_CONTENU);

        restModuleMockMvc.perform(put("/api/modules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedModule)))
            .andExpect(status().isOk());

        // Validate the Module in the database
        List<Module> moduleList = moduleRepository.findAll();
        assertThat(moduleList).hasSize(databaseSizeBeforeUpdate);
        Module testModule = moduleList.get(moduleList.size() - 1);
        assertThat(testModule.getTitre()).isEqualTo(UPDATED_TITRE);
        assertThat(testModule.getDuree()).isEqualTo(UPDATED_DUREE);
        assertThat(testModule.getCouleur()).isEqualTo(UPDATED_COULEUR);
        assertThat(testModule.getPrerequis()).isEqualTo(UPDATED_PREREQUIS);
        assertThat(testModule.getContenu()).isEqualTo(UPDATED_CONTENU);

        // Validate the Module in Elasticsearch
        Module moduleEs = moduleSearchRepository.findOne(testModule.getId());
        assertThat(moduleEs).isEqualToIgnoringGivenFields(testModule);
    }

    @Test
    @Transactional
    public void updateNonExistingModule() throws Exception {
        int databaseSizeBeforeUpdate = moduleRepository.findAll().size();

        // Create the Module

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restModuleMockMvc.perform(put("/api/modules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(module)))
            .andExpect(status().isCreated());

        // Validate the Module in the database
        List<Module> moduleList = moduleRepository.findAll();
        assertThat(moduleList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteModule() throws Exception {
        // Initialize the database
        moduleService.save(module);

        int databaseSizeBeforeDelete = moduleRepository.findAll().size();

        // Get the module
        restModuleMockMvc.perform(delete("/api/modules/{id}", module.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean moduleExistsInEs = moduleSearchRepository.exists(module.getId());
        assertThat(moduleExistsInEs).isFalse();

        // Validate the database is empty
        List<Module> moduleList = moduleRepository.findAll();
        assertThat(moduleList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchModule() throws Exception {
        // Initialize the database
        moduleService.save(module);

        // Search the module
        restModuleMockMvc.perform(get("/api/_search/modules?query=id:" + module.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(module.getId().intValue())))
            .andExpect(jsonPath("$.[*].titre").value(hasItem(DEFAULT_TITRE.toString())))
            .andExpect(jsonPath("$.[*].duree").value(hasItem(DEFAULT_DUREE.doubleValue())))
            .andExpect(jsonPath("$.[*].couleur").value(hasItem(DEFAULT_COULEUR.toString())))
            .andExpect(jsonPath("$.[*].prerequis").value(hasItem(DEFAULT_PREREQUIS.toString())))
            .andExpect(jsonPath("$.[*].contenu").value(hasItem(DEFAULT_CONTENU.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Module.class);
        Module module1 = new Module();
        module1.setId(1L);
        Module module2 = new Module();
        module2.setId(module1.getId());
        assertThat(module1).isEqualTo(module2);
        module2.setId(2L);
        assertThat(module1).isNotEqualTo(module2);
        module1.setId(null);
        assertThat(module1).isNotEqualTo(module2);
    }
}
