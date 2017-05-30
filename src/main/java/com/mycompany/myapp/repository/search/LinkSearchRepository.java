package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Link;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Link entity.
 */
public interface LinkSearchRepository extends ElasticsearchRepository<Link, Long> {
}
