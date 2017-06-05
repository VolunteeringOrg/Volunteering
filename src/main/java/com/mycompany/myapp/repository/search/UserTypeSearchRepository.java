package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.UserType;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the UserType entity.
 */
public interface UserTypeSearchRepository extends ElasticsearchRepository<UserType, Long> {
}
