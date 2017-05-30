package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A LinkType.
 */
@Entity
@Table(name = "link_type")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "linktype")
public class LinkType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @Column(name = "logo_filepath")
    private String logoFilepath;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public LinkType name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogoFilepath() {
        return logoFilepath;
    }

    public LinkType logoFilepath(String logoFilepath) {
        this.logoFilepath = logoFilepath;
        return this;
    }

    public void setLogoFilepath(String logoFilepath) {
        this.logoFilepath = logoFilepath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LinkType linkType = (LinkType) o;
        if (linkType.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), linkType.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "LinkType{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", logoFilepath='" + getLogoFilepath() + "'" +
            "}";
    }
}
