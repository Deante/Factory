package factory.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

import factory.domain.enumeration.NiveauEnum;

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
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "niveau")
    private NiveauEnum niveau;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @ManyToOne
    private Formation formation;

    @ManyToOne
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

    public User getUser() {
        return user;
    }

    public Stagiaire user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
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
