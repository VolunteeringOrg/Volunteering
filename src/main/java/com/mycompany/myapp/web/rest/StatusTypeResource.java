package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.StatusType;

import com.mycompany.myapp.repository.StatusTypeRepository;
import com.mycompany.myapp.repository.search.StatusTypeSearchRepository;
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
 * REST controller for managing StatusType.
 */
@RestController
@RequestMapping("/api")
public class StatusTypeResource {

    private final Logger log = LoggerFactory.getLogger(StatusTypeResource.class);

    private static final String ENTITY_NAME = "statusType";

    private final StatusTypeRepository statusTypeRepository;

    private final StatusTypeSearchRepository statusTypeSearchRepository;

    public StatusTypeResource(StatusTypeRepository statusTypeRepository, StatusTypeSearchRepository statusTypeSearchRepository) {
        this.statusTypeRepository = statusTypeRepository;
        this.statusTypeSearchRepository = statusTypeSearchRepository;
    }

    /**
     * POST  /status-types : Create a new statusType.
     *
     * @param statusType the statusType to create
     * @return the ResponseEntity with status 201 (Created) and with body the new statusType, or with status 400 (Bad Request) if the statusType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/status-types")
    @Timed
    public ResponseEntity<StatusType> createStatusType(@Valid @RequestBody StatusType statusType) throws URISyntaxException {
        log.debug("REST request to save StatusType : {}", statusType);
        if (statusType.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new statusType cannot already have an ID")).body(null);
        }
        StatusType result = statusTypeRepository.save(statusType);
        statusTypeSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/status-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /status-types : Updates an existing statusType.
     *
     * @param statusType the statusType to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated statusType,
     * or with status 400 (Bad Request) if the statusType is not valid,
     * or with status 500 (Internal Server Error) if the statusType couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/status-types")
    @Timed
    public ResponseEntity<StatusType> updateStatusType(@Valid @RequestBody StatusType statusType) throws URISyntaxException {
        log.debug("REST request to update StatusType : {}", statusType);
        if (statusType.getId() == null) {
            return createStatusType(statusType);
        }
        StatusType result = statusTypeRepository.save(statusType);
        statusTypeSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, statusType.getId().toString()))
            .body(result);
    }

    /**
     * GET  /status-types : get all the statusTypes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of statusTypes in body
     */
    @GetMapping("/status-types")
    @Timed
    public ResponseEntity<List<StatusType>> getAllStatusTypes(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of StatusTypes");
        Page<StatusType> page = statusTypeRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/status-types");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /status-types/:id : get the "id" statusType.
     *
     * @param id the id of the statusType to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the statusType, or with status 404 (Not Found)
     */
    @GetMapping("/status-types/{id}")
    @Timed
    public ResponseEntity<StatusType> getStatusType(@PathVariable Long id) {
        log.debug("REST request to get StatusType : {}", id);
        StatusType statusType = statusTypeRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(statusType));
    }

    /**
     * DELETE  /status-types/:id : delete the "id" statusType.
     *
     * @param id the id of the statusType to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/status-types/{id}")
    @Timed
    public ResponseEntity<Void> deleteStatusType(@PathVariable Long id) {
        log.debug("REST request to delete StatusType : {}", id);
        statusTypeRepository.delete(id);
        statusTypeSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/status-types?query=:query : search for the statusType corresponding
     * to the query.
     *
     * @param query the query of the statusType search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/status-types")
    @Timed
    public ResponseEntity<List<StatusType>> searchStatusTypes(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of StatusTypes for query {}", query);
        Page<StatusType> page = statusTypeSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/status-types");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
