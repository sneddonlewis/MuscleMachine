package com.sneddsy.musclemachine.service;

import com.sneddsy.musclemachine.domain.StrengthWorkout;
import com.sneddsy.musclemachine.repository.StrengthWorkoutRepository;
import com.sneddsy.musclemachine.web.rest.vm.workout.StrengthWorkoutVM;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link StrengthWorkout}.
 */
@Service
@Transactional
public class StrengthWorkoutService {

    private final Logger log = LoggerFactory.getLogger(StrengthWorkoutService.class);

    private final StrengthWorkoutRepository strengthWorkoutRepository;

    public StrengthWorkoutService(StrengthWorkoutRepository strengthWorkoutRepository) {
        this.strengthWorkoutRepository = strengthWorkoutRepository;
    }

    /**
     * Creates a new Strength workout and persists the associated data for the user.
     * @return
     */
    public StrengthWorkout create(StrengthWorkoutVM request) {
        log.debug("Request to create a new complete StrengthWorkout : {}", request);
        return new StrengthWorkout();
    }

    /**
     * Save a strengthWorkout.
     *
     * @param strengthWorkout the entity to save.
     * @return the persisted entity.
     */
    public StrengthWorkout save(StrengthWorkout strengthWorkout) {
        log.debug("Request to save StrengthWorkout : {}", strengthWorkout);
        return strengthWorkoutRepository.save(strengthWorkout);
    }

    /**
     * Update a strengthWorkout.
     *
     * @param strengthWorkout the entity to save.
     * @return the persisted entity.
     */
    public StrengthWorkout update(StrengthWorkout strengthWorkout) {
        log.debug("Request to update StrengthWorkout : {}", strengthWorkout);
        return strengthWorkoutRepository.save(strengthWorkout);
    }

    /**
     * Partially update a strengthWorkout.
     *
     * @param strengthWorkout the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<StrengthWorkout> partialUpdate(StrengthWorkout strengthWorkout) {
        log.debug("Request to partially update StrengthWorkout : {}", strengthWorkout);

        return strengthWorkoutRepository
            .findById(strengthWorkout.getId())
            .map(existingStrengthWorkout -> {
                if (strengthWorkout.getTime() != null) {
                    existingStrengthWorkout.setTime(strengthWorkout.getTime());
                }

                return existingStrengthWorkout;
            })
            .map(strengthWorkoutRepository::save);
    }

    /**
     * Get all the strengthWorkouts.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<StrengthWorkout> findAll() {
        log.debug("Request to get all StrengthWorkouts");
        return strengthWorkoutRepository.findAll();
    }

    /**
     * Get all the strengthWorkouts with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<StrengthWorkout> findAllWithEagerRelationships(Pageable pageable) {
        return strengthWorkoutRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one strengthWorkout by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<StrengthWorkout> findOne(Long id) {
        log.debug("Request to get StrengthWorkout : {}", id);
        return strengthWorkoutRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the strengthWorkout by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete StrengthWorkout : {}", id);
        strengthWorkoutRepository.deleteById(id);
    }
}
