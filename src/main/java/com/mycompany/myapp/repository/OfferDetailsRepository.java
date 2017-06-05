package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.OfferDetails;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the OfferDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OfferDetailsRepository extends JpaRepository<OfferDetails,Long> {

}
