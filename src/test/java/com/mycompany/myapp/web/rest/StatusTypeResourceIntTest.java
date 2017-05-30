package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.MyappApp;

import com.mycompany.myapp.domain.StatusType;
import com.mycompany.myapp.repository.StatusTypeRepository;
import com.mycompany.myapp.repository.search.StatusTypeSearchRepository;
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
 * Test class for the StatusTypeResource REST controller.
 *
 * @see StatusTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MyappApp.class)
public class StatusTypeResourceIntTest {

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    @Autowired
    private StatusTypeRepository statusTypeRepository;

    @Autowired
    private StatusTypeSearchRepository statusTypeSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restStatusTypeMockMvc;

    private StatusType statusType;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StatusTypeResource statusTypeResource = new StatusTypeResource(statusTypeRepository, statusTypeSearchRepository);
        this.restStatusTypeMockMvc = MockMvcBuilders.standaloneSetup(statusTypeResource)
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
    public static StatusType createEntity(EntityManager em) {
        StatusType statusType = new StatusType()
            .value(DEFAULT_VALUE);
        return statusType;
    }

    @Before
    public void initTest() {
        statusTypeSearchRepository.deleteAll();
        statusType = createEntity(em);
    }

    @Test
    @Transactional
    public void createStatusType() throws Exception {
        int databaseSizeBeforeCreate = statusTypeRepository.findAll().size();

        // Create the StatusType
        restStatusTypeMockMvc.perform(post("/api/status-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(statusType)))
            .andExpect(status().isCreated());

        // Validate the StatusType in the database
        List<StatusType> statusTypeList = statusTypeRepository.findAll();
        assertThat(statusTypeList).hasSize(databaseSizeBeforeCreate + 1);
        StatusType testStatusType = statusTypeList.get(statusTypeList.size() - 1);
        assertThat(testStatusType.getValue()).isEqualTo(DEFAULT_VALUE);

        // Validate the StatusType in Elasticsearch
        StatusType statusTypeEs = statusTypeSearchRepository.findOne(testStatusType.getId());
        assertThat(statusTypeEs).isEqualToComparingFieldByField(testStatusType);
    }

    @Test
    @Transactional
    public void createStatusTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = statusTypeRepository.findAll().size();

        // Create the StatusType with an existing ID
        statusType.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStatusTypeMockMvc.perform(post("/api/status-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(statusType)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<StatusType> statusTypeList = statusTypeRepository.findAll();
        assertThat(statusTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = statusTypeRepository.findAll().size();
        // set the field null
        statusType.setValue(null);

        // Create the StatusType, which fails.

        restStatusTypeMockMvc.perform(post("/api/status-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(statusType)))
            .andExpect(status().isBadRequest());

        List<StatusType> statusTypeList = statusTypeRepository.findAll();
        assertThat(statusTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStatusTypes() throws Exception {
        // Initialize the database
        statusTypeRepository.saveAndFlush(statusType);

        // Get all the statusTypeList
        restStatusTypeMockMvc.perform(get("/api/status-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(statusType.getId().intValue())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.toString())));
    }

    @Test
    @Transactional
    public void getStatusType() throws Exception {
        // Initialize the database
        statusTypeRepository.saveAndFlush(statusType);

        // Get the statusType
        restStatusTypeMockMvc.perform(get("/api/status-types/{id}", statusType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(statusType.getId().intValue()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingStatusType() throws Exception {
        // Get the statusType
        restStatusTypeMockMvc.perform(get("/api/status-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStatusType() throws Exception {
        // Initialize the database
        statusTypeRepository.saveAndFlush(statusType);
        statusTypeSearchRepository.save(statusType);
        int databaseSizeBeforeUpdate = statusTypeRepository.findAll().size();

        // Update the statusType
        StatusType updatedStatusType = statusTypeRepository.findOne(statusType.getId());
        updatedStatusType
            .value(UPDATED_VALUE);

        restStatusTypeMockMvc.perform(put("/api/status-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedStatusType)))
            .andExpect(status().isOk());

        // Validate the StatusType in the database
        List<StatusType> statusTypeList = statusTypeRepository.findAll();
        assertThat(statusTypeList).hasSize(databaseSizeBeforeUpdate);
        StatusType testStatusType = statusTypeList.get(statusTypeList.size() - 1);
        assertThat(testStatusType.getValue()).isEqualTo(UPDATED_VALUE);

        // Validate the StatusType in Elasticsearch
        StatusType statusTypeEs = statusTypeSearchRepository.findOne(testStatusType.getId());
        assertThat(statusTypeEs).isEqualToComparingFieldByField(testStatusType);
    }

    @Test
    @Transactional
    public void updateNonExistingStatusType() throws Exception {
        int databaseSizeBeforeUpdate = statusTypeRepository.findAll().size();

        // Create the StatusType

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restStatusTypeMockMvc.perform(put("/api/status-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(statusType)))
            .andExpect(status().isCreated());

        // Validate the StatusType in the database
        List<StatusType> statusTypeList = statusTypeRepository.findAll();
        assertThat(statusTypeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteStatusType() throws Exception {
        // Initialize the database
        statusTypeRepository.saveAndFlush(statusType);
        statusTypeSearchRepository.save(statusType);
        int databaseSizeBeforeDelete = statusTypeRepository.findAll().size();

        // Get the statusType
        restStatusTypeMockMvc.perform(delete("/api/status-types/{id}", statusType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean statusTypeExistsInEs = statusTypeSearchRepository.exists(statusType.getId());
        assertThat(statusTypeExistsInEs).isFalse();

        // Validate the database is empty
        List<StatusType> statusTypeList = statusTypeRepository.findAll();
        assertThat(statusTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchStatusType() throws Exception {
        // Initialize the database
        statusTypeRepository.saveAndFlush(statusType);
        statusTypeSearchRepository.save(statusType);

        // Search the statusType
        restStatusTypeMockMvc.perform(get("/api/_search/status-types?query=id:" + statusType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(statusType.getId().intValue())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StatusType.class);
        StatusType statusType1 = new StatusType();
        statusType1.setId(1L);
        StatusType statusType2 = new StatusType();
        statusType2.setId(statusType1.getId());
        assertThat(statusType1).isEqualTo(statusType2);
        statusType2.setId(2L);
        assertThat(statusType1).isNotEqualTo(statusType2);
        statusType1.setId(null);
        assertThat(statusType1).isNotEqualTo(statusType2);
    }
}
