package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.MyappApp;

import com.mycompany.myapp.domain.Offer;
import com.mycompany.myapp.domain.StatusType;
import com.mycompany.myapp.domain.Program;
import com.mycompany.myapp.repository.OfferRepository;
import com.mycompany.myapp.repository.search.OfferSearchRepository;
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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.mycompany.myapp.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the OfferResource REST controller.
 *
 * @see OfferResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MyappApp.class)
public class OfferResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_VOLUNTEER_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_VOLUNTEER_TYPE = "BBBBBBBBBB";

    private static final Integer DEFAULT_INITIAL_NO_VACANCIES = 1;
    private static final Integer UPDATED_INITIAL_NO_VACANCIES = 2;

    private static final Integer DEFAULT_ACTUAL_NO_VACANCIES = 1;
    private static final Integer UPDATED_ACTUAL_NO_VACANCIES = 2;

    private static final ZonedDateTime DEFAULT_DATE_FROM = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_FROM = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_DATE_TO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_TO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Integer DEFAULT_WORKHOURS_PER_MONTH = 1;
    private static final Integer UPDATED_WORKHOURS_PER_MONTH = 2;

    private static final String DEFAULT_DAYTIME = "AAAAAAAAAA";
    private static final String UPDATED_DAYTIME = "BBBBBBBBBB";

    private static final String DEFAULT_WORKHOURS = "AAAAAAAAAA";
    private static final String UPDATED_WORKHOURS = "BBBBBBBBBB";

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private OfferSearchRepository offerSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restOfferMockMvc;

    private Offer offer;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        OfferResource offerResource = new OfferResource(offerRepository, offerSearchRepository);
        this.restOfferMockMvc = MockMvcBuilders.standaloneSetup(offerResource)
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
    public static Offer createEntity(EntityManager em) {
        Offer offer = new Offer()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .volunteerType(DEFAULT_VOLUNTEER_TYPE)
            .initialNoVacancies(DEFAULT_INITIAL_NO_VACANCIES)
            .actualNoVacancies(DEFAULT_ACTUAL_NO_VACANCIES)
            .dateFrom(DEFAULT_DATE_FROM)
            .dateTo(DEFAULT_DATE_TO)
            .workhoursPerMonth(DEFAULT_WORKHOURS_PER_MONTH)
            .daytime(DEFAULT_DAYTIME)
            .workhours(DEFAULT_WORKHOURS);
        // Add required entity
        StatusType statusType = StatusTypeResourceIntTest.createEntity(em);
        em.persist(statusType);
        em.flush();
        offer.setStatusType(statusType);
        // Add required entity
        Program program = ProgramResourceIntTest.createEntity(em);
        em.persist(program);
        em.flush();
        offer.setProgram(program);
        return offer;
    }

    @Before
    public void initTest() {
        offerSearchRepository.deleteAll();
        offer = createEntity(em);
    }

    @Test
    @Transactional
    public void createOffer() throws Exception {
        int databaseSizeBeforeCreate = offerRepository.findAll().size();

        // Create the Offer
        restOfferMockMvc.perform(post("/api/offers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(offer)))
            .andExpect(status().isCreated());

        // Validate the Offer in the database
        List<Offer> offerList = offerRepository.findAll();
        assertThat(offerList).hasSize(databaseSizeBeforeCreate + 1);
        Offer testOffer = offerList.get(offerList.size() - 1);
        assertThat(testOffer.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testOffer.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testOffer.getVolunteerType()).isEqualTo(DEFAULT_VOLUNTEER_TYPE);
        assertThat(testOffer.getInitialNoVacancies()).isEqualTo(DEFAULT_INITIAL_NO_VACANCIES);
        assertThat(testOffer.getActualNoVacancies()).isEqualTo(DEFAULT_ACTUAL_NO_VACANCIES);
        assertThat(testOffer.getDateFrom()).isEqualTo(DEFAULT_DATE_FROM);
        assertThat(testOffer.getDateTo()).isEqualTo(DEFAULT_DATE_TO);
        assertThat(testOffer.getWorkhoursPerMonth()).isEqualTo(DEFAULT_WORKHOURS_PER_MONTH);
        assertThat(testOffer.getDaytime()).isEqualTo(DEFAULT_DAYTIME);
        assertThat(testOffer.getWorkhours()).isEqualTo(DEFAULT_WORKHOURS);

        // Validate the Offer in Elasticsearch
        Offer offerEs = offerSearchRepository.findOne(testOffer.getId());
        assertThat(offerEs).isEqualToComparingFieldByField(testOffer);
    }

    @Test
    @Transactional
    public void createOfferWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = offerRepository.findAll().size();

        // Create the Offer with an existing ID
        offer.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOfferMockMvc.perform(post("/api/offers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(offer)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Offer> offerList = offerRepository.findAll();
        assertThat(offerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = offerRepository.findAll().size();
        // set the field null
        offer.setName(null);

        // Create the Offer, which fails.

        restOfferMockMvc.perform(post("/api/offers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(offer)))
            .andExpect(status().isBadRequest());

        List<Offer> offerList = offerRepository.findAll();
        assertThat(offerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateFromIsRequired() throws Exception {
        int databaseSizeBeforeTest = offerRepository.findAll().size();
        // set the field null
        offer.setDateFrom(null);

        // Create the Offer, which fails.

        restOfferMockMvc.perform(post("/api/offers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(offer)))
            .andExpect(status().isBadRequest());

        List<Offer> offerList = offerRepository.findAll();
        assertThat(offerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateToIsRequired() throws Exception {
        int databaseSizeBeforeTest = offerRepository.findAll().size();
        // set the field null
        offer.setDateTo(null);

        // Create the Offer, which fails.

        restOfferMockMvc.perform(post("/api/offers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(offer)))
            .andExpect(status().isBadRequest());

        List<Offer> offerList = offerRepository.findAll();
        assertThat(offerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDaytimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = offerRepository.findAll().size();
        // set the field null
        offer.setDaytime(null);

        // Create the Offer, which fails.

        restOfferMockMvc.perform(post("/api/offers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(offer)))
            .andExpect(status().isBadRequest());

        List<Offer> offerList = offerRepository.findAll();
        assertThat(offerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllOffers() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get all the offerList
        restOfferMockMvc.perform(get("/api/offers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(offer.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].volunteerType").value(hasItem(DEFAULT_VOLUNTEER_TYPE.toString())))
            .andExpect(jsonPath("$.[*].initialNoVacancies").value(hasItem(DEFAULT_INITIAL_NO_VACANCIES)))
            .andExpect(jsonPath("$.[*].actualNoVacancies").value(hasItem(DEFAULT_ACTUAL_NO_VACANCIES)))
            .andExpect(jsonPath("$.[*].dateFrom").value(hasItem(sameInstant(DEFAULT_DATE_FROM))))
            .andExpect(jsonPath("$.[*].dateTo").value(hasItem(sameInstant(DEFAULT_DATE_TO))))
            .andExpect(jsonPath("$.[*].workhoursPerMonth").value(hasItem(DEFAULT_WORKHOURS_PER_MONTH)))
            .andExpect(jsonPath("$.[*].daytime").value(hasItem(DEFAULT_DAYTIME.toString())))
            .andExpect(jsonPath("$.[*].workhours").value(hasItem(DEFAULT_WORKHOURS.toString())));
    }

    @Test
    @Transactional
    public void getOffer() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);

        // Get the offer
        restOfferMockMvc.perform(get("/api/offers/{id}", offer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(offer.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.volunteerType").value(DEFAULT_VOLUNTEER_TYPE.toString()))
            .andExpect(jsonPath("$.initialNoVacancies").value(DEFAULT_INITIAL_NO_VACANCIES))
            .andExpect(jsonPath("$.actualNoVacancies").value(DEFAULT_ACTUAL_NO_VACANCIES))
            .andExpect(jsonPath("$.dateFrom").value(sameInstant(DEFAULT_DATE_FROM)))
            .andExpect(jsonPath("$.dateTo").value(sameInstant(DEFAULT_DATE_TO)))
            .andExpect(jsonPath("$.workhoursPerMonth").value(DEFAULT_WORKHOURS_PER_MONTH))
            .andExpect(jsonPath("$.daytime").value(DEFAULT_DAYTIME.toString()))
            .andExpect(jsonPath("$.workhours").value(DEFAULT_WORKHOURS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingOffer() throws Exception {
        // Get the offer
        restOfferMockMvc.perform(get("/api/offers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOffer() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);
        offerSearchRepository.save(offer);
        int databaseSizeBeforeUpdate = offerRepository.findAll().size();

        // Update the offer
        Offer updatedOffer = offerRepository.findOne(offer.getId());
        updatedOffer
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .volunteerType(UPDATED_VOLUNTEER_TYPE)
            .initialNoVacancies(UPDATED_INITIAL_NO_VACANCIES)
            .actualNoVacancies(UPDATED_ACTUAL_NO_VACANCIES)
            .dateFrom(UPDATED_DATE_FROM)
            .dateTo(UPDATED_DATE_TO)
            .workhoursPerMonth(UPDATED_WORKHOURS_PER_MONTH)
            .daytime(UPDATED_DAYTIME)
            .workhours(UPDATED_WORKHOURS);

        restOfferMockMvc.perform(put("/api/offers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedOffer)))
            .andExpect(status().isOk());

        // Validate the Offer in the database
        List<Offer> offerList = offerRepository.findAll();
        assertThat(offerList).hasSize(databaseSizeBeforeUpdate);
        Offer testOffer = offerList.get(offerList.size() - 1);
        assertThat(testOffer.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOffer.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testOffer.getVolunteerType()).isEqualTo(UPDATED_VOLUNTEER_TYPE);
        assertThat(testOffer.getInitialNoVacancies()).isEqualTo(UPDATED_INITIAL_NO_VACANCIES);
        assertThat(testOffer.getActualNoVacancies()).isEqualTo(UPDATED_ACTUAL_NO_VACANCIES);
        assertThat(testOffer.getDateFrom()).isEqualTo(UPDATED_DATE_FROM);
        assertThat(testOffer.getDateTo()).isEqualTo(UPDATED_DATE_TO);
        assertThat(testOffer.getWorkhoursPerMonth()).isEqualTo(UPDATED_WORKHOURS_PER_MONTH);
        assertThat(testOffer.getDaytime()).isEqualTo(UPDATED_DAYTIME);
        assertThat(testOffer.getWorkhours()).isEqualTo(UPDATED_WORKHOURS);

        // Validate the Offer in Elasticsearch
        Offer offerEs = offerSearchRepository.findOne(testOffer.getId());
        assertThat(offerEs).isEqualToComparingFieldByField(testOffer);
    }

    @Test
    @Transactional
    public void updateNonExistingOffer() throws Exception {
        int databaseSizeBeforeUpdate = offerRepository.findAll().size();

        // Create the Offer

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restOfferMockMvc.perform(put("/api/offers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(offer)))
            .andExpect(status().isCreated());

        // Validate the Offer in the database
        List<Offer> offerList = offerRepository.findAll();
        assertThat(offerList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteOffer() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);
        offerSearchRepository.save(offer);
        int databaseSizeBeforeDelete = offerRepository.findAll().size();

        // Get the offer
        restOfferMockMvc.perform(delete("/api/offers/{id}", offer.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean offerExistsInEs = offerSearchRepository.exists(offer.getId());
        assertThat(offerExistsInEs).isFalse();

        // Validate the database is empty
        List<Offer> offerList = offerRepository.findAll();
        assertThat(offerList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchOffer() throws Exception {
        // Initialize the database
        offerRepository.saveAndFlush(offer);
        offerSearchRepository.save(offer);

        // Search the offer
        restOfferMockMvc.perform(get("/api/_search/offers?query=id:" + offer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(offer.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].volunteerType").value(hasItem(DEFAULT_VOLUNTEER_TYPE.toString())))
            .andExpect(jsonPath("$.[*].initialNoVacancies").value(hasItem(DEFAULT_INITIAL_NO_VACANCIES)))
            .andExpect(jsonPath("$.[*].actualNoVacancies").value(hasItem(DEFAULT_ACTUAL_NO_VACANCIES)))
            .andExpect(jsonPath("$.[*].dateFrom").value(hasItem(sameInstant(DEFAULT_DATE_FROM))))
            .andExpect(jsonPath("$.[*].dateTo").value(hasItem(sameInstant(DEFAULT_DATE_TO))))
            .andExpect(jsonPath("$.[*].workhoursPerMonth").value(hasItem(DEFAULT_WORKHOURS_PER_MONTH)))
            .andExpect(jsonPath("$.[*].daytime").value(hasItem(DEFAULT_DAYTIME.toString())))
            .andExpect(jsonPath("$.[*].workhours").value(hasItem(DEFAULT_WORKHOURS.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Offer.class);
        Offer offer1 = new Offer();
        offer1.setId(1L);
        Offer offer2 = new Offer();
        offer2.setId(offer1.getId());
        assertThat(offer1).isEqualTo(offer2);
        offer2.setId(2L);
        assertThat(offer1).isNotEqualTo(offer2);
        offer1.setId(null);
        assertThat(offer1).isNotEqualTo(offer2);
    }
}
