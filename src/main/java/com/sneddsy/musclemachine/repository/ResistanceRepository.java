package com.sneddsy.musclemachine.repository;

import com.sneddsy.musclemachine.domain.Resistance;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Resistance entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ResistanceRepository extends JpaRepository<Resistance, Long> {}
