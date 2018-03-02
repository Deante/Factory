package factory.repository;

import factory.domain.Competence;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Spring Data JPA repository for the Competence entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CompetenceRepository extends JpaRepository<Competence, Long> {
    @Query("select distinct competence from Competence competence left join fetch competence.matieres")
    List<Competence> findAllWithEagerRelationships();

    @Query("select competence from Competence competence left join fetch competence.matieres where competence.id =:id")
    Competence findOneWithEagerRelationships(@Param("id") Long id);

}
