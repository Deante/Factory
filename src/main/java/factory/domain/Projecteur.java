package factory.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

import factory.domain.enumeration.EtatMaterielEnum;

/**
 * A Projecteur.
 */
@Entity
@Table(name = "projecteur")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "projecteur")
public class Projecteur implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(min = 3, max = 8)
    @Column(name = "code", length = 8, nullable = false)
    private String code;

    @Column(name = "cout_jour")
    private Double coutJour;

    @Enumerated(EnumType.STRING)
    @Column(name = "etat")
    private EtatMaterielEnum etat;

    @OneToOne(mappedBy = "projecteur")
    @JsonIgnore
    private Salle salle;

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

    public Projecteur code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Double getCoutJour() {
        return coutJour;
    }

    public Projecteur coutJour(Double coutJour) {
        this.coutJour = coutJour;
        return this;
    }

    public void setCoutJour(Double coutJour) {
        this.coutJour = coutJour;
    }

    public EtatMaterielEnum getEtat() {
        return etat;
    }

    public Projecteur etat(EtatMaterielEnum etat) {
        this.etat = etat;
        return this;
    }

    public void setEtat(EtatMaterielEnum etat) {
        this.etat = etat;
    }

    public Salle getSalle() {
        return salle;
    }

    public Projecteur salle(Salle salle) {
        this.salle = salle;
        return this;
    }

    public void setSalle(Salle salle) {
        this.salle = salle;
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
        Projecteur projecteur = (Projecteur) o;
        if (projecteur.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), projecteur.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Projecteur{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", coutJour=" + getCoutJour() +
            ", etat='" + getEtat() + "'" +
            "}";
    }
}
