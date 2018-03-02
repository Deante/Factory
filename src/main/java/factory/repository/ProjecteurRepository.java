package factory.repository;

import factory.domain.Projecteur;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Projecteur entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProjecteurRepository extends JpaRepository<Projecteur, Long> {

}
