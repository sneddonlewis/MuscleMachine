package com.sneddsy.musclemachine.web.rest;

import com.sneddsy.musclemachine.domain.Exercise;
import com.sneddsy.musclemachine.repository.ExerciseRepository;
import com.sneddsy.musclemachine.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.sneddsy.musclemachine.domain.Exercise}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ExerciseResource {

    private final Logger log = LoggerFactory.getLogger(ExerciseResource.class);

    private static final String ENTITY_NAME = "exercise";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExerciseRepository exerciseRepository;

    public ExerciseResource(ExerciseRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
    }

    /**
     * {@code POST  /exercises} : Create a new exercise.
     *
     * @param exercise the exercise to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new exercise, or with status {@code 400 (Bad Request)} if the exercise has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/exercises")
    public ResponseEntity<Exercise> createExercise(@Valid @RequestBody Exercise exercise) throws URISyntaxException {
        log.debug("REST request to save Exercise : {}", exercise);
        if (exercise.getId() != null) {
            throw new BadRequestAlertException("A new exercise cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Exercise result = exerciseRepository.save(exercise);
        return ResponseEntity
            .created(new URI("/api/exercises/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /exercises/:id} : Updates an existing exercise.
     *
     * @param id the id of the exercise to save.
     * @param exercise the exercise to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated exercise,
     * or with status {@code 400 (Bad Request)} if the exercise is not valid,
     * or with status {@code 500 (Internal Server Error)} if the exercise couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/exercises/{id}")
    public ResponseEntity<Exercise> updateExercise(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Exercise exercise
    ) throws URISyntaxException {
        log.debug("REST request to update Exercise : {}, {}", id, exercise);
        if (exercise.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, exercise.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!exerciseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Exercise result = exerciseRepository.save(exercise);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, exercise.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /exercises/:id} : Partial updates given fields of an existing exercise, field will ignore if it is null
     *
     * @param id the id of the exercise to save.
     * @param exercise the exercise to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated exercise,
     * or with status {@code 400 (Bad Request)} if the exercise is not valid,
     * or with status {@code 404 (Not Found)} if the exercise is not found,
     * or with status {@code 500 (Internal Server Error)} if the exercise couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/exercises/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Exercise> partialUpdateExercise(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Exercise exercise
    ) throws URISyntaxException {
        log.debug("REST request to partial update Exercise partially : {}, {}", id, exercise);
        if (exercise.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, exercise.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!exerciseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Exercise> result = exerciseRepository
            .findById(exercise.getId())
            .map(existingExercise -> {
                if (exercise.getName() != null) {
                    existingExercise.setName(exercise.getName());
                }

                return existingExercise;
            })
            .map(exerciseRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, exercise.getId().toString())
        );
    }

    /**
     * {@code GET  /exercises} : get all the exercises.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of exercises in body.
     */
    @GetMapping("/exercises")
    public List<Exercise> getAllExercises(@RequestParam(required = false) String filter) {
        if ("strengthworkout-is-null".equals(filter)) {
            log.debug("REST request to get all Exercises where strengthWorkout is null");
            return StreamSupport
                .stream(exerciseRepository.findAll().spliterator(), false)
                .filter(exercise -> exercise.getStrengthWorkout() == null)
                .collect(Collectors.toList());
        }
        log.debug("REST request to get all Exercises");
        return exerciseRepository.findAll();
    }

    /**
     * {@code GET  /exercises/:id} : get the "id" exercise.
     *
     * @param id the id of the exercise to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the exercise, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/exercises/{id}")
    public ResponseEntity<Exercise> getExercise(@PathVariable Long id) {
        log.debug("REST request to get Exercise : {}", id);
        Optional<Exercise> exercise = exerciseRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(exercise);
    }

    /**
     * {@code DELETE  /exercises/:id} : delete the "id" exercise.
     *
     * @param id the id of the exercise to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/exercises/{id}")
    public ResponseEntity<Void> deleteExercise(@PathVariable Long id) {
        log.debug("REST request to delete Exercise : {}", id);
        exerciseRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
