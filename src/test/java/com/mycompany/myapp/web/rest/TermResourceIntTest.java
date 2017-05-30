package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.MyappApp;

import com.mycompany.myapp.domain.Term;
import com.mycompany.myapp.repository.TermRepository;
import com.mycompany.myapp.repository.search.TermSearchRepository;
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
 * Test class for the TermResource REST controller.
 *
 * @see TermResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MyappApp.class)
public class TermResourceIntTest {

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    @Autowired
    private TermRepository termRepository;

    @Autowired
    private TermSearchRepository termSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTermMockMvc;

    private Term term;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TermResource termResource = new TermResource(termRepository, termSearchRepository);
        this.restTermMockMvc = MockMvcBuilders.standaloneSetup(termResource)
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
    public static Term createEntity(EntityManager em) {
        Term term = new Term()
            .value(DEFAULT_VALUE);
        return term;
    }

    @Before
    public void initTest() {
        termSearchRepository.deleteAll();
        term = createEntity(em);
    }

    @Test
    @Transactional
    public void createTerm() throws Exception {
        int databaseSizeBeforeCreate = termRepository.findAll().size();

        // Create the Term
        restTermMockMvc.perform(post("/api/terms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(term)))
            .andExpect(status().isCreated());

        // Validate the Term in the database
        List<Term> termList = termRepository.findAll();
        assertThat(termList).hasSize(databaseSizeBeforeCreate + 1);
        Term testTerm = termList.get(termList.size() - 1);
        assertThat(testTerm.getValue()).isEqualTo(DEFAULT_VALUE);

        // Validate the Term in Elasticsearch
        Term termEs = termSearchRepository.findOne(testTerm.getId());
        assertThat(termEs).isEqualToComparingFieldByField(testTerm);
    }

    @Test
    @Transactional
    public void createTermWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = termRepository.findAll().size();

        // Create the Term with an existing ID
        term.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTermMockMvc.perform(post("/api/terms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(term)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Term> termList = termRepository.findAll();
        assertThat(termList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = termRepository.findAll().size();
        // set the field null
        term.setValue(null);

        // Create the Term, which fails.

        restTermMockMvc.perform(post("/api/terms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(term)))
            .andExpect(status().isBadRequest());

        List<Term> termList = termRepository.findAll();
        assertThat(termList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTerms() throws Exception {
        // Initialize the database
        termRepository.saveAndFlush(term);

        // Get all the termList
        restTermMockMvc.perform(get("/api/terms?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(term.getId().intValue())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.toString())));
    }

    @Test
    @Transactional
    public void getTerm() throws Exception {
        // Initialize the database
        termRepository.saveAndFlush(term);

        // Get the term
        restTermMockMvc.perform(get("/api/terms/{id}", term.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(term.getId().intValue()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTerm() throws Exception {
        // Get the term
        restTermMockMvc.perform(get("/api/terms/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTerm() throws Exception {
        // Initialize the database
        termRepository.saveAndFlush(term);
        termSearchRepository.save(term);
        int databaseSizeBeforeUpdate = termRepository.findAll().size();

        // Update the term
        Term updatedTerm = termRepository.findOne(term.getId());
        updatedTerm
            .value(UPDATED_VALUE);

        restTermMockMvc.perform(put("/api/terms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTerm)))
            .andExpect(status().isOk());

        // Validate the Term in the database
        List<Term> termList = termRepository.findAll();
        assertThat(termList).hasSize(databaseSizeBeforeUpdate);
        Term testTerm = termList.get(termList.size() - 1);
        assertThat(testTerm.getValue()).isEqualTo(UPDATED_VALUE);

        // Validate the Term in Elasticsearch
        Term termEs = termSearchRepository.findOne(testTerm.getId());
        assertThat(termEs).isEqualToComparingFieldByField(testTerm);
    }

    @Test
    @Transactional
    public void updateNonExistingTerm() throws Exception {
        int databaseSizeBeforeUpdate = termRepository.findAll().size();

        // Create the Term

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTermMockMvc.perform(put("/api/terms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(term)))
            .andExpect(status().isCreated());

        // Validate the Term in the database
        List<Term> termList = termRepository.findAll();
        assertThat(termList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTerm() throws Exception {
        // Initialize the database
        termRepository.saveAndFlush(term);
        termSearchRepository.save(term);
        int databaseSizeBeforeDelete = termRepository.findAll().size();

        // Get the term
        restTermMockMvc.perform(delete("/api/terms/{id}", term.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean termExistsInEs = termSearchRepository.exists(term.getId());
        assertThat(termExistsInEs).isFalse();

        // Validate the database is empty
        List<Term> termList = termRepository.findAll();
        assertThat(termList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTerm() throws Exception {
        // Initialize the database
        termRepository.saveAndFlush(term);
        termSearchRepository.save(term);

        // Search the term
        restTermMockMvc.perform(get("/api/_search/terms?query=id:" + term.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(term.getId().intValue())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Term.class);
        Term term1 = new Term();
        term1.setId(1L);
        Term term2 = new Term();
        term2.setId(term1.getId());
        assertThat(term1).isEqualTo(term2);
        term2.setId(2L);
        assertThat(term1).isNotEqualTo(term2);
        term1.setId(null);
        assertThat(term1).isNotEqualTo(term2);
    }
}
