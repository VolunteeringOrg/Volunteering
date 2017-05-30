package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.StatusType;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the StatusType entity.
 */
public interface StatusTypeSearchRepository extends ElasticsearchRepository<StatusType, Long> {
}
