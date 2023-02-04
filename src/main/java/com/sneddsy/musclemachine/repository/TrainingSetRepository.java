package com.sneddsy.musclemachine.repository;

import com.sneddsy.musclemachine.domain.TrainingSet;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TrainingSet entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TrainingSetRepository extends JpaRepository<TrainingSet, Long> {}
