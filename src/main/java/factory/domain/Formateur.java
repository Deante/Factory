package factory.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Formateur.
 */
@Entity
@Table(name = "formateur")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "formateur")
public class Formateur implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "formateur_competences",
               joinColumns = @JoinColumn(name="formateurs_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="competences_id", referencedColumnName="id"))
    private Set<Competence> competences = new HashSet<>();

    @ManyToMany(mappedBy = "formateurs")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Formation> formations = new HashSet<>();

    @ManyToMany(mappedBy = "formateurs")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Module> modules = new HashSet<>();

    @OneToMany(mappedBy = "formateur")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Disponibilite> disponibilites = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public Formateur user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Competence> getCompetences() {
        return competences;
    }

    public Formateur competences(Set<Competence> competences) {
        this.competences = competences;
        return this;
    }

    public Formateur addCompetences(Competence competence) {
        this.competences.add(competence);
        competence.getFormateurs().add(this);
        return this;
    }

    public Formateur removeCompetences(Competence competence) {
        this.competences.remove(competence);
        competence.getFormateurs().remove(this);
        return this;
    }

    public void setCompetences(Set<Competence> competences) {
        this.competences = competences;
    }

    public Set<Formation> getFormations() {
        return formations;
    }

    public Formateur formations(Set<Formation> formations) {
        this.formations = formations;
        return this;
    }

    public Formateur addFormations(Formation formation) {
        this.formations.add(formation);
        formation.getFormateurs().add(this);
        return this;
    }

    public Formateur removeFormations(Formation formation) {
        this.formations.remove(formation);
        formation.getFormateurs().remove(this);
        return this;
    }

    public void setFormations(Set<Formation> formations) {
        this.formations = formations;
    }

    public Set<Module> getModules() {
        return modules;
    }

    public Formateur modules(Set<Module> modules) {
        this.modules = modules;
        return this;
    }

    public Formateur addModules(Module module) {
        this.modules.add(module);
        module.getFormateurs().add(this);
        return this;
    }

    public Formateur removeModules(Module module) {
        this.modules.remove(module);
        module.getFormateurs().remove(this);
        return this;
    }

    public void setModules(Set<Module> modules) {
        this.modules = modules;
    }

    public Set<Disponibilite> getDisponibilites() {
        return disponibilites;
    }

    public Formateur disponibilites(Set<Disponibilite> disponibilites) {
        this.disponibilites = disponibilites;
        return this;
    }

    public Formateur addDisponibilites(Disponibilite disponibilite) {
        this.disponibilites.add(disponibilite);
        disponibilite.setFormateur(this);
        return this;
    }

    public Formateur removeDisponibilites(Disponibilite disponibilite) {
        this.disponibilites.remove(disponibilite);
        disponibilite.setFormateur(null);
        return this;
    }

    public void setDisponibilites(Set<Disponibilite> disponibilites) {
        this.disponibilites = disponibilites;
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
        Formateur formateur = (Formateur) o;
        if (formateur.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), formateur.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Formateur{" +
            "id=" + getId() +
            "}";
    }
}
