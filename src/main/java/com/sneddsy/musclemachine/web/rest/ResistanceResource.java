package com.sneddsy.musclemachine.web.rest;

import com.sneddsy.musclemachine.domain.Resistance;
import com.sneddsy.musclemachine.repository.ResistanceRepository;
import com.sneddsy.musclemachine.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.sneddsy.musclemachine.domain.Resistance}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ResistanceResource {

    private final Logger log = LoggerFactory.getLogger(ResistanceResource.class);

    private static final String ENTITY_NAME = "resistance";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ResistanceRepository resistanceRepository;

    public ResistanceResource(ResistanceRepository resistanceRepository) {
        this.resistanceRepository = resistanceRepository;
    }

    /**
     * {@code POST  /resistances} : Create a new resistance.
     *
     * @param resistance the resistance to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new resistance, or with status {@code 400 (Bad Request)} if the resistance has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/resistances")
    public ResponseEntity<Resistance> createResistance(@RequestBody Resistance resistance) throws URISyntaxException {
        log.debug("REST request to save Resistance : {}", resistance);
        if (resistance.getId() != null) {
            throw new BadRequestAlertException("A new resistance cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Resistance result = resistanceRepository.save(resistance);
        return ResponseEntity
            .created(new URI("/api/resistances/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /resistances/:id} : Updates an existing resistance.
     *
     * @param id the id of the resistance to save.
     * @param resistance the resistance to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated resistance,
     * or with status {@code 400 (Bad Request)} if the resistance is not valid,
     * or with status {@code 500 (Internal Server Error)} if the resistance couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/resistances/{id}")
    public ResponseEntity<Resistance> updateResistance(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Resistance resistance
    ) throws URISyntaxException {
        log.debug("REST request to update Resistance : {}, {}", id, resistance);
        if (resistance.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, resistance.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!resistanceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Resistance result = resistanceRepository.save(resistance);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, resistance.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /resistances/:id} : Partial updates given fields of an existing resistance, field will ignore if it is null
     *
     * @param id the id of the resistance to save.
     * @param resistance the resistance to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated resistance,
     * or with status {@code 400 (Bad Request)} if the resistance is not valid,
     * or with status {@code 404 (Not Found)} if the resistance is not found,
     * or with status {@code 500 (Internal Server Error)} if the resistance couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/resistances/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Resistance> partialUpdateResistance(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Resistance resistance
    ) throws URISyntaxException {
        log.debug("REST request to partial update Resistance partially : {}, {}", id, resistance);
        if (resistance.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, resistance.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!resistanceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Resistance> result = resistanceRepository
            .findById(resistance.getId())
            .map(existingResistance -> {
                if (resistance.getBand() != null) {
                    existingResistance.setBand(resistance.getBand());
                }
                if (resistance.getCable() != null) {
                    existingResistance.setCable(resistance.getCable());
                }
                if (resistance.getFreeWeight() != null) {
                    existingResistance.setFreeWeight(resistance.getFreeWeight());
                }

                return existingResistance;
            })
            .map(resistanceRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, resistance.getId().toString())
        );
    }

    /**
     * {@code GET  /resistances} : get all the resistances.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of resistances in body.
     */
    @GetMapping("/resistances")
    public List<Resistance> getAllResistances(@RequestParam(required = false) String filter) {
        if ("trainingset-is-null".equals(filter)) {
            log.debug("REST request to get all Resistances where trainingSet is null");
            return StreamSupport
                .stream(resistanceRepository.findAll().spliterator(), false)
                .filter(resistance -> resistance.getTrainingSet() == null)
                .collect(Collectors.toList());
        }
        log.debug("REST request to get all Resistances");
        return resistanceRepository.findAll();
    }

    /**
     * {@code GET  /resistances/:id} : get the "id" resistance.
     *
     * @param id the id of the resistance to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the resistance, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/resistances/{id}")
    public ResponseEntity<Resistance> getResistance(@PathVariable Long id) {
        log.debug("REST request to get Resistance : {}", id);
        Optional<Resistance> resistance = resistanceRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(resistance);
    }

    /**
     * {@code DELETE  /resistances/:id} : delete the "id" resistance.
     *
     * @param id the id of the resistance to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/resistances/{id}")
    public ResponseEntity<Void> deleteResistance(@PathVariable Long id) {
        log.debug("REST request to delete Resistance : {}", id);
        resistanceRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
