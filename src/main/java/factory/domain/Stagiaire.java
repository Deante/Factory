package factory.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import factory.domain.enumeration.NiveauEnum;
import com.fasterxml.jackson.annotation.JsonView;


/**
 * A Stagiaire.
 */
@Entity
@Table(name = "stagiaire")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "stagiaire")
public class Stagiaire implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @JsonView(Views.Common.class)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "niveau")
    @JsonView(Views.Common.class)
    private NiveauEnum niveau;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "stagiaire_ordinateurs",
               joinColumns = @JoinColumn(name="stagiaires_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="ordinateurs_id", referencedColumnName="id"))
    @JsonView(Views.Common.class)
    private Set<Ordinateur> ordinateurs = new HashSet<>();

    @ManyToOne
    @JsonView(Views.Stagiaire.class)
    private Formation formation;

    @ManyToOne
    @JsonView(Views.Stagiaire.class)
    private Ordinateur ordinateur;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public NiveauEnum getNiveau() {
        return niveau;
    }

    public Stagiaire niveau(NiveauEnum niveau) {
        this.niveau = niveau;
        return this;
    }

    public void setNiveau(NiveauEnum niveau) {
        this.niveau = niveau;
    }

    public Set<Ordinateur> getOrdinateurs() {
        return ordinateurs;
    }

    public Stagiaire ordinateurs(Set<Ordinateur> ordinateurs) {
        this.ordinateurs = ordinateurs;
        return this;
    }

    public Stagiaire addOrdinateurs(Ordinateur ordinateur) {
        this.ordinateurs.add(ordinateur);
        ordinateur.getStagiaires().add(this);
        return this;
    }

    public Stagiaire removeOrdinateurs(Ordinateur ordinateur) {
        this.ordinateurs.remove(ordinateur);
        ordinateur.getStagiaires().remove(this);
        return this;
    }

    public void setOrdinateurs(Set<Ordinateur> ordinateurs) {
        this.ordinateurs = ordinateurs;
    }

    public Formation getFormation() {
        return formation;
    }

    public Stagiaire formation(Formation formation) {
        this.formation = formation;
        return this;
    }

    public void setFormation(Formation formation) {
        this.formation = formation;
    }

    public Ordinateur getOrdinateur() {
        return ordinateur;
    }

    public Stagiaire ordinateur(Ordinateur ordinateur) {
        this.ordinateur = ordinateur;
        return this;
    }

    public void setOrdinateur(Ordinateur ordinateur) {
        this.ordinateur = ordinateur;
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
        Stagiaire stagiaire = (Stagiaire) o;
        if (stagiaire.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), stagiaire.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Stagiaire{" +
            "id=" + getId() +
            ", niveau='" + getNiveau() + "'" +
            "}";
    }
}
