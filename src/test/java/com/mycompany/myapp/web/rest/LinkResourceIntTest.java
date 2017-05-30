package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.MyappApp;

import com.mycompany.myapp.domain.Link;
import com.mycompany.myapp.repository.LinkRepository;
import com.mycompany.myapp.repository.search.LinkSearchRepository;
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
 * Test class for the LinkResource REST controller.
 *
 * @see LinkResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MyappApp.class)
public class LinkResourceIntTest {

    private static final Integer DEFAULT_PROVIDER_ID = 1;
    private static final Integer UPDATED_PROVIDER_ID = 2;

    private static final Integer DEFAULT_LINK_TYPE_ID = 1;
    private static final Integer UPDATED_LINK_TYPE_ID = 2;

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    @Autowired
    private LinkRepository linkRepository;

    @Autowired
    private LinkSearchRepository linkSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restLinkMockMvc;

    private Link link;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LinkResource linkResource = new LinkResource(linkRepository, linkSearchRepository);
        this.restLinkMockMvc = MockMvcBuilders.standaloneSetup(linkResource)
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
    public static Link createEntity(EntityManager em) {
        Link link = new Link()
            .providerId(DEFAULT_PROVIDER_ID)
            .linkTypeId(DEFAULT_LINK_TYPE_ID)
            .value(DEFAULT_VALUE);
        return link;
    }

    @Before
    public void initTest() {
        linkSearchRepository.deleteAll();
        link = createEntity(em);
    }

    @Test
    @Transactional
    public void createLink() throws Exception {
        int databaseSizeBeforeCreate = linkRepository.findAll().size();

        // Create the Link
        restLinkMockMvc.perform(post("/api/links")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(link)))
            .andExpect(status().isCreated());

        // Validate the Link in the database
        List<Link> linkList = linkRepository.findAll();
        assertThat(linkList).hasSize(databaseSizeBeforeCreate + 1);
        Link testLink = linkList.get(linkList.size() - 1);
        assertThat(testLink.getProviderId()).isEqualTo(DEFAULT_PROVIDER_ID);
        assertThat(testLink.getLinkTypeId()).isEqualTo(DEFAULT_LINK_TYPE_ID);
        assertThat(testLink.getValue()).isEqualTo(DEFAULT_VALUE);

