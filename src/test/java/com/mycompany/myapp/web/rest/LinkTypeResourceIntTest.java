package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.MyappApp;

import com.mycompany.myapp.domain.LinkType;
import com.mycompany.myapp.repository.LinkTypeRepository;
import com.mycompany.myapp.repository.search.LinkTypeSearchRepository;
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
 * Test class for the LinkTypeResource REST controller.
 *
 * @see LinkTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MyappApp.class)
public class LinkTypeResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LOGO_PATH = "AAAAAAAAAA";
    private static final String UPDATED_LOGO_PATH = "BBBBBBBBBB";

    @Autowired
    private LinkTypeRepository linkTypeRepository;

    @Autowired
    private LinkTypeSearchRepository linkTypeSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restLinkTypeMockMvc;

    private LinkType linkType;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LinkTypeResource linkTypeResource = new LinkTypeResource(linkTypeRepository, linkTypeSearchRepository);
        this.restLinkTypeMockMvc = MockMvcBuilders.standaloneSetup(linkTypeResource)
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
    public static LinkType createEntity(EntityManager em) {
        LinkType linkType = new LinkType()
            .name(DEFAULT_NAME)
            .logoPath(DEFAULT_LOGO_PATH);
        return linkType;
    }

    @Before
    public void initTest() {
        linkTypeSearchRepository.deleteAll();
        linkType = createEntity(em);
    }

    @Test
    @Transactional
    public void createLinkType() throws Exception {
        int databaseSizeBeforeCreate = linkTypeRepository.findAll().size();

        // Create the LinkType
        restLinkTypeMockMvc.perform(post("/api/link-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(linkType)))
            .andExpect(status().isCreated());

        // Validate the LinkType in the database
        List<LinkType> linkTypeList = linkTypeRepository.findAll();
        assertThat(linkTypeList).hasSize(databaseSizeBeforeCreate + 1);
        LinkType testLinkType = linkTypeList.get(linkTypeList.size() - 1);
        assertThat(testLinkType.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testLinkType.getLogoPath()).isEqualTo(DEFAULT_LOGO_PATH);

        // Validate the LinkType in Elasticsearch
        LinkType linkTypeEs = linkTypeSearchRepository.findOne(testLinkType.getId());
        assertThat(linkTypeEs).isEqualToComparingFieldByField(testLinkType);
    }

    @Test
    @Transactional
    public void createLinkTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = linkTypeRepository.findAll().size();

        // Create the LinkType with an existing ID
        linkType.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLinkTypeMockMvc.perform(post("/api/link-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(linkType)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<LinkType> linkTypeList = linkTypeRepository.findAll();
        assertThat(linkTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = linkTypeRepository.findAll().size();
        // set the field null
        linkType.setName(null);

        // Create the LinkType, which fails.

        restLinkTypeMockMvc.perform(post("/api/link-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(linkType)))
            .andExpect(status().isBadRequest());

        List<LinkType> linkTypeList = linkTypeRepository.findAll();
        assertThat(linkTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLogoPathIsRequired() throws Exception {
        int databaseSizeBeforeTest = linkTypeRepository.findAll().size();
        // set the field null
        linkType.setLogoPath(null);

        // Create the LinkType, which fails.

        restLinkTypeMockMvc.perform(post("/api/link-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(linkType)))
            .andExpect(status().isBadRequest());

        List<LinkType> linkTypeList = linkTypeRepository.findAll();
        assertThat(linkTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLinkTypes() throws Exception {
        // Initialize the database
        linkTypeRepository.saveAndFlush(linkType);

        // Get all the linkTypeList
        restLinkTypeMockMvc.perform(get("/api/link-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(linkType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].logoPath").value(hasItem(DEFAULT_LOGO_PATH.toString())));
    }

    @Test
    @Transactional
    public void getLinkType() throws Exception {
        // Initialize the database
        linkTypeRepository.saveAndFlush(linkType);

        // Get the linkType
        restLinkTypeMockMvc.perform(get("/api/link-types/{id}", linkType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(linkType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.logoPath").value(DEFAULT_LOGO_PATH.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingLinkType() throws Exception {
        // Get the linkType
        restLinkTypeMockMvc.perform(get("/api/link-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLinkType() throws Exception {
        // Initialize the database
        linkTypeRepository.saveAndFlush(linkType);
        linkTypeSearchRepository.save(linkType);
        int databaseSizeBeforeUpdate = linkTypeRepository.findAll().size();

        // Update the linkType
        LinkType updatedLinkType = linkTypeRepository.findOne(linkType.getId());
        updatedLinkType
            .name(UPDATED_NAME)
            .logoPath(UPDATED_LOGO_PATH);

        restLinkTypeMockMvc.perform(put("/api/link-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedLinkType)))
            .andExpect(status().isOk());

        // Validate the LinkType in the database
        List<LinkType> linkTypeList = linkTypeRepository.findAll();
        assertThat(linkTypeList).hasSize(databaseSizeBeforeUpdate);
        LinkType testLinkType = linkTypeList.get(linkTypeList.size() - 1);
        assertThat(testLinkType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testLinkType.getLogoPath()).isEqualTo(UPDATED_LOGO_PATH);

        // Validate the LinkType in Elasticsearch
        LinkType linkTypeEs = linkTypeSearchRepository.findOne(testLinkType.getId());
        assertThat(linkTypeEs).isEqualToComparingFieldByField(testLinkType);
    }

    @Test
    @Transactional
    public void updateNonExistingLinkType() throws Exception {
        int databaseSizeBeforeUpdate = linkTypeRepository.findAll().size();

        // Create the LinkType

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restLinkTypeMockMvc.perform(put("/api/link-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(linkType)))
            .andExpect(status().isCreated());

        // Validate the LinkType in the database
        List<LinkType> linkTypeList = linkTypeRepository.findAll();
        assertThat(linkTypeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteLinkType() throws Exception {
        // Initialize the database
        linkTypeRepository.saveAndFlush(linkType);
        linkTypeSearchRepository.save(linkType);
        int databaseSizeBeforeDelete = linkTypeRepository.findAll().size();

        // Get the linkType
        restLinkTypeMockMvc.perform(delete("/api/link-types/{id}", linkType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean linkTypeExistsInEs = linkTypeSearchRepository.exists(linkType.getId());
        assertThat(linkTypeExistsInEs).isFalse();

        // Validate the database is empty
        List<LinkType> linkTypeList = linkTypeRepository.findAll();
        assertThat(linkTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchLinkType() throws Exception {
        // Initialize the database
        linkTypeRepository.saveAndFlush(linkType);
        linkTypeSearchRepository.save(linkType);

        // Search the linkType
        restLinkTypeMockMvc.perform(get("/api/_search/link-types?query=id:" + linkType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(linkType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].logoPath").value(hasItem(DEFAULT_LOGO_PATH.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LinkType.class);
        LinkType linkType1 = new LinkType();
        linkType1.setId(1L);
        LinkType linkType2 = new LinkType();
        linkType2.setId(linkType1.getId());
        assertThat(linkType1).isEqualTo(linkType2);
        linkType2.setId(2L);
        assertThat(linkType1).isNotEqualTo(linkType2);
        linkType1.setId(null);
        assertThat(linkType1).isNotEqualTo(linkType2);
    }
}
