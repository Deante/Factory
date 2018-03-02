package factory.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Formation.
 */
@Entity
@Table(name = "formation")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "formation")
public class Formation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(min = 3, max = 50)
    @Column(name = "nom", length = 50, nullable = false)
    private String nom;

    @NotNull
    @Column(name = "date_debut_form", nullable = false)
    private LocalDate dateDebutForm;

    @NotNull
    @Column(name = "date_fin_form", nullable = false)
    private LocalDate dateFinForm;

    @Size(min = 0, max = 250)
    @Column(name = "objectifs", length = 250)
    private String objectifs;

    @ManyToOne
    private Departement departement;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "formation_formateurs",
               joinColumns = @JoinColumn(name="formations_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="formateurs_id", referencedColumnName="id"))
    private Set<Formateur> formateurs = new HashSet<>();

    @ManyToOne
    private Gestionnaire gestionnaire;

    @OneToMany(mappedBy = "formation")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Stagiaire> stagiaires = new HashSet<>();

    @ManyToOne
    private Salle salle;

    @ManyToOne
    private Technicien technicien;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "formation_modules",
               joinColumns = @JoinColumn(name="formations_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="modules_id", referencedColumnName="id"))
    private Set<Module> modules = new HashSet<>();

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

    public Formation nom(String nom) {
        this.nom = nom;
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public LocalDate getDateDebutForm() {
        return dateDebutForm;
    }

    public Formation dateDebutForm(LocalDate dateDebutForm) {
        this.dateDebutForm = dateDebutForm;
        return this;
    }

    public void setDateDebutForm(LocalDate dateDebutForm) {
        this.dateDebutForm = dateDebutForm;
    }

    public LocalDate getDateFinForm() {
        return dateFinForm;
    }

    public Formation dateFinForm(LocalDate dateFinForm) {
        this.dateFinForm = dateFinForm;
        return this;
    }

    public void setDateFinForm(LocalDate dateFinForm) {
        this.dateFinForm = dateFinForm;
    }

    public String getObjectifs() {
        return objectifs;
    }

    public Formation objectifs(String objectifs) {
        this.objectifs = objectifs;
        return this;
    }

    public void setObjectifs(String objectifs) {
        this.objectifs = objectifs;
    }

    public Departement getDepartement() {
        return departement;
    }

    public Formation departement(Departement departement) {
        this.departement = departement;
        return this;
    }

    public void setDepartement(Departement departement) {
        this.departement = departement;
    }

    public Set<Formateur> getFormateurs() {
        return formateurs;
    }

    public Formation formateurs(Set<Formateur> formateurs) {
        this.formateurs = formateurs;
        return this;
    }

    public Formation addFormateurs(Formateur formateur) {
        this.formateurs.add(formateur);
        formateur.getFormations().add(this);
        return this;
    }

    public Formation removeFormateurs(Formateur formateur) {
        this.formateurs.remove(formateur);
        formateur.getFormations().remove(this);
        return this;
    }

    public void setFormateurs(Set<Formateur> formateurs) {
        this.formateurs = formateurs;
    }

    public Gestionnaire getGestionnaire() {
        return gestionnaire;
    }

    public Formation gestionnaire(Gestionnaire gestionnaire) {
        this.gestionnaire = gestionnaire;
        return this;
    }

    public void setGestionnaire(Gestionnaire gestionnaire) {
        this.gestionnaire = gestionnaire;
    }

    public Set<Stagiaire> getStagiaires() {
        return stagiaires;
    }

    public Formation stagiaires(Set<Stagiaire> stagiaires) {
        this.stagiaires = stagiaires;
        return this;
    }

    public Formation addStagiaires(Stagiaire stagiaire) {
        this.stagiaires.add(stagiaire);
        stagiaire.setFormation(this);
        return this;
    }

    public Formation removeStagiaires(Stagiaire stagiaire) {
        this.stagiaires.remove(stagiaire);
        stagiaire.setFormation(null);
        return this;
    }

    public void setStagiaires(Set<Stagiaire> stagiaires) {
        this.stagiaires = stagiaires;
    }

    public Salle getSalle() {
        return salle;
    }

    public Formation salle(Salle salle) {
        this.salle = salle;
        return this;
    }

    public void setSalle(Salle salle) {
        this.salle = salle;
    }

    public Technicien getTechnicien() {
        return technicien;
    }

    public Formation technicien(Technicien technicien) {
        this.technicien = technicien;
        return this;
    }

    public void setTechnicien(Technicien technicien) {
        this.technicien = technicien;
    }

    public Set<Module> getModules() {
        return modules;
    }

    public Formation modules(Set<Module> modules) {
        this.modules = modules;
        return this;
    }

    public Formation addModules(Module module) {
        this.modules.add(module);
        module.getFormations().add(this);
        return this;
    }

    public Formation removeModules(Module module) {
        this.modules.remove(module);
        module.getFormations().remove(this);
        return this;
    }

    public void setModules(Set<Module> modules) {
        this.modules = modules;
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
        Formation formation = (Formation) o;
        if (formation.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), formation.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Formation{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", dateDebutForm='" + getDateDebutForm() + "'" +
            ", dateFinForm='" + getDateFinForm() + "'" +
            ", objectifs='" + getObjectifs() + "'" +
            "}";
    }
}
