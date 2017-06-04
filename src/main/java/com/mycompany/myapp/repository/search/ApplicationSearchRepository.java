package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Application;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Application entity.
 */
public interface ApplicationSearchRepository extends ElasticsearchRepository<Application, Long> {
}
