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

import factory.domain.enumeration.NiveauEnum;

/**
 * A Competence.
 */
@Entity
@Table(name = "competence")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "competence")
public class Competence implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(min = 3, max = 20)
    @Column(name = "nom", length = 20, nullable = false)
    private String nom;

    @Enumerated(EnumType.STRING)
    @Column(name = "niveau")
    private NiveauEnum niveau;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "competence_matieres",
               joinColumns = @JoinColumn(name="competences_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="matieres_id", referencedColumnName="id"))
    private Set<Matiere> matieres = new HashSet<>();

    @ManyToMany(mappedBy = "competences")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Formateur> formateurs = new HashSet<>();

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

    public Competence nom(String nom) {
        this.nom = nom;
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public NiveauEnum getNiveau() {
        return niveau;
    }

    public Competence niveau(NiveauEnum niveau) {
        this.niveau = niveau;
        return this;
    }

    public void setNiveau(NiveauEnum niveau) {
        this.niveau = niveau;
    }

    public Set<Matiere> getMatieres() {
        return matieres;
    }

    public Competence matieres(Set<Matiere> matieres) {
        this.matieres = matieres;
        return this;
    }

    public Competence addMatieres(Matiere matiere) {
        this.matieres.add(matiere);
        matiere.getCompetences().add(this);
        return this;
    }

    public Competence removeMatieres(Matiere matiere) {
        this.matieres.remove(matiere);
        matiere.getCompetences().remove(this);
        return this;
    }

    public void setMatieres(Set<Matiere> matieres) {
        this.matieres = matieres;
    }

    public Set<Formateur> getFormateurs() {
        return formateurs;
    }

    public Competence formateurs(Set<Formateur> formateurs) {
        this.formateurs = formateurs;
        return this;
    }

    public Competence addFormateurs(Formateur formateur) {
        this.formateurs.add(formateur);
        formateur.getCompetences().add(this);
        return this;
    }

    public Competence removeFormateurs(Formateur formateur) {
        this.formateurs.remove(formateur);
        formateur.getCompetences().remove(this);
        return this;
    }

    public void setFormateurs(Set<Formateur> formateurs) {
        this.formateurs = formateurs;
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
        Competence competence = (Competence) o;
        if (competence.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), competence.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Competence{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", niveau='" + getNiveau() + "'" +
            "}";
    }
}
