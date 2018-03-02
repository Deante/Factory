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
 * A Salle.
 */
@Entity
@Table(name = "salle")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "salle")
public class Salle implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(min = 3, max = 8)
    @Column(name = "code", length = 8, nullable = false)
    private String code;

    @Min(value = 2)
    @Max(value = 250)
    @Column(name = "capacite")
    private Integer capacite;

    @Min(value = 1)
    @Max(value = 20)
    @Column(name = "etage")
    private Integer etage;

    @OneToOne
    @JoinColumn(unique = true)
    private Projecteur projecteur;

    @OneToMany(mappedBy = "salle")
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

    public String getCode() {
        return code;
    }

    public Salle code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getCapacite() {
        return capacite;
    }

    public Salle capacite(Integer capacite) {
        this.capacite = capacite;
        return this;
    }

    public void setCapacite(Integer capacite) {
        this.capacite = capacite;
    }

    public Integer getEtage() {
        return etage;
    }

    public Salle etage(Integer etage) {
        this.etage = etage;
        return this;
    }

    public void setEtage(Integer etage) {
        this.etage = etage;
    }

    public Projecteur getProjecteur() {
        return projecteur;
    }

    public Salle projecteur(Projecteur projecteur) {
        this.projecteur = projecteur;
        return this;
    }

    public void setProjecteur(Projecteur projecteur) {
        this.projecteur = projecteur;
    }

    public Set<Formation> getFormations() {
        return formations;
    }

    public Salle formations(Set<Formation> formations) {
        this.formations = formations;
        return this;
    }

    public Salle addFormations(Formation formation) {
        this.formations.add(formation);
        formation.setSalle(this);
        return this;
    }

    public Salle removeFormations(Formation formation) {
        this.formations.remove(formation);
        formation.setSalle(null);
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
        Salle salle = (Salle) o;
        if (salle.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), salle.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Salle{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", capacite=" + getCapacite() +
            ", etage=" + getEtage() +
            "}";
    }
}
