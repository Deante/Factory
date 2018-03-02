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
 * A Site.
 */
@Entity
@Table(name = "site")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "site")
public class Site implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "nom", length = 20, nullable = false)
    private String nom;

    @Size(min = 8, max = 50)
    @Column(name = "voie", length = 50)
    private String voie;

    @Size(min = 0, max = 20)
    @Column(name = "complement", length = 20)
    private String complement;

    @Size(min = 5, max = 5)
    @Column(name = "code_postal", length = 5)
    private String codePostal;

    @Size(min = 2, max = 50)
    @Column(name = "ville", length = 50)
    private String ville;

    @Size(min = 10, max = 10)
    @Column(name = "telephone", length = 10)
    private String telephone;

    @OneToMany(mappedBy = "site")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Departement> departements = new HashSet<>();

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

    public Site nom(String nom) {
        this.nom = nom;
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getVoie() {
        return voie;
    }

    public Site voie(String voie) {
        this.voie = voie;
        return this;
    }

    public void setVoie(String voie) {
        this.voie = voie;
    }

    public String getComplement() {
        return complement;
    }

    public Site complement(String complement) {
        this.complement = complement;
        return this;
    }

    public void setComplement(String complement) {
        this.complement = complement;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public Site codePostal(String codePostal) {
        this.codePostal = codePostal;
        return this;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    public String getVille() {
        return ville;
    }

    public Site ville(String ville) {
        this.ville = ville;
        return this;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getTelephone() {
        return telephone;
    }

    public Site telephone(String telephone) {
        this.telephone = telephone;
        return this;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Set<Departement> getDepartements() {
        return departements;
    }

    public Site departements(Set<Departement> departements) {
        this.departements = departements;
        return this;
    }

    public Site addDepartements(Departement departement) {
        this.departements.add(departement);
        departement.setSite(this);
        return this;
    }

    public Site removeDepartements(Departement departement) {
        this.departements.remove(departement);
        departement.setSite(null);
        return this;
    }

    public void setDepartements(Set<Departement> departements) {
        this.departements = departements;
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
        Site site = (Site) o;
        if (site.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), site.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Site{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", voie='" + getVoie() + "'" +
            ", complement='" + getComplement() + "'" +
            ", codePostal='" + getCodePostal() + "'" +
            ", ville='" + getVille() + "'" +
            ", telephone='" + getTelephone() + "'" +
            "}";
    }
}
