package com.sneddsy.muscle.repository;

import com.sneddsy.muscle.domain.StrengthWorkout;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the StrengthWorkout entity.
 */
@Repository
public interface StrengthWorkoutRepository extends JpaRepository<StrengthWorkout, Long> {
    @Query("select strengthWorkout from StrengthWorkout strengthWorkout where strengthWorkout.user.login = ?#{principal.username}")
    List<StrengthWorkout> findByUserIsCurrentUser();

    default Optional<StrengthWorkout> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<StrengthWorkout> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<StrengthWorkout> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct strengthWorkout from StrengthWorkout strengthWorkout left join fetch strengthWorkout.user",
        countQuery = "select count(distinct strengthWorkout) from StrengthWorkout strengthWorkout"
    )
    Page<StrengthWorkout> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct strengthWorkout from StrengthWorkout strengthWorkout left join fetch strengthWorkout.user")
    List<StrengthWorkout> findAllWithToOneRelationships();

    @Query("select strengthWorkout from StrengthWorkout strengthWorkout left join fetch strengthWorkout.user where strengthWorkout.id =:id")
    Optional<StrengthWorkout> findOneWithToOneRelationships(@Param("id") Long id);
}
