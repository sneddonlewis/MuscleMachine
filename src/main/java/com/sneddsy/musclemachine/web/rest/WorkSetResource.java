package com.sneddsy.musclemachine.web.rest;

import com.sneddsy.musclemachine.domain.WorkSet;
import com.sneddsy.musclemachine.repository.WorkSetRepository;
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
 * REST controller for managing {@link com.sneddsy.musclemachine.domain.WorkSet}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class WorkSetResource {

    private final Logger log = LoggerFactory.getLogger(WorkSetResource.class);

    private static final String ENTITY_NAME = "workSet";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WorkSetRepository workSetRepository;

    public WorkSetResource(WorkSetRepository workSetRepository) {
        this.workSetRepository = workSetRepository;
    }

    /**
     * {@code POST  /work-sets} : Create a new workSet.
     *
     * @param workSet the workSet to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new workSet, or with status {@code 400 (Bad Request)} if the workSet has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/work-sets")
    public ResponseEntity<WorkSet> createWorkSet(@Valid @RequestBody WorkSet workSet) throws URISyntaxException {
        log.debug("REST request to save WorkSet : {}", workSet);
        if (workSet.getId() != null) {
            throw new BadRequestAlertException("A new workSet cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WorkSet result = workSetRepository.save(workSet);
        return ResponseEntity
            .created(new URI("/api/work-sets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /work-sets/:id} : Updates an existing workSet.
     *
     * @param id the id of the workSet to save.
     * @param workSet the workSet to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workSet,
     * or with status {@code 400 (Bad Request)} if the workSet is not valid,
     * or with status {@code 500 (Internal Server Error)} if the workSet couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/work-sets/{id}")
    public ResponseEntity<WorkSet> updateWorkSet(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody WorkSet workSet
    ) throws URISyntaxException {
        log.debug("REST request to update WorkSet : {}, {}", id, workSet);
        if (workSet.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workSet.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workSetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        WorkSet result = workSetRepository.save(workSet);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, workSet.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /work-sets/:id} : Partial updates given fields of an existing workSet, field will ignore if it is null
     *
     * @param id the id of the workSet to save.
     * @param workSet the workSet to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workSet,
     * or with status {@code 400 (Bad Request)} if the workSet is not valid,
     * or with status {@code 404 (Not Found)} if the workSet is not found,
     * or with status {@code 500 (Internal Server Error)} if the workSet couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/work-sets/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<WorkSet> partialUpdateWorkSet(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody WorkSet workSet
    ) throws URISyntaxException {
        log.debug("REST request to partial update WorkSet partially : {}, {}", id, workSet);
        if (workSet.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workSet.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workSetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WorkSet> result = workSetRepository
            .findById(workSet.getId())
            .map(existingWorkSet -> {
                if (workSet.getSetNumber() != null) {
                    existingWorkSet.setSetNumber(workSet.getSetNumber());
                }
                if (workSet.getRepetitions() != null) {
                    existingWorkSet.setRepetitions(workSet.getRepetitions());
                }
                if (workSet.getTimeUnderLoad() != null) {
                    existingWorkSet.setTimeUnderLoad(workSet.getTimeUnderLoad());
                }
                if (workSet.getBandResistance() != null) {
                    existingWorkSet.setBandResistance(workSet.getBandResistance());
                }
                if (workSet.getCableResistance() != null) {
                    existingWorkSet.setCableResistance(workSet.getCableResistance());
                }
                if (workSet.getFreeWeightResistance() != null) {
                    existingWorkSet.setFreeWeightResistance(workSet.getFreeWeightResistance());
                }

                return existingWorkSet;
            })
            .map(workSetRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, workSet.getId().toString())
        );
    }

    /**
     * {@code GET  /work-sets} : get all the workSets.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of workSets in body.
     */
    @GetMapping("/work-sets")
    public List<WorkSet> getAllWorkSets() {
        log.debug("REST request to get all WorkSets");
        return workSetRepository.findAll();
    }

    /**
     * {@code GET  /work-sets/:id} : get the "id" workSet.
     *
     * @param id the id of the workSet to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the workSet, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/work-sets/{id}")
    public ResponseEntity<WorkSet> getWorkSet(@PathVariable Long id) {
        log.debug("REST request to get WorkSet : {}", id);
        Optional<WorkSet> workSet = workSetRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(workSet);
    }

    /**
     * {@code DELETE  /work-sets/:id} : delete the "id" workSet.
     *
     * @param id the id of the workSet to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/work-sets/{id}")
    public ResponseEntity<Void> deleteWorkSet(@PathVariable Long id) {
        log.debug("REST request to delete WorkSet : {}", id);
        workSetRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
