package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.LinkType;

import com.mycompany.myapp.repository.LinkTypeRepository;
import com.mycompany.myapp.repository.search.LinkTypeSearchRepository;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import com.mycompany.myapp.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing LinkType.
 */
@RestController
@RequestMapping("/api")
public class LinkTypeResource {

    private final Logger log = LoggerFactory.getLogger(LinkTypeResource.class);

    private static final String ENTITY_NAME = "linkType";

    private final LinkTypeRepository linkTypeRepository;

    private final LinkTypeSearchRepository linkTypeSearchRepository;

    public LinkTypeResource(LinkTypeRepository linkTypeRepository, LinkTypeSearchRepository linkTypeSearchRepository) {
        this.linkTypeRepository = linkTypeRepository;
        this.linkTypeSearchRepository = linkTypeSearchRepository;
    }

    /**
     * POST  /link-types : Create a new linkType.
     *
     * @param linkType the linkType to create
     * @return the ResponseEntity with status 201 (Created) and with body the new linkType, or with status 400 (Bad Request) if the linkType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/link-types")
    @Timed
    public ResponseEntity<LinkType> createLinkType(@Valid @RequestBody LinkType linkType) throws URISyntaxException {
        log.debug("REST request to save LinkType : {}", linkType);
        if (linkType.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new linkType cannot already have an ID")).body(null);
        }
        LinkType result = linkTypeRepository.save(linkType);
        linkTypeSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/link-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /link-types : Updates an existing linkType.
     *
     * @param linkType the linkType to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated linkType,
     * or with status 400 (Bad Request) if the linkType is not valid,
     * or with status 500 (Internal Server Error) if the linkType couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/link-types")
    @Timed
    public ResponseEntity<LinkType> updateLinkType(@Valid @RequestBody LinkType linkType) throws URISyntaxException {
        log.debug("REST request to update LinkType : {}", linkType);
        if (linkType.getId() == null) {
            return createLinkType(linkType);
        }
        LinkType result = linkTypeRepository.save(linkType);
        linkTypeSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, linkType.getId().toString()))
            .body(result);
    }

    /**
     * GET  /link-types : get all the linkTypes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of linkTypes in body
     */
    @GetMapping("/link-types")
    @Timed
    public ResponseEntity<List<LinkType>> getAllLinkTypes(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of LinkTypes");
        Page<LinkType> page = linkTypeRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/link-types");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /link-types/:id : get the "id" linkType.
     *
     * @param id the id of the linkType to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the linkType, or with status 404 (Not Found)
     */
    @GetMapping("/link-types/{id}")
    @Timed
    public ResponseEntity<LinkType> getLinkType(@PathVariable Long id) {
        log.debug("REST request to get LinkType : {}", id);
        LinkType linkType = linkTypeRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(linkType));
    }

    /**
     * DELETE  /link-types/:id : delete the "id" linkType.
     *
     * @param id the id of the linkType to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/link-types/{id}")
    @Timed
    public ResponseEntity<Void> deleteLinkType(@PathVariable Long id) {
        log.debug("REST request to delete LinkType : {}", id);
        linkTypeRepository.delete(id);
        linkTypeSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/link-types?query=:query : search for the linkType corresponding
     * to the query.
     *
     * @param query the query of the linkType search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/link-types")
    @Timed
    public ResponseEntity<List<LinkType>> searchLinkTypes(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of LinkTypes for query {}", query);
        Page<LinkType> page = linkTypeSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/link-types");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
