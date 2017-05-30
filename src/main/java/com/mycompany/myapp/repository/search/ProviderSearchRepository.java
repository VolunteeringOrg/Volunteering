package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Provider;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Provider entity.
 */
public interface ProviderSearchRepository extends ElasticsearchRepository<Provider, Long> {
}
