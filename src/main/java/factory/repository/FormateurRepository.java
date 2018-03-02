package factory.repository;

import factory.domain.Formateur;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Spring Data JPA repository for the Formateur entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FormateurRepository extends JpaRepository<Formateur, Long> {
    @Query("select distinct formateur from Formateur formateur left join fetch formateur.competences")
    List<Formateur> findAllWithEagerRelationships();

    @Query("select formateur from Formateur formateur left join fetch formateur.competences where formateur.id =:id")
    Formateur findOneWithEagerRelationships(@Param("id") Long id);

}
