package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.MyappApp;

import com.mycompany.myapp.domain.Requirement;
import com.mycompany.myapp.domain.Offer;
import com.mycompany.myapp.repository.RequirementRepository;
import com.mycompany.myapp.repository.search.RequirementSearchRepository;
import com.mycompany.myapp.web.rest.errors.ExceptionTranslator;

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

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the RequirementResource REST controller.
 *
 * @see RequirementResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MyappApp.class)
public class RequirementResourceIntTest {

    private static final String DEFAULT_IS_OBLIGATORY = "A";
    private static final String UPDATED_IS_OBLIGATORY = "B";

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    @Autowired
    private RequirementRepository requirementRepository;

    @Autowired
    private RequirementSearchRepository requirementSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restRequirementMockMvc;

    private Requirement requirement;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RequirementResource requirementResource = new RequirementResource(requirementRepository, requirementSearchRepository);
        this.restRequirementMockMvc = MockMvcBuilders.standaloneSetup(requirementResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Requirement createEntity(EntityManager em) {
        Requirement requirement = new Requirement()
            .isObligatory(DEFAULT_IS_OBLIGATORY)
            .value(DEFAULT_VALUE);
        // Add required entity
        Offer offer = OfferResourceIntTest.createEntity(em);
        em.persist(offer);
        em.flush();
        requirement.setOffer(offer);
        return requirement;
    }

    @Before
    public void initTest() {
        requirementSearchRepository.deleteAll();
        requirement = createEntity(em);
    }

    @Test
    @Transactional
    public void createRequirement() throws Exception {
        int databaseSizeBeforeCreate = requirementRepository.findAll().size();

        // Create the Requirement
        restRequirementMockMvc.perform(post("/api/requirements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(requirement)))
            .andExpect(status().isCreated());

        // Validate the Requirement in the database
        List<Requirement> requirementList = requirementRepository.findAll();
        assertThat(requirementList).hasSize(databaseSizeBeforeCreate + 1);
        Requirement testRequirement = requirementList.get(requirementList.size() - 1);
        assertThat(testRequirement.getIsObligatory()).isEqualTo(DEFAULT_IS_OBLIGATORY);
        assertThat(testRequirement.getValue()).isEqualTo(DEFAULT_VALUE);

        // Validate the Requirement in Elasticsearch
        Requirement requirementEs = requirementSearchRepository.findOne(testRequirement.getId());
        assertThat(requirementEs).isEqualToComparingFieldByField(testRequirement);
    }

    @Test
    @Transactional
    public void createRequirementWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = requirementRepository.findAll().size();

        // Create the Requirement with an existing ID
        requirement.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRequirementMockMvc.perform(post("/api/requirements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(requirement)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Requirement> requirementList = requirementRepository.findAll();
        assertThat(requirementList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkIsObligatoryIsRequired() throws Exception {
        int databaseSizeBeforeTest = requirementRepository.findAll().size();
        // set the field null
        requirement.setIsObligatory(null);

        // Create the Requirement, which fails.

        restRequirementMockMvc.perform(post("/api/requirements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(requirement)))
            .andExpect(status().isBadRequest());

        List<Requirement> requirementList = requirementRepository.findAll();
        assertThat(requirementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = requirementRepository.findAll().size();
        // set the field null
        requirement.setValue(null);

        // Create the Requirement, which fails.

        restRequirementMockMvc.perform(post("/api/requirements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(requirement)))
            .andExpect(status().isBadRequest());

        List<Requirement> requirementList = requirementRepository.findAll();
        assertThat(requirementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRequirements() throws Exception {
        // Initialize the database
        requirementRepository.saveAndFlush(requirement);

        // Get all the requirementList
        restRequirementMockMvc.perform(get("/api/requirements?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(requirement.getId().intValue())))
            .andExpect(jsonPath("$.[*].isObligatory").value(hasItem(DEFAULT_IS_OBLIGATORY.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.toString())));
    }

    @Test
    @Transactional
    public void getRequirement() throws Exception {
        // Initialize the database
        requirementRepository.saveAndFlush(requirement);

        // Get the requirement
        restRequirementMockMvc.perform(get("/api/requirements/{id}", requirement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(requirement.getId().intValue()))
            .andExpect(jsonPath("$.isObligatory").value(DEFAULT_IS_OBLIGATORY.toString()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRequirement() throws Exception {
        // Get the requirement
        restRequirementMockMvc.perform(get("/api/requirements/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRequirement() throws Exception {
        // Initialize the database
        requirementRepository.saveAndFlush(requirement);
        requirementSearchRepository.save(requirement);
        int databaseSizeBeforeUpdate = requirementRepository.findAll().size();

        // Update the requirement
        Requirement updatedRequirement = requirementRepository.findOne(requirement.getId());
        updatedRequirement
            .isObligatory(UPDATED_IS_OBLIGATORY)
            .value(UPDATED_VALUE);

        restRequirementMockMvc.perform(put("/api/requirements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRequirement)))
            .andExpect(status().isOk());

        // Validate the Requirement in the database
        List<Requirement> requirementList = requirementRepository.findAll();
        assertThat(requirementList).hasSize(databaseSizeBeforeUpdate);
        Requirement testRequirement = requirementList.get(requirementList.size() - 1);
        assertThat(testRequirement.getIsObligatory()).isEqualTo(UPDATED_IS_OBLIGATORY);
        assertThat(testRequirement.getValue()).isEqualTo(UPDATED_VALUE);

        // Validate the Requirement in Elasticsearch
        Requirement requirementEs = requirementSearchRepository.findOne(testRequirement.getId());
        assertThat(requirementEs).isEqualToComparingFieldByField(testRequirement);
    }

    @Test
    @Transactional
    public void updateNonExistingRequirement() throws Exception {
        int databaseSizeBeforeUpdate = requirementRepository.findAll().size();

        // Create the Requirement

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restRequirementMockMvc.perform(put("/api/requirements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(requirement)))
            .andExpect(status().isCreated());

        // Validate the Requirement in the database
        List<Requirement> requirementList = requirementRepository.findAll();
        assertThat(requirementList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteRequirement() throws Exception {
        // Initialize the database
        requirementRepository.saveAndFlush(requirement);
        requirementSearchRepository.save(requirement);
        int databaseSizeBeforeDelete = requirementRepository.findAll().size();

        // Get the requirement
        restRequirementMockMvc.perform(delete("/api/requirements/{id}", requirement.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean requirementExistsInEs = requirementSearchRepository.exists(requirement.getId());
        assertThat(requirementExistsInEs).isFalse();

        // Validate the database is empty
        List<Requirement> requirementList = requirementRepository.findAll();
        assertThat(requirementList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchRequirement() throws Exception {
        // Initialize the database
        requirementRepository.saveAndFlush(requirement);
        requirementSearchRepository.save(requirement);

        // Search the requirement
        restRequirementMockMvc.perform(get("/api/_search/requirements?query=id:" + requirement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(requirement.getId().intValue())))
            .andExpect(jsonPath("$.[*].isObligatory").value(hasItem(DEFAULT_IS_OBLIGATORY.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Requirement.class);
        Requirement requirement1 = new Requirement();
        requirement1.setId(1L);
        Requirement requirement2 = new Requirement();
        requirement2.setId(requirement1.getId());
        assertThat(requirement1).isEqualTo(requirement2);
        requirement2.setId(2L);
        assertThat(requirement1).isNotEqualTo(requirement2);
        requirement1.setId(null);
        assertThat(requirement1).isNotEqualTo(requirement2);
    }
}
