package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Requirement;

import com.mycompany.myapp.repository.RequirementRepository;
import com.mycompany.myapp.repository.search.RequirementSearchRepository;
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
 * REST controller for managing Requirement.
 */
@RestController
@RequestMapping("/api")
public class RequirementResource {

    private final Logger log = LoggerFactory.getLogger(RequirementResource.class);

    private static final String ENTITY_NAME = "requirement";

    private final RequirementRepository requirementRepository;

    private final RequirementSearchRepository requirementSearchRepository;

    public RequirementResource(RequirementRepository requirementRepository, RequirementSearchRepository requirementSearchRepository) {
        this.requirementRepository = requirementRepository;
        this.requirementSearchRepository = requirementSearchRepository;
    }

    /**
     * POST  /requirements : Create a new requirement.
     *
     * @param requirement the requirement to create
     * @return the ResponseEntity with status 201 (Created) and with body the new requirement, or with status 400 (Bad Request) if the requirement has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/requirements")
    @Timed
    public ResponseEntity<Requirement> createRequirement(@Valid @RequestBody Requirement requirement) throws URISyntaxException {
        log.debug("REST request to save Requirement : {}", requirement);
        if (requirement.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new requirement cannot already have an ID")).body(null);
        }
        Requirement result = requirementRepository.save(requirement);
        requirementSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/requirements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /requirements : Updates an existing requirement.
     *
     * @param requirement the requirement to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated requirement,
     * or with status 400 (Bad Request) if the requirement is not valid,
     * or with status 500 (Internal Server Error) if the requirement couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/requirements")
    @Timed
    public ResponseEntity<Requirement> updateRequirement(@Valid @RequestBody Requirement requirement) throws URISyntaxException {
        log.debug("REST request to update Requirement : {}", requirement);
        if (requirement.getId() == null) {
            return createRequirement(requirement);
        }
        Requirement result = requirementRepository.save(requirement);
        requirementSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, requirement.getId().toString()))
            .body(result);
    }

    /**
     * GET  /requirements : get all the requirements.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of requirements in body
     */
    @GetMapping("/requirements")
    @Timed
    public ResponseEntity<List<Requirement>> getAllRequirements(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Requirements");
        Page<Requirement> page = requirementRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/requirements");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /requirements/:id : get the "id" requirement.
     *
     * @param id the id of the requirement to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the requirement, or with status 404 (Not Found)
     */
    @GetMapping("/requirements/{id}")
    @Timed
    public ResponseEntity<Requirement> getRequirement(@PathVariable Long id) {
        log.debug("REST request to get Requirement : {}", id);
        Requirement requirement = requirementRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(requirement));
    }

    /**
     * DELETE  /requirements/:id : delete the "id" requirement.
     *
     * @param id the id of the requirement to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/requirements/{id}")
    @Timed
    public ResponseEntity<Void> deleteRequirement(@PathVariable Long id) {
        log.debug("REST request to delete Requirement : {}", id);
        requirementRepository.delete(id);
        requirementSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/requirements?query=:query : search for the requirement corresponding
     * to the query.
     *
     * @param query the query of the requirement search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/requirements")
    @Timed
    public ResponseEntity<List<Requirement>> searchRequirements(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Requirements for query {}", query);
        Page<Requirement> page = requirementSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/requirements");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
