package com.sneddsy.musclemachine.web.rest;

import com.sneddsy.musclemachine.domain.TrainingSet;
import com.sneddsy.musclemachine.repository.TrainingSetRepository;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.sneddsy.musclemachine.domain.TrainingSet}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class TrainingSetResource {

    private final Logger log = LoggerFactory.getLogger(TrainingSetResource.class);

    private static final String ENTITY_NAME = "trainingSet";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TrainingSetRepository trainingSetRepository;

    public TrainingSetResource(TrainingSetRepository trainingSetRepository) {
        this.trainingSetRepository = trainingSetRepository;
    }

    /**
     * {@code POST  /training-sets} : Create a new trainingSet.
     *
     * @param trainingSet the trainingSet to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new trainingSet, or with status {@code 400 (Bad Request)} if the trainingSet has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/training-sets")
    public ResponseEntity<TrainingSet> createTrainingSet(@Valid @RequestBody TrainingSet trainingSet) throws URISyntaxException {
        log.debug("REST request to save TrainingSet : {}", trainingSet);
        if (trainingSet.getId() != null) {
            throw new BadRequestAlertException("A new trainingSet cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TrainingSet result = trainingSetRepository.save(trainingSet);
        return ResponseEntity
            .created(new URI("/api/training-sets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /training-sets/:id} : Updates an existing trainingSet.
     *
     * @param id the id of the trainingSet to save.
     * @param trainingSet the trainingSet to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated trainingSet,
     * or with status {@code 400 (Bad Request)} if the trainingSet is not valid,
     * or with status {@code 500 (Internal Server Error)} if the trainingSet couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/training-sets/{id}")
    public ResponseEntity<TrainingSet> updateTrainingSet(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TrainingSet trainingSet
    ) throws URISyntaxException {
        log.debug("REST request to update TrainingSet : {}, {}", id, trainingSet);
        if (trainingSet.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, trainingSet.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!trainingSetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TrainingSet result = trainingSetRepository.save(trainingSet);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, trainingSet.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /training-sets/:id} : Partial updates given fields of an existing trainingSet, field will ignore if it is null
     *
     * @param id the id of the trainingSet to save.
     * @param trainingSet the trainingSet to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated trainingSet,
     * or with status {@code 400 (Bad Request)} if the trainingSet is not valid,
     * or with status {@code 404 (Not Found)} if the trainingSet is not found,
     * or with status {@code 500 (Internal Server Error)} if the trainingSet couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/training-sets/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TrainingSet> partialUpdateTrainingSet(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TrainingSet trainingSet
    ) throws URISyntaxException {
        log.debug("REST request to partial update TrainingSet partially : {}, {}", id, trainingSet);
        if (trainingSet.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, trainingSet.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!trainingSetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TrainingSet> result = trainingSetRepository
            .findById(trainingSet.getId())
            .map(existingTrainingSet -> {
                if (trainingSet.getSetNumber() != null) {
                    existingTrainingSet.setSetNumber(trainingSet.getSetNumber());
                }
                if (trainingSet.getRepetitions() != null) {
                    existingTrainingSet.setRepetitions(trainingSet.getRepetitions());
                }
                if (trainingSet.getTimeUnderLoad() != null) {
                    existingTrainingSet.setTimeUnderLoad(trainingSet.getTimeUnderLoad());
                }

                return existingTrainingSet;
            })
            .map(trainingSetRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, trainingSet.getId().toString())
        );
    }

    /**
     * {@code GET  /training-sets} : get all the trainingSets.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of trainingSets in body.
     */
    @GetMapping("/training-sets")
    public List<TrainingSet> getAllTrainingSets() {
        log.debug("REST request to get all TrainingSets");
        return trainingSetRepository.findAll();
    }

    /**
     * {@code GET  /training-sets/:id} : get the "id" trainingSet.
     *
     * @param id the id of the trainingSet to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the trainingSet, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/training-sets/{id}")
    public ResponseEntity<TrainingSet> getTrainingSet(@PathVariable Long id) {
        log.debug("REST request to get TrainingSet : {}", id);
        Optional<TrainingSet> trainingSet = trainingSetRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(trainingSet);
    }

    /**
     * {@code DELETE  /training-sets/:id} : delete the "id" trainingSet.
     *
     * @param id the id of the trainingSet to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/training-sets/{id}")
    public ResponseEntity<Void> deleteTrainingSet(@PathVariable Long id) {
        log.debug("REST request to delete TrainingSet : {}", id);
        trainingSetRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
