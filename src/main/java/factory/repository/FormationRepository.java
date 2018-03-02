package factory.repository;

import factory.domain.Formation;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Spring Data JPA repository for the Formation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FormationRepository extends JpaRepository<Formation, Long> {
    @Query("select distinct formation from Formation formation left join fetch formation.formateurs left join fetch formation.modules")
    List<Formation> findAllWithEagerRelationships();

    @Query("select formation from Formation formation left join fetch formation.formateurs left join fetch formation.modules where formation.id =:id")
    Formation findOneWithEagerRelationships(@Param("id") Long id);

}
