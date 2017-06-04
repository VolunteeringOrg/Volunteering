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

    @NotNull
    @Size(max = 255)
    @Column(name = "logo_path", length = 255, nullable = false)
    private String logoPath;

    @OneToMany(mappedBy = "linkType")
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

    public LinkType name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public LinkType logoPath(String logoPath) {
        this.logoPath = logoPath;
        return this;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }

    public Set<Link> getLinks() {
        return links;
    }

    public LinkType links(Set<Link> links) {
        this.links = links;
        return this;
    }

    public LinkType addLink(Link link) {
        this.links.add(link);
        link.setLinkType(this);
        return this;
    }

    public LinkType removeLink(Link link) {
        this.links.remove(link);
//        link.setLinkType(null);   //LinkType is a Dictionary
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
            ", logoPath='" + getLogoPath() + "'" +
            "}";
    }
}
