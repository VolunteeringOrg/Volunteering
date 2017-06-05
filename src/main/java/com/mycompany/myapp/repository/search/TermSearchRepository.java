package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Term;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Term entity.
 */
public interface TermSearchRepository extends ElasticsearchRepository<Term, Long> {
}
