package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A OfferDetails.
 */
@Entity
@Table(name = "offer_details")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "offerdetails")
public class OfferDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Size(max = 1000)
    @Column(name = "description", length = 1000)
    private String description;

    @NotNull
    @Size(max = 1)
    @Column(name = "is_valid", length = 1, nullable = false)
    private String isValid;

    @ManyToOne(optional = false)
    @NotNull
    private Offer offer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public OfferDetails description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIsValid() {
        return isValid;
    }

    public OfferDetails isValid(String isValid) {
        this.isValid = isValid;
        return this;
    }

    public void setIsValid(String isValid) {
        this.isValid = isValid;
    }

    public Offer getOffer() {
        return offer;
    }

    public OfferDetails offer(Offer offer) {
        this.offer = offer;
        return this;
    }

    public void setOffer(Offer offer) {
        this.offer = offer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OfferDetails offerDetails = (OfferDetails) o;
        if (offerDetails.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), offerDetails.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "OfferDetails{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", isValid='" + getIsValid() + "'" +
            "}";
    }
}
