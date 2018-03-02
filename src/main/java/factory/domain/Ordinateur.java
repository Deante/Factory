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

import factory.domain.enumeration.EtatMaterielEnum;

/**
 * A Ordinateur.
 */
@Entity
@Table(name = "ordinateur")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "ordinateur")
public class Ordinateur implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(min = 3, max = 8)
    @Column(name = "code", length = 8, nullable = false)
    private String code;

    @Size(min = 2, max = 20)
    @Column(name = "processeur", length = 20)
    private String processeur;

    @Min(value = 8)
    @Max(value = 64)
    @Column(name = "memoire")
    private Integer memoire;

    @Min(value = 128)
    @Max(value = 8000)
    @Column(name = "disque")
    private Integer disque;

    @Column(name = "annee_achat")
    private String anneeAchat;

    @Column(name = "cout_jour")
    private Double coutJour;

    @Enumerated(EnumType.STRING)
    @Column(name = "etat")
    private EtatMaterielEnum etat;

    @OneToMany(mappedBy = "ordinateur")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Stagiaire> stagiaires = new HashSet<>();

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

    public Ordinateur code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getProcesseur() {
        return processeur;
    }

    public Ordinateur processeur(String processeur) {
        this.processeur = processeur;
        return this;
    }

    public void setProcesseur(String processeur) {
        this.processeur = processeur;
    }

    public Integer getMemoire() {
        return memoire;
    }

    public Ordinateur memoire(Integer memoire) {
        this.memoire = memoire;
        return this;
    }

    public void setMemoire(Integer memoire) {
        this.memoire = memoire;
    }

    public Integer getDisque() {
        return disque;
    }

    public Ordinateur disque(Integer disque) {
        this.disque = disque;
        return this;
    }

    public void setDisque(Integer disque) {
        this.disque = disque;
    }

    public String getAnneeAchat() {
        return anneeAchat;
    }

    public Ordinateur anneeAchat(String anneeAchat) {
        this.anneeAchat = anneeAchat;
        return this;
    }

    public void setAnneeAchat(String anneeAchat) {
        this.anneeAchat = anneeAchat;
    }

    public Double getCoutJour() {
        return coutJour;
    }

    public Ordinateur coutJour(Double coutJour) {
        this.coutJour = coutJour;
        return this;
    }

    public void setCoutJour(Double coutJour) {
        this.coutJour = coutJour;
    }

    public EtatMaterielEnum getEtat() {
        return etat;
    }

    public Ordinateur etat(EtatMaterielEnum etat) {
        this.etat = etat;
        return this;
    }

    public void setEtat(EtatMaterielEnum etat) {
        this.etat = etat;
    }

    public Set<Stagiaire> getStagiaires() {
        return stagiaires;
    }

    public Ordinateur stagiaires(Set<Stagiaire> stagiaires) {
        this.stagiaires = stagiaires;
        return this;
    }

    public Ordinateur addStagiaires(Stagiaire stagiaire) {
        this.stagiaires.add(stagiaire);
        stagiaire.setOrdinateur(this);
        return this;
    }

    public Ordinateur removeStagiaires(Stagiaire stagiaire) {
        this.stagiaires.remove(stagiaire);
        stagiaire.setOrdinateur(null);
        return this;
    }

    public void setStagiaires(Set<Stagiaire> stagiaires) {
        this.stagiaires = stagiaires;
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
        Ordinateur ordinateur = (Ordinateur) o;
        if (ordinateur.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), ordinateur.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Ordinateur{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", processeur='" + getProcesseur() + "'" +
            ", memoire=" + getMemoire() +
            ", disque=" + getDisque() +
            ", anneeAchat='" + getAnneeAchat() + "'" +
            ", coutJour=" + getCoutJour() +
            ", etat='" + getEtat() + "'" +
            "}";
    }
}
