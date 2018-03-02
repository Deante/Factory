package factory.repository;

import factory.domain.Stagiaire;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Stagiaire entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StagiaireRepository extends JpaRepository<Stagiaire, Long> {

}
