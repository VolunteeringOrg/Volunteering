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

    @Size(max = 255)
    @Column(name = "share_program", length = 255)
    private String shareProgram;

    @ManyToOne(optional = false)
    @NotNull
    private Provider provider;

    @ManyToOne(optional = false)
    @NotNull
    private StatusType statusType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Provider getProvider() {
        return provider;
    }

    public Program provider(Provider provider) {
        this.provider = provider;
        return this;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public StatusType getStatusType() {
        return statusType;
    }

    public Program statusType(StatusType statusType) {
        this.statusType = statusType;
        return this;
    }

    public void setStatusType(StatusType statusType) {
        this.statusType = statusType;
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
            ", name='" + getName() + "'" +
            ", highlight='" + getHighlight() + "'" +
            ", dateTo='" + getDateTo() + "'" +
            ", dateFrom='" + getDateFrom() + "'" +
            ", shareProgram='" + getShareProgram() + "'" +
            "}";
    }
}
