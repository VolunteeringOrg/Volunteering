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
 * A Provider.
 */
@Entity
@Table(name = "provider")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "provider")
public class Provider implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @Size(max = 255)
    @Column(name = "logo_filepath", length = 255)
    private String logoFilepath;

    @Size(max = 1000)
    @Column(name = "summary", length = 1000)
    private String summary;

    @OneToMany(mappedBy = "provider")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Program> programs = new HashSet<>();

    @OneToMany(mappedBy = "provider")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Link> links = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Provider name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogoFilepath() {
        return logoFilepath;
    }

    public Provider logoFilepath(String logoFilepath) {
        this.logoFilepath = logoFilepath;
        return this;
    }

    public void setLogoFilepath(String logoFilepath) {
        this.logoFilepath = logoFilepath;
    }

    public String getSummary() {
        return summary;
    }

    public Provider summary(String summary) {
        this.summary = summary;
        return this;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Set<Program> getPrograms() {
        return programs;
    }

    public Provider programs(Set<Program> programs) {
        this.programs = programs;
        return this;
    }

    public Provider addProgram(Program program) {
        this.programs.add(program);
        program.setProvider(this);
        return this;
    }

    public Provider removeProgram(Program program) {
        this.programs.remove(program);
        program.setProvider(null);
        return this;
    }

    public void setPrograms(Set<Program> programs) {
        this.programs = programs;
    }

    public Set<Link> getLinks() {
        return links;
    }

    public Provider links(Set<Link> links) {
        this.links = links;
        return this;
    }

    public Provider addLink(Link link) {
        this.links.add(link);
        link.setProvider(this);
        return this;
    }

    public Provider removeLink(Link link) {
        this.links.remove(link);
        link.setProvider(null);
        return this;
    }

    public void setLinks(Set<Link> links) {
        this.links = links;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Provider provider = (Provider) o;
        if (provider.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), provider.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Provider{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", logoFilepath='" + getLogoFilepath() + "'" +
            ", summary='" + getSummary() + "'" +
            "}";
    }
}
