package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.OfferDetails;

import com.mycompany.myapp.repository.OfferDetailsRepository;
import com.mycompany.myapp.repository.search.OfferDetailsSearchRepository;
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
 * REST controller for managing OfferDetails.
 */
@RestController
@RequestMapping("/api")
public class OfferDetailsResource {

    private final Logger log = LoggerFactory.getLogger(OfferDetailsResource.class);

    private static final String ENTITY_NAME = "offerDetails";

    private final OfferDetailsRepository offerDetailsRepository;

    private final OfferDetailsSearchRepository offerDetailsSearchRepository;

    public OfferDetailsResource(OfferDetailsRepository offerDetailsRepository, OfferDetailsSearchRepository offerDetailsSearchRepository) {
        this.offerDetailsRepository = offerDetailsRepository;
        this.offerDetailsSearchRepository = offerDetailsSearchRepository;
    }

    /**
     * POST  /offer-details : Create a new offerDetails.
     *
     * @param offerDetails the offerDetails to create
     * @return the ResponseEntity with status 201 (Created) and with body the new offerDetails, or with status 400 (Bad Request) if the offerDetails has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/offer-details")
    @Timed
    public ResponseEntity<OfferDetails> createOfferDetails(@Valid @RequestBody OfferDetails offerDetails) throws URISyntaxException {
        log.debug("REST request to save OfferDetails : {}", offerDetails);
        if (offerDetails.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new offerDetails cannot already have an ID")).body(null);
        }
        OfferDetails result = offerDetailsRepository.save(offerDetails);
        offerDetailsSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/offer-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /offer-details : Updates an existing offerDetails.
     *
     * @param offerDetails the offerDetails to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated offerDetails,
     * or with status 400 (Bad Request) if the offerDetails is not valid,
     * or with status 500 (Internal Server Error) if the offerDetails couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/offer-details")
    @Timed
    public ResponseEntity<OfferDetails> updateOfferDetails(@Valid @RequestBody OfferDetails offerDetails) throws URISyntaxException {
        log.debug("REST request to update OfferDetails : {}", offerDetails);
        if (offerDetails.getId() == null) {
            return createOfferDetails(offerDetails);
        }
        OfferDetails result = offerDetailsRepository.save(offerDetails);
        offerDetailsSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, offerDetails.getId().toString()))
            .body(result);
    }

    /**
     * GET  /offer-details : get all the offerDetails.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of offerDetails in body
     */
    @GetMapping("/offer-details")
    @Timed
    public ResponseEntity<List<OfferDetails>> getAllOfferDetails(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of OfferDetails");
        Page<OfferDetails> page = offerDetailsRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/offer-details");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /offer-details/:id : get the "id" offerDetails.
     *
     * @param id the id of the offerDetails to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the offerDetails, or with status 404 (Not Found)
     */
    @GetMapping("/offer-details/{id}")
    @Timed
    public ResponseEntity<OfferDetails> getOfferDetails(@PathVariable Long id) {
        log.debug("REST request to get OfferDetails : {}", id);
        OfferDetails offerDetails = offerDetailsRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(offerDetails));
    }

    /**
     * DELETE  /offer-details/:id : delete the "id" offerDetails.
     *
     * @param id the id of the offerDetails to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/offer-details/{id}")
    @Timed
    public ResponseEntity<Void> deleteOfferDetails(@PathVariable Long id) {
        log.debug("REST request to delete OfferDetails : {}", id);
        offerDetailsRepository.delete(id);
        offerDetailsSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/offer-details?query=:query : search for the offerDetails corresponding
     * to the query.
     *
     * @param query the query of the offerDetails search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/offer-details")
    @Timed
    public ResponseEntity<List<OfferDetails>> searchOfferDetails(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of OfferDetails for query {}", query);
        Page<OfferDetails> page = offerDetailsSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/offer-details");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