        // Validate the Link in Elasticsearch
        Link linkEs = linkSearchRepository.findOne(testLink.getId());
        assertThat(linkEs).isEqualToComparingFieldByField(testLink);
    }

    @Test
    @Transactional
    public void createLinkWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = linkRepository.findAll().size();

        // Create the Link with an existing ID
        link.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLinkMockMvc.perform(post("/api/links")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(link)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Link> linkList = linkRepository.findAll();
        assertThat(linkList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkProviderIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = linkRepository.findAll().size();
        // set the field null
        link.setProviderId(null);

        // Create the Link, which fails.

        restLinkMockMvc.perform(post("/api/links")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(link)))
            .andExpect(status().isBadRequest());

        List<Link> linkList = linkRepository.findAll();
        assertThat(linkList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLinkTypeIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = linkRepository.findAll().size();
        // set the field null
        link.setLinkTypeId(null);

        // Create the Link, which fails.

        restLinkMockMvc.perform(post("/api/links")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(link)))
            .andExpect(status().isBadRequest());

        List<Link> linkList = linkRepository.findAll();
        assertThat(linkList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = linkRepository.findAll().size();
        // set the field null
        link.setValue(null);

        // Create the Link, which fails.

        restLinkMockMvc.perform(post("/api/links")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(link)))
            .andExpect(status().isBadRequest());

        List<Link> linkList = linkRepository.findAll();
        assertThat(linkList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLinks() throws Exception {
        // Initialize the database
        linkRepository.saveAndFlush(link);

        // Get all the linkList
        restLinkMockMvc.perform(get("/api/links?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(link.getId().intValue())))
            .andExpect(jsonPath("$.[*].providerId").value(hasItem(DEFAULT_PROVIDER_ID)))
            .andExpect(jsonPath("$.[*].linkTypeId").value(hasItem(DEFAULT_LINK_TYPE_ID)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.toString())));
    }

    @Test
    @Transactional
    public void getLink() throws Exception {
        // Initialize the database
        linkRepository.saveAndFlush(link);

        // Get the link
        restLinkMockMvc.perform(get("/api/links/{id}", link.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(link.getId().intValue()))
            .andExpect(jsonPath("$.providerId").value(DEFAULT_PROVIDER_ID))
            .andExpect(jsonPath("$.linkTypeId").value(DEFAULT_LINK_TYPE_ID))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingLink() throws Exception {
        // Get the link
        restLinkMockMvc.perform(get("/api/links/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLink() throws Exception {
        // Initialize the database
        linkRepository.saveAndFlush(link);
        linkSearchRepository.save(link);
        int databaseSizeBeforeUpdate = linkRepository.findAll().size();

        // Update the link
        Link updatedLink = linkRepository.findOne(link.getId());
        updatedLink
            .providerId(UPDATED_PROVIDER_ID)
            .linkTypeId(UPDATED_LINK_TYPE_ID)
            .value(UPDATED_VALUE);

        restLinkMockMvc.perform(put("/api/links")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedLink)))
            .andExpect(status().isOk());

        // Validate the Link in the database
        List<Link> linkList = linkRepository.findAll();
        assertThat(linkList).hasSize(databaseSizeBeforeUpdate);
        Link testLink = linkList.get(linkList.size() - 1);
        assertThat(testLink.getProviderId()).isEqualTo(UPDATED_PROVIDER_ID);
        assertThat(testLink.getLinkTypeId()).isEqualTo(UPDATED_LINK_TYPE_ID);
        assertThat(testLink.getValue()).isEqualTo(UPDATED_VALUE);

        // Validate the Link in Elasticsearch
        Link linkEs = linkSearchRepository.findOne(testLink.getId());
        assertThat(linkEs).isEqualToComparingFieldByField(testLink);
    }

    @Test
    @Transactional
    public void updateNonExistingLink() throws Exception {
        int databaseSizeBeforeUpdate = linkRepository.findAll().size();

        // Create the Link

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restLinkMockMvc.perform(put("/api/links")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(link)))
            .andExpect(status().isCreated());

        // Validate the Link in the database
        List<Link> linkList = linkRepository.findAll();
        assertThat(linkList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteLink() throws Exception {
        // Initialize the database
        linkRepository.saveAndFlush(link);
        linkSearchRepository.save(link);
        int databaseSizeBeforeDelete = linkRepository.findAll().size();

        // Get the link
        restLinkMockMvc.perform(delete("/api/links/{id}", link.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean linkExistsInEs = linkSearchRepository.exists(link.getId());
        assertThat(linkExistsInEs).isFalse();

        // Validate the database is empty
        List<Link> linkList = linkRepository.findAll();
        assertThat(linkList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchLink() throws Exception {
        // Initialize the database
        linkRepository.saveAndFlush(link);
        linkSearchRepository.save(link);

        // Search the link
        restLinkMockMvc.perform(get("/api/_search/links?query=id:" + link.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(link.getId().intValue())))
            .andExpect(jsonPath("$.[*].providerId").value(hasItem(DEFAULT_PROVIDER_ID)))
            .andExpect(jsonPath("$.[*].linkTypeId").value(hasItem(DEFAULT_LINK_TYPE_ID)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Link.class);
        Link link1 = new Link();
        link1.setId(1L);
        Link link2 = new Link();
        link2.setId(link1.getId());
        assertThat(link1).isEqualTo(link2);
        link2.setId(2L);
        assertThat(link1).isNotEqualTo(link2);
        link1.setId(null);
        assertThat(link1).isNotEqualTo(link2);
    }
}
