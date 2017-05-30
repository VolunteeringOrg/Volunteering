package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.LinkType;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the LinkType entity.
 */
public interface LinkTypeSearchRepository extends ElasticsearchRepository<LinkType, Long> {
}
