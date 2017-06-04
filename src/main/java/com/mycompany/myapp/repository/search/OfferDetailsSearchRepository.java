package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.OfferDetails;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the OfferDetails entity.
 */
public interface OfferDetailsSearchRepository extends ElasticsearchRepository<OfferDetails, Long> {
}
