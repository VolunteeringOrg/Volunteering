package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A StatusType.
 */
@Entity
@Table(name = "status_type")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "statustype")
public class StatusType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "jhi_value", length = 255, nullable = false)
    private String value;

    @OneToMany(mappedBy = "statusType")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Program> programs = new HashSet<>();

    @OneToMany(mappedBy = "statusType")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Offer> offers = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public StatusType value(String value) {
        this.value = value;
        return this;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Set<Program> getPrograms() {
        return programs;
    }

    public StatusType programs(Set<Program> programs) {
        this.programs = programs;
        return this;
    }

    public StatusType addProgram(Program program) {
        this.programs.add(program);
        program.setStatusType(this);
        return this;
    }

    public StatusType removeProgram(Program program) {
        this.programs.remove(program);
        program.setStatusType(null);
        return this;
    }

    public void setPrograms(Set<Program> programs) {
        this.programs = programs;
    }

    public Set<Offer> getOffers() {
        return offers;
    }

    public StatusType offers(Set<Offer> offers) {
        this.offers = offers;
        return this;
    }

    public StatusType addOffer(Offer offer) {
        this.offers.add(offer);
        offer.setStatusType(this);
        return this;
    }

    public StatusType removeOffer(Offer offer) {
        this.offers.remove(offer);
        offer.setStatusType(null);
        return this;
    }

    public void setOffers(Set<Offer> offers) {
        this.offers = offers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StatusType statusType = (StatusType) o;
        if (statusType.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), statusType.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "StatusType{" +
            "id=" + getId() +
            ", value='" + getValue() + "'" +
            "}";
    }
}
