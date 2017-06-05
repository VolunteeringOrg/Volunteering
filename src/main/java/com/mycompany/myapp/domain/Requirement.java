package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Requirement.
 */
@Entity
@Table(name = "requirement")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "requirement")
public class Requirement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 1)
    @Column(name = "is_obligatory", length = 1, nullable = false)
    private String isObligatory;

    @NotNull
    @Size(max = 1000)
    @Column(name = "jhi_value", length = 1000, nullable = false)
    private String value;

    @ManyToOne(optional = false)
    @NotNull
    private Offer offer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIsObligatory() {
        return isObligatory;
    }

    public Requirement isObligatory(String isObligatory) {
        this.isObligatory = isObligatory;
        return this;
    }

    public void setIsObligatory(String isObligatory) {
        this.isObligatory = isObligatory;
    }

    public String getValue() {
        return value;
    }

    public Requirement value(String value) {
        this.value = value;
        return this;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Offer getOffer() {
        return offer;
    }

    public Requirement offer(Offer offer) {
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
        Requirement requirement = (Requirement) o;
        if (requirement.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), requirement.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Requirement{" +
            "id=" + getId() +
            ", isObligatory='" + getIsObligatory() + "'" +
            ", value='" + getValue() + "'" +
            "}";
    }
}
