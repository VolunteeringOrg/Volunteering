package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.LinkType;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the LinkType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LinkTypeRepository extends JpaRepository<LinkType,Long> {

}
