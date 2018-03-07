package factory.repository;

import factory.domain.Module;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Spring Data JPA repository for the Module entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ModuleRepository extends JpaRepository<Module, Long> {
    @Query("select distinct module from Module module left join fetch module.matieres left join fetch module.formateurs")
    List<Module> findAllWithEagerRelationships();

    @Query("select module from Module module left join fetch module.matieres left join fetch module.formateurs where module.id =:id")
    Module findOneWithEagerRelationships(@Param("id") Long id);

}
