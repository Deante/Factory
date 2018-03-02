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
 * A Matiere.
 */
@Entity
@Table(name = "matiere")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "matiere")
public class Matiere implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(min = 3, max = 20)
    @Column(name = "nom", length = 20, nullable = false)
    private String nom;

    @ManyToMany(mappedBy = "matieres")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Module> modules = new HashSet<>();

    @ManyToMany(mappedBy = "matieres")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Competence> competences = new HashSet<>();

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

    public Matiere nom(String nom) {
        this.nom = nom;
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Set<Module> getModules() {
        return modules;
    }

    public Matiere modules(Set<Module> modules) {
        this.modules = modules;
        return this;
    }

    public Matiere addModules(Module module) {
        this.modules.add(module);
        module.getMatieres().add(this);
        return this;
    }

    public Matiere removeModules(Module module) {
        this.modules.remove(module);
        module.getMatieres().remove(this);
        return this;
    }

    public void setModules(Set<Module> modules) {
        this.modules = modules;
    }

    public Set<Competence> getCompetences() {
        return competences;
    }

    public Matiere competences(Set<Competence> competences) {
        this.competences = competences;
        return this;
    }

    public Matiere addCompetences(Competence competence) {
        this.competences.add(competence);
        competence.getMatieres().add(this);
        return this;
    }

    public Matiere removeCompetences(Competence competence) {
        this.competences.remove(competence);
        competence.getMatieres().remove(this);
        return this;
    }

    public void setCompetences(Set<Competence> competences) {
        this.competences = competences;
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
        Matiere matiere = (Matiere) o;
        if (matiere.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), matiere.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Matiere{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            "}";
    }
}
