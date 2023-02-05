package com.sneddsy.muscle.repository;

import com.sneddsy.muscle.domain.WorkSet;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the WorkSet entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WorkSetRepository extends JpaRepository<WorkSet, Long> {}
