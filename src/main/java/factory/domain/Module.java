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

import factory.domain.enumeration.CouleurEnum;

/**
 * A Module.
 */
@Entity
@Table(name = "module")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "module")
public class Module implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(min = 3, max = 20)
    @Column(name = "titre", length = 20, nullable = false)
    private String titre;

    @NotNull
    @Column(name = "duree", nullable = false)
    private Double duree;

    @Enumerated(EnumType.STRING)
    @Column(name = "couleur")
    private CouleurEnum couleur;

    @Column(name = "prerequis")
    private String prerequis;

    @Column(name = "contenu")
    private String contenu;

    @ManyToMany(mappedBy = "modules")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Formation> formations = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "module_matieres",
               joinColumns = @JoinColumn(name="modules_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="matieres_id", referencedColumnName="id"))
    private Set<Matiere> matieres = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "module_formateurs",
               joinColumns = @JoinColumn(name="modules_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="formateurs_id", referencedColumnName="id"))
    private Set<Formateur> formateurs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public Module titre(String titre) {
        this.titre = titre;
        return this;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public Double getDuree() {
        return duree;
    }

    public Module duree(Double duree) {
        this.duree = duree;
        return this;
    }

    public void setDuree(Double duree) {
        this.duree = duree;
    }

    public CouleurEnum getCouleur() {
        return couleur;
    }

    public Module couleur(CouleurEnum couleur) {
        this.couleur = couleur;
        return this;
    }

    public void setCouleur(CouleurEnum couleur) {
        this.couleur = couleur;
    }

    public String getPrerequis() {
        return prerequis;
    }

    public Module prerequis(String prerequis) {
        this.prerequis = prerequis;
        return this;
    }

    public void setPrerequis(String prerequis) {
        this.prerequis = prerequis;
    }

    public String getContenu() {
        return contenu;
    }

    public Module contenu(String contenu) {
        this.contenu = contenu;
        return this;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public Set<Formation> getFormations() {
        return formations;
    }

    public Module formations(Set<Formation> formations) {
        this.formations = formations;
        return this;
    }

    public Module addFormations(Formation formation) {
        this.formations.add(formation);
        formation.getModules().add(this);
        return this;
    }

    public Module removeFormations(Formation formation) {
        this.formations.remove(formation);
        formation.getModules().remove(this);
        return this;
    }

    public void setFormations(Set<Formation> formations) {
        this.formations = formations;
    }

    public Set<Matiere> getMatieres() {
        return matieres;
    }

    public Module matieres(Set<Matiere> matieres) {
        this.matieres = matieres;
        return this;
    }

    public Module addMatieres(Matiere matiere) {
        this.matieres.add(matiere);
        matiere.getModules().add(this);
        return this;
    }

    public Module removeMatieres(Matiere matiere) {
        this.matieres.remove(matiere);
        matiere.getModules().remove(this);
        return this;
    }

    public void setMatieres(Set<Matiere> matieres) {
        this.matieres = matieres;
    }

    public Set<Formateur> getFormateurs() {
        return formateurs;
    }

    public Module formateurs(Set<Formateur> formateurs) {
        this.formateurs = formateurs;
        return this;
    }

    public Module addFormateurs(Formateur formateur) {
        this.formateurs.add(formateur);
        formateur.getModules().add(this);
        return this;
    }

    public Module removeFormateurs(Formateur formateur) {
        this.formateurs.remove(formateur);
        formateur.getModules().remove(this);
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
        Module module = (Module) o;
        if (module.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), module.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Module{" +
            "id=" + getId() +
            ", titre='" + getTitre() + "'" +
            ", duree=" + getDuree() +
            ", couleur='" + getCouleur() + "'" +
            ", prerequis='" + getPrerequis() + "'" +
            ", contenu='" + getContenu() + "'" +
            "}";
    }
}
