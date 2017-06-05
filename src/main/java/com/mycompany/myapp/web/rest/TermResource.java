package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Term;

import com.mycompany.myapp.repository.TermRepository;
import com.mycompany.myapp.repository.search.TermSearchRepository;
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
 * REST controller for managing Term.
 */
@RestController
@RequestMapping("/api")
public class TermResource {

    private final Logger log = LoggerFactory.getLogger(TermResource.class);

    private static final String ENTITY_NAME = "term";

    private final TermRepository termRepository;

    private final TermSearchRepository termSearchRepository;

    public TermResource(TermRepository termRepository, TermSearchRepository termSearchRepository) {
        this.termRepository = termRepository;
        this.termSearchRepository = termSearchRepository;
    }

    /**
     * POST  /terms : Create a new term.
     *
     * @param term the term to create
     * @return the ResponseEntity with status 201 (Created) and with body the new term, or with status 400 (Bad Request) if the term has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/terms")
    @Timed
    public ResponseEntity<Term> createTerm(@Valid @RequestBody Term term) throws URISyntaxException {
        log.debug("REST request to save Term : {}", term);
        if (term.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new term cannot already have an ID")).body(null);
        }
        Term result = termRepository.save(term);
        termSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/terms/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /terms : Updates an existing term.
     *
     * @param term the term to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated term,
     * or with status 400 (Bad Request) if the term is not valid,
     * or with status 500 (Internal Server Error) if the term couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/terms")
    @Timed
    public ResponseEntity<Term> updateTerm(@Valid @RequestBody Term term) throws URISyntaxException {
        log.debug("REST request to update Term : {}", term);
        if (term.getId() == null) {
            return createTerm(term);
        }
        Term result = termRepository.save(term);
        termSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, term.getId().toString()))
            .body(result);
    }

    /**
     * GET  /terms : get all the terms.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of terms in body
     */
    @GetMapping("/terms")
    @Timed
    public ResponseEntity<List<Term>> getAllTerms(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Terms");
        Page<Term> page = termRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/terms");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /terms/:id : get the "id" term.
     *
     * @param id the id of the term to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the term, or with status 404 (Not Found)
     */
    @GetMapping("/terms/{id}")
    @Timed
    public ResponseEntity<Term> getTerm(@PathVariable Long id) {
        log.debug("REST request to get Term : {}", id);
        Term term = termRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(term));
    }

    /**
     * DELETE  /terms/:id : delete the "id" term.
     *
     * @param id the id of the term to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/terms/{id}")
    @Timed
    public ResponseEntity<Void> deleteTerm(@PathVariable Long id) {
        log.debug("REST request to delete Term : {}", id);
        termRepository.delete(id);
        termSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/terms?query=:query : search for the term corresponding
     * to the query.
     *
     * @param query the query of the term search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/terms")
    @Timed
    public ResponseEntity<List<Term>> searchTerms(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Terms for query {}", query);
        Page<Term> page = termSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/terms");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
