package com.sneddsy.musclemachine.web.rest;

import com.sneddsy.musclemachine.domain.StrengthWorkout;
import com.sneddsy.musclemachine.repository.StrengthWorkoutRepository;
import com.sneddsy.musclemachine.service.StrengthWorkoutService;
import com.sneddsy.musclemachine.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.sneddsy.musclemachine.domain.StrengthWorkout}.
 */
@RestController
@RequestMapping("/api")
public class StrengthWorkoutResource {

    private final Logger log = LoggerFactory.getLogger(StrengthWorkoutResource.class);

    private static final String ENTITY_NAME = "strengthWorkout";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StrengthWorkoutService strengthWorkoutService;

    private final StrengthWorkoutRepository strengthWorkoutRepository;

    public StrengthWorkoutResource(StrengthWorkoutService strengthWorkoutService, StrengthWorkoutRepository strengthWorkoutRepository) {
        this.strengthWorkoutService = strengthWorkoutService;
        this.strengthWorkoutRepository = strengthWorkoutRepository;
    }

    /**
     * {@code POST  /strength-workouts} : Create a new strengthWorkout.
     *
     * @param strengthWorkout the strengthWorkout to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new strengthWorkout, or with status {@code 400 (Bad Request)} if the strengthWorkout has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/strength-workouts")
    public ResponseEntity<StrengthWorkout> createStrengthWorkout(@Valid @RequestBody StrengthWorkout strengthWorkout)
        throws URISyntaxException {
        log.debug("REST request to save StrengthWorkout : {}", strengthWorkout);
        if (strengthWorkout.getId() != null) {
            throw new BadRequestAlertException("A new strengthWorkout cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StrengthWorkout result = strengthWorkoutService.save(strengthWorkout);
        return ResponseEntity
            .created(new URI("/api/strength-workouts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /strength-workouts/:id} : Updates an existing strengthWorkout.
     *
     * @param id the id of the strengthWorkout to save.
     * @param strengthWorkout the strengthWorkout to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated strengthWorkout,
     * or with status {@code 400 (Bad Request)} if the strengthWorkout is not valid,
     * or with status {@code 500 (Internal Server Error)} if the strengthWorkout couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/strength-workouts/{id}")
    public ResponseEntity<StrengthWorkout> updateStrengthWorkout(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody StrengthWorkout strengthWorkout
    ) throws URISyntaxException {
        log.debug("REST request to update StrengthWorkout : {}, {}", id, strengthWorkout);
        if (strengthWorkout.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, strengthWorkout.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!strengthWorkoutRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        StrengthWorkout result = strengthWorkoutService.update(strengthWorkout);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, strengthWorkout.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /strength-workouts/:id} : Partial updates given fields of an existing strengthWorkout, field will ignore if it is null
     *
     * @param id the id of the strengthWorkout to save.
     * @param strengthWorkout the strengthWorkout to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated strengthWorkout,
     * or with status {@code 400 (Bad Request)} if the strengthWorkout is not valid,
     * or with status {@code 404 (Not Found)} if the strengthWorkout is not found,
     * or with status {@code 500 (Internal Server Error)} if the strengthWorkout couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/strength-workouts/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<StrengthWorkout> partialUpdateStrengthWorkout(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody StrengthWorkout strengthWorkout
    ) throws URISyntaxException {
        log.debug("REST request to partial update StrengthWorkout partially : {}, {}", id, strengthWorkout);
        if (strengthWorkout.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, strengthWorkout.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!strengthWorkoutRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<StrengthWorkout> result = strengthWorkoutService.partialUpdate(strengthWorkout);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, strengthWorkout.getId().toString())
        );
    }

    /**
     * {@code GET  /strength-workouts} : get all the strengthWorkouts.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of strengthWorkouts in body.
     */
    @GetMapping("/strength-workouts")
    public List<StrengthWorkout> getAllStrengthWorkouts(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all StrengthWorkouts");
        return strengthWorkoutService.findAll();
    }

    /**
     * {@code GET  /strength-workouts/:id} : get the "id" strengthWorkout.
     *
     * @param id the id of the strengthWorkout to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the strengthWorkout, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/strength-workouts/{id}")
    public ResponseEntity<StrengthWorkout> getStrengthWorkout(@PathVariable Long id) {
        log.debug("REST request to get StrengthWorkout : {}", id);
        Optional<StrengthWorkout> strengthWorkout = strengthWorkoutService.findOne(id);
        return ResponseUtil.wrapOrNotFound(strengthWorkout);
    }

    /**
     * {@code DELETE  /strength-workouts/:id} : delete the "id" strengthWorkout.
     *
     * @param id the id of the strengthWorkout to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/strength-workouts/{id}")
    public ResponseEntity<Void> deleteStrengthWorkout(@PathVariable Long id) {
        log.debug("REST request to delete StrengthWorkout : {}", id);
        strengthWorkoutService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
