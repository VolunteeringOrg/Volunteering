package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Offer.
 */
@Entity
@Table(name = "offer")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "offer")
public class Offer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "program_id", nullable = false)
    private Integer programId;

    @NotNull
    @Size(max = 255)
    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @Size(max = 1000)
    @Column(name = "description", length = 1000)
    private String description;

    @Size(max = 255)
    @Column(name = "volunteer_type", length = 255)
    private String volunteerType;

    @Column(name = "initial_no_vacancies")
    private Integer initialNoVacancies;

    @Column(name = "actual_no_vacancies")
    private Integer actualNoVacancies;

    @NotNull
    @Column(name = "status_type_id", nullable = false)
    private Integer statusTypeId;

    @NotNull
    @Column(name = "date_from", nullable = false)
    private ZonedDateTime dateFrom;

    @NotNull
    @Column(name = "date_to", nullable = false)
    private ZonedDateTime dateTo;

    @Column(name = "workhours_per_month")
    private Integer workhoursPerMonth;

    @NotNull
    @Column(name = "term_id", nullable = false)
    private Integer termId;

    @NotNull
    @Size(max = 255)
    @Column(name = "daytime", length = 255, nullable = false)
    private String daytime;

    @Size(max = 255)
    @Column(name = "workhours", length = 255)
    private String workhours;

    @ManyToOne
    private Program fk_program_offer;

    @ManyToOne(optional = false)
    @NotNull
    private StatusType fk_statustype_offer;

    @ManyToOne
    private Term fk_term_offer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getProgramId() {
        return programId;
    }

    public Offer programId(Integer programId) {
        this.programId = programId;
        return this;
    }

    public void setProgramId(Integer programId) {
        this.programId = programId;
    }

    public String getName() {
        return name;
    }

    public Offer name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Offer description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVolunteerType() {
        return volunteerType;
    }

    public Offer volunteerType(String volunteerType) {
        this.volunteerType = volunteerType;
        return this;
    }

    public void setVolunteerType(String volunteerType) {
        this.volunteerType = volunteerType;
    }

    public Integer getInitialNoVacancies() {
        return initialNoVacancies;
    }

    public Offer initialNoVacancies(Integer initialNoVacancies) {
        this.initialNoVacancies = initialNoVacancies;
        return this;
    }

    public void setInitialNoVacancies(Integer initialNoVacancies) {
        this.initialNoVacancies = initialNoVacancies;
    }

    public Integer getActualNoVacancies() {
        return actualNoVacancies;
    }

    public Offer actualNoVacancies(Integer actualNoVacancies) {
        this.actualNoVacancies = actualNoVacancies;
        return this;
    }

    public void setActualNoVacancies(Integer actualNoVacancies) {
        this.actualNoVacancies = actualNoVacancies;
    }

    public Integer getStatusTypeId() {
        return statusTypeId;
    }

    public Offer statusTypeId(Integer statusTypeId) {
        this.statusTypeId = statusTypeId;
        return this;
    }

    public void setStatusTypeId(Integer statusTypeId) {
        this.statusTypeId = statusTypeId;
    }

    public ZonedDateTime getDateFrom() {
        return dateFrom;
    }

    public Offer dateFrom(ZonedDateTime dateFrom) {
        this.dateFrom = dateFrom;
        return this;
    }

    public void setDateFrom(ZonedDateTime dateFrom) {
        this.dateFrom = dateFrom;
    }

    public ZonedDateTime getDateTo() {
        return dateTo;
    }

    public Offer dateTo(ZonedDateTime dateTo) {
        this.dateTo = dateTo;
        return this;
    }

    public void setDateTo(ZonedDateTime dateTo) {
        this.dateTo = dateTo;
    }

    public Integer getWorkhoursPerMonth() {
        return workhoursPerMonth;
    }

    public Offer workhoursPerMonth(Integer workhoursPerMonth) {
        this.workhoursPerMonth = workhoursPerMonth;
        return this;
    }

    public void setWorkhoursPerMonth(Integer workhoursPerMonth) {
        this.workhoursPerMonth = workhoursPerMonth;
    }

    public Integer getTermId() {
        return termId;
    }

    public Offer termId(Integer termId) {
        this.termId = termId;
        return this;
    }

    public void setTermId(Integer termId) {
        this.termId = termId;
    }

    public String getDaytime() {
        return daytime;
    }

    public Offer daytime(String daytime) {
        this.daytime = daytime;
        return this;
    }

    public void setDaytime(String daytime) {
        this.daytime = daytime;
    }

    public String getWorkhours() {
        return workhours;
    }

    public Offer workhours(String workhours) {
        this.workhours = workhours;
        return this;
    }

    public void setWorkhours(String workhours) {
        this.workhours = workhours;
    }

    public Program getFk_program_offer() {
        return fk_program_offer;
    }

    public Offer fk_program_offer(Program program) {
        this.fk_program_offer = program;
        return this;
    }

    public void setFk_program_offer(Program program) {
        this.fk_program_offer = program;
    }

    public StatusType getFk_statustype_offer() {
        return fk_statustype_offer;
    }

    public Offer fk_statustype_offer(StatusType statusType) {
        this.fk_statustype_offer = statusType;
        return this;
    }

    public void setFk_statustype_offer(StatusType statusType) {
        this.fk_statustype_offer = statusType;
    }

    public Term getFk_term_offer() {
        return fk_term_offer;
    }

    public Offer fk_term_offer(Term term) {
        this.fk_term_offer = term;
        return this;
    }

    public void setFk_term_offer(Term term) {
        this.fk_term_offer = term;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Offer offer = (Offer) o;
        if (offer.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), offer.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Offer{" +
            "id=" + getId() +
            ", programId='" + getProgramId() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", volunteerType='" + getVolunteerType() + "'" +
            ", initialNoVacancies='" + getInitialNoVacancies() + "'" +
            ", actualNoVacancies='" + getActualNoVacancies() + "'" +
            ", statusTypeId='" + getStatusTypeId() + "'" +
            ", dateFrom='" + getDateFrom() + "'" +
            ", dateTo='" + getDateTo() + "'" +
            ", workhoursPerMonth='" + getWorkhoursPerMonth() + "'" +
            ", termId='" + getTermId() + "'" +
            ", daytime='" + getDaytime() + "'" +
            ", workhours='" + getWorkhours() + "'" +
            "}";
    }
}
