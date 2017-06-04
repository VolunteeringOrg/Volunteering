package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Document;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Document entity.
 */
public interface DocumentSearchRepository extends ElasticsearchRepository<Document, Long> {
}
