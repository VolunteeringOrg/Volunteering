package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Requirement;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Requirement entity.
 */
public interface RequirementSearchRepository extends ElasticsearchRepository<Requirement, Long> {
}
