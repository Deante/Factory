package factory.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Departement.
 */
@Entity
@Table(name = "departement")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "departement")
public class Departement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(min = 2, max = 50)
    @Column(name = "nom", length = 50, nullable = false)
    private String nom;

    @Column(name = "etages")
    private Integer etages;

    @ManyToOne
    private Site site;

    @OneToMany(mappedBy = "departement")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Formation> formations = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public Departement nom(String nom) {
        this.nom = nom;
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Integer getEtages() {
        return etages;
    }

    public Departement etages(Integer etages) {
        this.etages = etages;
        return this;
    }

    public void setEtages(Integer etages) {
        this.etages = etages;
    }

    public Site getSite() {
        return site;
    }

    public Departement site(Site site) {
        this.site = site;
        return this;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public Set<Formation> getFormations() {
        return formations;
    }

    public Departement formations(Set<Formation> formations) {
        this.formations = formations;
        return this;
    }

    public Departement addFormations(Formation formation) {
        this.formations.add(formation);
        formation.setDepartement(this);
        return this;
    }

    public Departement removeFormations(Formation formation) {
        this.formations.remove(formation);
        formation.setDepartement(null);
        return this;
    }

    public void setFormations(Set<Formation> formations) {
        this.formations = formations;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Departement departement = (Departement) o;
        if (departement.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), departement.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Departement{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", etages=" + getEtages() +
            "}";
    }
}
