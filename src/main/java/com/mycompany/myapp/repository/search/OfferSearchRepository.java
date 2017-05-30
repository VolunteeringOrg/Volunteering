package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Offer;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Offer entity.
 */
public interface OfferSearchRepository extends ElasticsearchRepository<Offer, Long> {
}
