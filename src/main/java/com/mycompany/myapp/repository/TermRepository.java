package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Term;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Term entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TermRepository extends JpaRepository<Term,Long> {

}
