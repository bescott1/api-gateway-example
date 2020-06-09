package com.ippon.gateway.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A ApiKey.
 */
@Entity
@Table(name = "api_key")
public class ApiKey implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "client_id", nullable = false)
    private String clientId;


    @ManyToMany
    @JoinTable(name = "api_key_authorities",
               joinColumns = @JoinColumn(name = "api_key_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "authority_name", referencedColumnName = "name"))
    private Set<Authority> authorities = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public ApiKey description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getClientId() {
        return clientId;
    }

    public ApiKey clientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public ApiKey authorities(Set<Authority> authorities) {
        this.authorities = authorities;
        return this;
    }

    public ApiKey addAuthorities(Authority authority) {
        this.authorities.add(authority);
        return this;
    }

    public ApiKey removeAuthorities(Authority authority) {
        this.authorities.remove(authority);
        return this;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ApiKey)) {
            return false;
        }
        return id != null && id.equals(((ApiKey) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ApiKey{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", clientId='" + getClientId() + "'" +
            "}";
    }
}
