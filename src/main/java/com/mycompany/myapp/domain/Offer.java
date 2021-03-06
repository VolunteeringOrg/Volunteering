package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Offer.
 */
@Entity
@Table(name = "offer")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "offer")
public class Offer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

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
    @Column(name = "date_from", nullable = false)
    private ZonedDateTime dateFrom;

    @NotNull
    @Column(name = "date_to", nullable = false)
    private ZonedDateTime dateTo;

    @Column(name = "workhours_per_month")
    private Integer workhoursPerMonth;

    @NotNull
    @Size(max = 255)
    @Column(name = "daytime", length = 255, nullable = false)
    private String daytime;

    @Size(max = 255)
    @Column(name = "workhours", length = 255)
    private String workhours;

    @ManyToOne(optional = false)
    @NotNull
    private StatusType statusType;

    @ManyToOne(optional = false)
    @NotNull
    private Program program;

    @ManyToOne
    private Term term;

    @OneToMany(mappedBy = "offer")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Application> applications = new HashSet<>();

    @OneToMany(mappedBy = "offer")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Requirement> requirements = new HashSet<>();

    @OneToMany(mappedBy = "offer")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<OfferDetails> offerDetails = new HashSet<>();

    @OneToMany(mappedBy = "offer")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Document> documents = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public StatusType getStatusType() {
        return statusType;
    }

    public Offer statusType(StatusType statusType) {
        this.statusType = statusType;
        return this;
    }

    public void setStatusType(StatusType statusType) {
        this.statusType = statusType;
    }

    public Program getProgram() {
        return program;
    }

    public Offer program(Program program) {
        this.program = program;
        return this;
    }

    public void setProgram(Program program) {
        this.program = program;
    }

    public Term getTerm() {
        return term;
    }

    public Offer term(Term term) {
        this.term = term;
        return this;
    }

    public void setTerm(Term term) {
        this.term = term;
    }

    public Set<Application> getApplications() {
        return applications;
    }

    public Offer applications(Set<Application> applications) {
        this.applications = applications;
        return this;
    }

    public Offer addApplication(Application application) {
        this.applications.add(application);
        application.setOffer(this);
        return this;
    }

    public Offer removeApplication(Application application) {
        this.applications.remove(application);
        application.setOffer(null);
        return this;
    }

    public void setApplications(Set<Application> applications) {
        this.applications = applications;
    }

    public Set<Requirement> getRequirements() {
        return requirements;
    }

    public Offer requirements(Set<Requirement> requirements) {
        this.requirements = requirements;
        return this;
    }

    public Offer addRequirement(Requirement requirement) {
        this.requirements.add(requirement);
        requirement.setOffer(this);
        return this;
    }

    public Offer removeRequirement(Requirement requirement) {
        this.requirements.remove(requirement);
        requirement.setOffer(null);
        return this;
    }

    public void setRequirements(Set<Requirement> requirements) {
        this.requirements = requirements;
    }

    public Set<OfferDetails> getOfferDetails() {
        return offerDetails;
    }

    public Offer offerDetails(Set<OfferDetails> offerDetails) {
        this.offerDetails = offerDetails;
        return this;
    }

    public Offer addOfferDetails(OfferDetails offerDetails) {
        this.offerDetails.add(offerDetails);
        offerDetails.setOffer(this);
        return this;
    }

    public Offer removeOfferDetails(OfferDetails offerDetails) {
        this.offerDetails.remove(offerDetails);
        offerDetails.setOffer(null);
        return this;
    }

    public void setOfferDetails(Set<OfferDetails> offerDetails) {
        this.offerDetails = offerDetails;
    }

    public Set<Document> getDocuments() {
        return documents;
    }

    public Offer documents(Set<Document> documents) {
        this.documents = documents;
        return this;
    }

    public Offer addDocument(Document document) {
        this.documents.add(document);
        document.setOffer(this);
        return this;
    }

    public Offer removeDocument(Document document) {
        this.documents.remove(document);
        document.setOffer(null);
        return this;
    }

    public void setDocuments(Set<Document> documents) {
        this.documents = documents;
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
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", volunteerType='" + getVolunteerType() + "'" +
            ", initialNoVacancies='" + getInitialNoVacancies() + "'" +
            ", actualNoVacancies='" + getActualNoVacancies() + "'" +
            ", dateFrom='" + getDateFrom() + "'" +
            ", dateTo='" + getDateTo() + "'" +
            ", workhoursPerMonth='" + getWorkhoursPerMonth() + "'" +
            ", daytime='" + getDaytime() + "'" +
            ", workhours='" + getWorkhours() + "'" +
            "}";
    }
}
