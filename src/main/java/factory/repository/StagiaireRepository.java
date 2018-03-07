package factory.repository;

import factory.domain.Stagiaire;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Spring Data JPA repository for the Stagiaire entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StagiaireRepository extends JpaRepository<Stagiaire, Long> {
    @Query("select distinct stagiaire from Stagiaire stagiaire left join fetch stagiaire.ordinateurs")
    List<Stagiaire> findAllWithEagerRelationships();

    @Query("select stagiaire from Stagiaire stagiaire left join fetch stagiaire.ordinateurs where stagiaire.id =:id")
    Stagiaire findOneWithEagerRelationships(@Param("id") Long id);

}
