package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.MyappApp;

import com.mycompany.myapp.domain.OfferDetails;
import com.mycompany.myapp.domain.Offer;
import com.mycompany.myapp.repository.OfferDetailsRepository;
import com.mycompany.myapp.repository.search.OfferDetailsSearchRepository;
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
 * Test class for the OfferDetailsResource REST controller.
 *
 * @see OfferDetailsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MyappApp.class)
public class OfferDetailsResourceIntTest {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_IS_VALID = "A";
    private static final String UPDATED_IS_VALID = "B";

    @Autowired
    private OfferDetailsRepository offerDetailsRepository;

    @Autowired
    private OfferDetailsSearchRepository offerDetailsSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restOfferDetailsMockMvc;

    private OfferDetails offerDetails;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        OfferDetailsResource offerDetailsResource = new OfferDetailsResource(offerDetailsRepository, offerDetailsSearchRepository);
        this.restOfferDetailsMockMvc = MockMvcBuilders.standaloneSetup(offerDetailsResource)
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
    public static OfferDetails createEntity(EntityManager em) {
        OfferDetails offerDetails = new OfferDetails()
            .description(DEFAULT_DESCRIPTION)
            .isValid(DEFAULT_IS_VALID);
        // Add required entity
        Offer offer = OfferResourceIntTest.createEntity(em);
        em.persist(offer);
        em.flush();
        offerDetails.setOffer(offer);
        return offerDetails;
    }

    @Before
    public void initTest() {
        offerDetailsSearchRepository.deleteAll();
        offerDetails = createEntity(em);
    }

    @Test
    @Transactional
    public void createOfferDetails() throws Exception {
        int databaseSizeBeforeCreate = offerDetailsRepository.findAll().size();

        // Create the OfferDetails
        restOfferDetailsMockMvc.perform(post("/api/offer-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(offerDetails)))
            .andExpect(status().isCreated());

        // Validate the OfferDetails in the database
        List<OfferDetails> offerDetailsList = offerDetailsRepository.findAll();
        assertThat(offerDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        OfferDetails testOfferDetails = offerDetailsList.get(offerDetailsList.size() - 1);
        assertThat(testOfferDetails.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testOfferDetails.getIsValid()).isEqualTo(DEFAULT_IS_VALID);

        // Validate the OfferDetails in Elasticsearch
        OfferDetails offerDetailsEs = offerDetailsSearchRepository.findOne(testOfferDetails.getId());
        assertThat(offerDetailsEs).isEqualToComparingFieldByField(testOfferDetails);
    }

    @Test
    @Transactional
    public void createOfferDetailsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = offerDetailsRepository.findAll().size();

        // Create the OfferDetails with an existing ID
        offerDetails.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOfferDetailsMockMvc.perform(post("/api/offer-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(offerDetails)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<OfferDetails> offerDetailsList = offerDetailsRepository.findAll();
        assertThat(offerDetailsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkIsValidIsRequired() throws Exception {
        int databaseSizeBeforeTest = offerDetailsRepository.findAll().size();
        // set the field null
        offerDetails.setIsValid(null);

        // Create the OfferDetails, which fails.

        restOfferDetailsMockMvc.perform(post("/api/offer-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(offerDetails)))
            .andExpect(status().isBadRequest());

        List<OfferDetails> offerDetailsList = offerDetailsRepository.findAll();
        assertThat(offerDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllOfferDetails() throws Exception {
        // Initialize the database
        offerDetailsRepository.saveAndFlush(offerDetails);

        // Get all the offerDetailsList
        restOfferDetailsMockMvc.perform(get("/api/offer-details?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(offerDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].isValid").value(hasItem(DEFAULT_IS_VALID.toString())));
    }

    @Test
    @Transactional
    public void getOfferDetails() throws Exception {
        // Initialize the database
        offerDetailsRepository.saveAndFlush(offerDetails);

        // Get the offerDetails
        restOfferDetailsMockMvc.perform(get("/api/offer-details/{id}", offerDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(offerDetails.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.isValid").value(DEFAULT_IS_VALID.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingOfferDetails() throws Exception {
        // Get the offerDetails
        restOfferDetailsMockMvc.perform(get("/api/offer-details/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOfferDetails() throws Exception {
        // Initialize the database
        offerDetailsRepository.saveAndFlush(offerDetails);
        offerDetailsSearchRepository.save(offerDetails);
        int databaseSizeBeforeUpdate = offerDetailsRepository.findAll().size();

        // Update the offerDetails
        OfferDetails updatedOfferDetails = offerDetailsRepository.findOne(offerDetails.getId());
        updatedOfferDetails
            .description(UPDATED_DESCRIPTION)
            .isValid(UPDATED_IS_VALID);

        restOfferDetailsMockMvc.perform(put("/api/offer-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedOfferDetails)))
            .andExpect(status().isOk());

        // Validate the OfferDetails in the database
        List<OfferDetails> offerDetailsList = offerDetailsRepository.findAll();
        assertThat(offerDetailsList).hasSize(databaseSizeBeforeUpdate);
        OfferDetails testOfferDetails = offerDetailsList.get(offerDetailsList.size() - 1);
        assertThat(testOfferDetails.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testOfferDetails.getIsValid()).isEqualTo(UPDATED_IS_VALID);

        // Validate the OfferDetails in Elasticsearch
        OfferDetails offerDetailsEs = offerDetailsSearchRepository.findOne(testOfferDetails.getId());
        assertThat(offerDetailsEs).isEqualToComparingFieldByField(testOfferDetails);
    }

    @Test
    @Transactional
    public void updateNonExistingOfferDetails() throws Exception {
        int databaseSizeBeforeUpdate = offerDetailsRepository.findAll().size();

        // Create the OfferDetails

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restOfferDetailsMockMvc.perform(put("/api/offer-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(offerDetails)))
            .andExpect(status().isCreated());

        // Validate the OfferDetails in the database
        List<OfferDetails> offerDetailsList = offerDetailsRepository.findAll();
        assertThat(offerDetailsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteOfferDetails() throws Exception {
        // Initialize the database
        offerDetailsRepository.saveAndFlush(offerDetails);
        offerDetailsSearchRepository.save(offerDetails);
        int databaseSizeBeforeDelete = offerDetailsRepository.findAll().size();

        // Get the offerDetails
        restOfferDetailsMockMvc.perform(delete("/api/offer-details/{id}", offerDetails.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean offerDetailsExistsInEs = offerDetailsSearchRepository.exists(offerDetails.getId());
        assertThat(offerDetailsExistsInEs).isFalse();

        // Validate the database is empty
        List<OfferDetails> offerDetailsList = offerDetailsRepository.findAll();
        assertThat(offerDetailsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchOfferDetails() throws Exception {
        // Initialize the database
        offerDetailsRepository.saveAndFlush(offerDetails);
        offerDetailsSearchRepository.save(offerDetails);

        // Search the offerDetails
        restOfferDetailsMockMvc.perform(get("/api/_search/offer-details?query=id:" + offerDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(offerDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].isValid").value(hasItem(DEFAULT_IS_VALID.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OfferDetails.class);
        OfferDetails offerDetails1 = new OfferDetails();
        offerDetails1.setId(1L);
        OfferDetails offerDetails2 = new OfferDetails();
        offerDetails2.setId(offerDetails1.getId());
        assertThat(offerDetails1).isEqualTo(offerDetails2);
        offerDetails2.setId(2L);
        assertThat(offerDetails1).isNotEqualTo(offerDetails2);
        offerDetails1.setId(null);
        assertThat(offerDetails1).isNotEqualTo(offerDetails2);
    }
}
