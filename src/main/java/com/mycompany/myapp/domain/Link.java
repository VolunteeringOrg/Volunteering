package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Link.
 */
@Entity
@Table(name = "jhi_link")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "link")
public class Link implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "provider_id", nullable = false)
    private Integer providerId;

    @NotNull
    @Column(name = "link_type_id", nullable = false)
    private Integer linkTypeId;

    @NotNull
    @Size(max = 255)
    @Column(name = "jhi_value", length = 255, nullable = false)
    private String value;

    @ManyToOne
    private Provider fk_provider_link;

    @ManyToOne
    private LinkType fk_linktype_link;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getProviderId() {
        return providerId;
    }

    public Link providerId(Integer providerId) {
        this.providerId = providerId;
        return this;
    }

    public void setProviderId(Integer providerId) {
        this.providerId = providerId;
    }

    public Integer getLinkTypeId() {
        return linkTypeId;
    }

    public Link linkTypeId(Integer linkTypeId) {
        this.linkTypeId = linkTypeId;
        return this;
    }

    public void setLinkTypeId(Integer linkTypeId) {
        this.linkTypeId = linkTypeId;
    }

    public String getValue() {
        return value;
    }

    public Link value(String value) {
        this.value = value;
        return this;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Provider getFk_provider_link() {
        return fk_provider_link;
    }

    public Link fk_provider_link(Provider provider) {
        this.fk_provider_link = provider;
        return this;
    }

    public void setFk_provider_link(Provider provider) {
        this.fk_provider_link = provider;
    }

    public LinkType getFk_linktype_link() {
        return fk_linktype_link;
    }

    public Link fk_linktype_link(LinkType linkType) {
        this.fk_linktype_link = linkType;
        return this;
    }

    public void setFk_linktype_link(LinkType linkType) {
        this.fk_linktype_link = linkType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Link link = (Link) o;
        if (link.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), link.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Link{" +
            "id=" + getId() +
            ", providerId='" + getProviderId() + "'" +
            ", linkTypeId='" + getLinkTypeId() + "'" +
            ", value='" + getValue() + "'" +
            "}";
    }
}
