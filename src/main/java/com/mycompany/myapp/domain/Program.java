package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Program.
 */
@Entity
@Table(name = "program")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "program")
public class Program implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "provider_id")
    private Integer providerId;

    @Size(max = 255)
    @Column(name = "name", length = 255)
    private String name;

    @Size(max = 255)
    @Column(name = "highlight", length = 255)
    private String highlight;

    @NotNull
    @Column(name = "date_to", nullable = false)
    private ZonedDateTime dateTo;

    @NotNull
    @Column(name = "date_from", nullable = false)
    private ZonedDateTime dateFrom;

    @NotNull
    @Column(name = "status_type_id", nullable = false)
    private Integer statusTypeId;

    @Size(max = 255)
    @Column(name = "share_program", length = 255)
    private String shareProgram;

    @OneToMany(mappedBy = "v")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Fk_provider_program> vs = new HashSet<>();

    @ManyToOne
    private Provider fk_provider_program;

    @ManyToOne
    private StatusType fk_statustype_program;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getProviderId() {
        return providerId;
    }

    public Program providerId(Integer providerId) {
        this.providerId = providerId;
        return this;
    }

    public void setProviderId(Integer providerId) {
        this.providerId = providerId;
    }

    public String getName() {
        return name;
    }

    public Program name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHighlight() {
        return highlight;
    }

    public Program highlight(String highlight) {
        this.highlight = highlight;
        return this;
    }

    public void setHighlight(String highlight) {
        this.highlight = highlight;
    }

    public ZonedDateTime getDateTo() {
        return dateTo;
    }

    public Program dateTo(ZonedDateTime dateTo) {
        this.dateTo = dateTo;
        return this;
    }

    public void setDateTo(ZonedDateTime dateTo) {
        this.dateTo = dateTo;
    }

    public ZonedDateTime getDateFrom() {
        return dateFrom;
    }

    public Program dateFrom(ZonedDateTime dateFrom) {
        this.dateFrom = dateFrom;
        return this;
    }

    public void setDateFrom(ZonedDateTime dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Integer getStatusTypeId() {
        return statusTypeId;
    }

    public Program statusTypeId(Integer statusTypeId) {
        this.statusTypeId = statusTypeId;
        return this;
    }

    public void setStatusTypeId(Integer statusTypeId) {
        this.statusTypeId = statusTypeId;
    }

    public String getShareProgram() {
        return shareProgram;
    }

    public Program shareProgram(String shareProgram) {
        this.shareProgram = shareProgram;
        return this;
    }

    public void setShareProgram(String shareProgram) {
        this.shareProgram = shareProgram;
    }

    public Set<Fk_provider_program> getVs() {
        return vs;
    }

    public Program vs(Set<Fk_provider_program> fk_provider_programs) {
        this.vs = fk_provider_programs;
        return this;
    }

    public Program addV(Fk_provider_program fk_provider_program) {
        this.vs.add(fk_provider_program);
        fk_provider_program.setV(this);
        return this;
    }

    public Program removeV(Fk_provider_program fk_provider_program) {
        this.vs.remove(fk_provider_program);
        fk_provider_program.setV(null);
        return this;
    }

    public void setVs(Set<Fk_provider_program> fk_provider_programs) {
        this.vs = fk_provider_programs;
    }

    public Provider getFk_provider_program() {
        return fk_provider_program;
    }

    public Program fk_provider_program(Provider provider) {
        this.fk_provider_program = provider;
        return this;
    }

    public void setFk_provider_program(Provider provider) {
        this.fk_provider_program = provider;
    }

    public StatusType getFk_statustype_program() {
        return fk_statustype_program;
    }

    public Program fk_statustype_program(StatusType statusType) {
        this.fk_statustype_program = statusType;
        return this;
    }

    public void setFk_statustype_program(StatusType statusType) {
        this.fk_statustype_program = statusType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Program program = (Program) o;
        if (program.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), program.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Program{" +
            "id=" + getId() +
            ", providerId='" + getProviderId() + "'" +
            ", name='" + getName() + "'" +
            ", highlight='" + getHighlight() + "'" +
            ", dateTo='" + getDateTo() + "'" +
            ", dateFrom='" + getDateFrom() + "'" +
            ", statusTypeId='" + getStatusTypeId() + "'" +
            ", shareProgram='" + getShareProgram() + "'" +
            "}";
    }
}
