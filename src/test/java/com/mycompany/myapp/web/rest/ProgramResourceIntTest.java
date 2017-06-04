package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.MyappApp;

import com.mycompany.myapp.domain.Program;
import com.mycompany.myapp.domain.Provider;
import com.mycompany.myapp.domain.StatusType;
import com.mycompany.myapp.repository.ProgramRepository;
import com.mycompany.myapp.repository.search.ProgramSearchRepository;
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
 * Test class for the ProgramResource REST controller.
 *
 * @see ProgramResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MyappApp.class)
public class ProgramResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_HIGHLIGHT = "AAAAAAAAAA";
    private static final String UPDATED_HIGHLIGHT = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATE_TO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_TO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_DATE_FROM = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_FROM = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_SHARE_PROGRAM = "AAAAAAAAAA";
    private static final String UPDATED_SHARE_PROGRAM = "BBBBBBBBBB";

    @Autowired
    private ProgramRepository programRepository;

    @Autowired
    private ProgramSearchRepository programSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restProgramMockMvc;

    private Program program;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ProgramResource programResource = new ProgramResource(programRepository, programSearchRepository);
        this.restProgramMockMvc = MockMvcBuilders.standaloneSetup(programResource)
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
    public static Program createEntity(EntityManager em) {
        Program program = new Program()
            .name(DEFAULT_NAME)
            .highlight(DEFAULT_HIGHLIGHT)
            .dateTo(DEFAULT_DATE_TO)
            .dateFrom(DEFAULT_DATE_FROM)
            .shareProgram(DEFAULT_SHARE_PROGRAM);
        // Add required entity
        Provider provider = ProviderResourceIntTest.createEntity(em);
        em.persist(provider);
        em.flush();
        program.setProvider(provider);
        // Add required entity
        StatusType statusType = StatusTypeResourceIntTest.createEntity(em);
        em.persist(statusType);
        em.flush();
        program.setStatusType(statusType);
        return program;
    }

    @Before
    public void initTest() {
        programSearchRepository.deleteAll();
        program = createEntity(em);
    }

    @Test
    @Transactional
    public void createProgram() throws Exception {
        int databaseSizeBeforeCreate = programRepository.findAll().size();

        // Create the Program
        restProgramMockMvc.perform(post("/api/programs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(program)))
            .andExpect(status().isCreated());

        // Validate the Program in the database
        List<Program> programList = programRepository.findAll();
        assertThat(programList).hasSize(databaseSizeBeforeCreate + 1);
        Program testProgram = programList.get(programList.size() - 1);
        assertThat(testProgram.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProgram.getHighlight()).isEqualTo(DEFAULT_HIGHLIGHT);
        assertThat(testProgram.getDateTo()).isEqualTo(DEFAULT_DATE_TO);
        assertThat(testProgram.getDateFrom()).isEqualTo(DEFAULT_DATE_FROM);
        assertThat(testProgram.getShareProgram()).isEqualTo(DEFAULT_SHARE_PROGRAM);

        // Validate the Program in Elasticsearch
        Program programEs = programSearchRepository.findOne(testProgram.getId());
        assertThat(programEs).isEqualToComparingFieldByField(testProgram);
    }

    @Test
    @Transactional
    public void createProgramWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = programRepository.findAll().size();

        // Create the Program with an existing ID
        program.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProgramMockMvc.perform(post("/api/programs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(program)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Program> programList = programRepository.findAll();
        assertThat(programList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDateToIsRequired() throws Exception {
        int databaseSizeBeforeTest = programRepository.findAll().size();
        // set the field null
        program.setDateTo(null);

        // Create the Program, which fails.

        restProgramMockMvc.perform(post("/api/programs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(program)))
            .andExpect(status().isBadRequest());

        List<Program> programList = programRepository.findAll();
        assertThat(programList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateFromIsRequired() throws Exception {
        int databaseSizeBeforeTest = programRepository.findAll().size();
        // set the field null
        program.setDateFrom(null);

        // Create the Program, which fails.

        restProgramMockMvc.perform(post("/api/programs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(program)))
            .andExpect(status().isBadRequest());

        List<Program> programList = programRepository.findAll();
        assertThat(programList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPrograms() throws Exception {
        // Initialize the database
        programRepository.saveAndFlush(program);

        // Get all the programList
        restProgramMockMvc.perform(get("/api/programs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(program.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].highlight").value(hasItem(DEFAULT_HIGHLIGHT.toString())))
            .andExpect(jsonPath("$.[*].dateTo").value(hasItem(sameInstant(DEFAULT_DATE_TO))))
            .andExpect(jsonPath("$.[*].dateFrom").value(hasItem(sameInstant(DEFAULT_DATE_FROM))))
            .andExpect(jsonPath("$.[*].shareProgram").value(hasItem(DEFAULT_SHARE_PROGRAM.toString())));
    }

    @Test
    @Transactional
    public void getProgram() throws Exception {
        // Initialize the database
        programRepository.saveAndFlush(program);

        // Get the program
        restProgramMockMvc.perform(get("/api/programs/{id}", program.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(program.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.highlight").value(DEFAULT_HIGHLIGHT.toString()))
            .andExpect(jsonPath("$.dateTo").value(sameInstant(DEFAULT_DATE_TO)))
            .andExpect(jsonPath("$.dateFrom").value(sameInstant(DEFAULT_DATE_FROM)))
            .andExpect(jsonPath("$.shareProgram").value(DEFAULT_SHARE_PROGRAM.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingProgram() throws Exception {
        // Get the program
        restProgramMockMvc.perform(get("/api/programs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProgram() throws Exception {
        // Initialize the database
        programRepository.saveAndFlush(program);
        programSearchRepository.save(program);
        int databaseSizeBeforeUpdate = programRepository.findAll().size();

        // Update the program
        Program updatedProgram = programRepository.findOne(program.getId());
        updatedProgram
            .name(UPDATED_NAME)
            .highlight(UPDATED_HIGHLIGHT)
            .dateTo(UPDATED_DATE_TO)
            .dateFrom(UPDATED_DATE_FROM)
            .shareProgram(UPDATED_SHARE_PROGRAM);

        restProgramMockMvc.perform(put("/api/programs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedProgram)))
            .andExpect(status().isOk());

        // Validate the Program in the database
        List<Program> programList = programRepository.findAll();
        assertThat(programList).hasSize(databaseSizeBeforeUpdate);
        Program testProgram = programList.get(programList.size() - 1);
        assertThat(testProgram.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProgram.getHighlight()).isEqualTo(UPDATED_HIGHLIGHT);
        assertThat(testProgram.getDateTo()).isEqualTo(UPDATED_DATE_TO);
        assertThat(testProgram.getDateFrom()).isEqualTo(UPDATED_DATE_FROM);
        assertThat(testProgram.getShareProgram()).isEqualTo(UPDATED_SHARE_PROGRAM);

        // Validate the Program in Elasticsearch
        Program programEs = programSearchRepository.findOne(testProgram.getId());
        assertThat(programEs).isEqualToComparingFieldByField(testProgram);
    }

    @Test
    @Transactional
    public void updateNonExistingProgram() throws Exception {
        int databaseSizeBeforeUpdate = programRepository.findAll().size();

        // Create the Program

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restProgramMockMvc.perform(put("/api/programs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(program)))
            .andExpect(status().isCreated());

        // Validate the Program in the database
        List<Program> programList = programRepository.findAll();
        assertThat(programList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteProgram() throws Exception {
        // Initialize the database
        programRepository.saveAndFlush(program);
        programSearchRepository.save(program);
        int databaseSizeBeforeDelete = programRepository.findAll().size();

        // Get the program
        restProgramMockMvc.perform(delete("/api/programs/{id}", program.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean programExistsInEs = programSearchRepository.exists(program.getId());
        assertThat(programExistsInEs).isFalse();

        // Validate the database is empty
        List<Program> programList = programRepository.findAll();
        assertThat(programList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchProgram() throws Exception {
        // Initialize the database
        programRepository.saveAndFlush(program);
        programSearchRepository.save(program);

        // Search the program
        restProgramMockMvc.perform(get("/api/_search/programs?query=id:" + program.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(program.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].highlight").value(hasItem(DEFAULT_HIGHLIGHT.toString())))
            .andExpect(jsonPath("$.[*].dateTo").value(hasItem(sameInstant(DEFAULT_DATE_TO))))
            .andExpect(jsonPath("$.[*].dateFrom").value(hasItem(sameInstant(DEFAULT_DATE_FROM))))
            .andExpect(jsonPath("$.[*].shareProgram").value(hasItem(DEFAULT_SHARE_PROGRAM.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Program.class);
        Program program1 = new Program();
        program1.setId(1L);
        Program program2 = new Program();
        program2.setId(program1.getId());
        assertThat(program1).isEqualTo(program2);
        program2.setId(2L);
        assertThat(program1).isNotEqualTo(program2);
        program1.setId(null);
        assertThat(program1).isNotEqualTo(program2);
    }
}
