package com.sneddsy.musclemachine.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sneddsy.musclemachine.IntegrationTest;
import com.sneddsy.musclemachine.domain.WorkSet;
import com.sneddsy.musclemachine.repository.WorkSetRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link WorkSetResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WorkSetResourceIT {

    private static final Integer DEFAULT_SET_NUMBER = 1;
    private static final Integer UPDATED_SET_NUMBER = 2;

    private static final Integer DEFAULT_REPETITIONS = 1;
    private static final Integer UPDATED_REPETITIONS = 2;

    private static final Integer DEFAULT_TIME_UNDER_LOAD = 1;
    private static final Integer UPDATED_TIME_UNDER_LOAD = 2;

    private static final Float DEFAULT_BAND_RESISTANCE = 1F;
    private static final Float UPDATED_BAND_RESISTANCE = 2F;

    private static final Float DEFAULT_CABLE_RESISTANCE = 1F;
    private static final Float UPDATED_CABLE_RESISTANCE = 2F;

    private static final Float DEFAULT_FREE_WEIGHT_RESISTANCE = 1F;
    private static final Float UPDATED_FREE_WEIGHT_RESISTANCE = 2F;

    private static final String ENTITY_API_URL = "/api/work-sets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WorkSetRepository workSetRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWorkSetMockMvc;

    private WorkSet workSet;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkSet createEntity(EntityManager em) {
        WorkSet workSet = new WorkSet()
            .setNumber(DEFAULT_SET_NUMBER)
            .repetitions(DEFAULT_REPETITIONS)
            .timeUnderLoad(DEFAULT_TIME_UNDER_LOAD)
            .bandResistance(DEFAULT_BAND_RESISTANCE)
            .cableResistance(DEFAULT_CABLE_RESISTANCE)
            .freeWeightResistance(DEFAULT_FREE_WEIGHT_RESISTANCE);
        return workSet;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkSet createUpdatedEntity(EntityManager em) {
        WorkSet workSet = new WorkSet()
            .setNumber(UPDATED_SET_NUMBER)
            .repetitions(UPDATED_REPETITIONS)
            .timeUnderLoad(UPDATED_TIME_UNDER_LOAD)
            .bandResistance(UPDATED_BAND_RESISTANCE)
            .cableResistance(UPDATED_CABLE_RESISTANCE)
            .freeWeightResistance(UPDATED_FREE_WEIGHT_RESISTANCE);
        return workSet;
    }

    @BeforeEach
    public void initTest() {
        workSet = createEntity(em);
    }

    @Test
    @Transactional
    void createWorkSet() throws Exception {
        int databaseSizeBeforeCreate = workSetRepository.findAll().size();
        // Create the WorkSet
        restWorkSetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(workSet)))
            .andExpect(status().isCreated());

        // Validate the WorkSet in the database
        List<WorkSet> workSetList = workSetRepository.findAll();
        assertThat(workSetList).hasSize(databaseSizeBeforeCreate + 1);
        WorkSet testWorkSet = workSetList.get(workSetList.size() - 1);
        assertThat(testWorkSet.getSetNumber()).isEqualTo(DEFAULT_SET_NUMBER);
        assertThat(testWorkSet.getRepetitions()).isEqualTo(DEFAULT_REPETITIONS);
        assertThat(testWorkSet.getTimeUnderLoad()).isEqualTo(DEFAULT_TIME_UNDER_LOAD);
        assertThat(testWorkSet.getBandResistance()).isEqualTo(DEFAULT_BAND_RESISTANCE);
        assertThat(testWorkSet.getCableResistance()).isEqualTo(DEFAULT_CABLE_RESISTANCE);
        assertThat(testWorkSet.getFreeWeightResistance()).isEqualTo(DEFAULT_FREE_WEIGHT_RESISTANCE);
    }

    @Test
    @Transactional
    void createWorkSetWithExistingId() throws Exception {
        // Create the WorkSet with an existing ID
        workSet.setId(1L);

        int databaseSizeBeforeCreate = workSetRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWorkSetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(workSet)))
            .andExpect(status().isBadRequest());

        // Validate the WorkSet in the database
        List<WorkSet> workSetList = workSetRepository.findAll();
        assertThat(workSetList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSetNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = workSetRepository.findAll().size();
        // set the field null
        workSet.setSetNumber(null);

        // Create the WorkSet, which fails.

        restWorkSetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(workSet)))
            .andExpect(status().isBadRequest());

        List<WorkSet> workSetList = workSetRepository.findAll();
        assertThat(workSetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllWorkSets() throws Exception {
        // Initialize the database
        workSetRepository.saveAndFlush(workSet);

        // Get all the workSetList
        restWorkSetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workSet.getId().intValue())))
            .andExpect(jsonPath("$.[*].setNumber").value(hasItem(DEFAULT_SET_NUMBER)))
            .andExpect(jsonPath("$.[*].repetitions").value(hasItem(DEFAULT_REPETITIONS)))
            .andExpect(jsonPath("$.[*].timeUnderLoad").value(hasItem(DEFAULT_TIME_UNDER_LOAD)))
            .andExpect(jsonPath("$.[*].bandResistance").value(hasItem(DEFAULT_BAND_RESISTANCE.doubleValue())))
            .andExpect(jsonPath("$.[*].cableResistance").value(hasItem(DEFAULT_CABLE_RESISTANCE.doubleValue())))
            .andExpect(jsonPath("$.[*].freeWeightResistance").value(hasItem(DEFAULT_FREE_WEIGHT_RESISTANCE.doubleValue())));
    }

    @Test
    @Transactional
    void getWorkSet() throws Exception {
        // Initialize the database
        workSetRepository.saveAndFlush(workSet);

        // Get the workSet
        restWorkSetMockMvc
            .perform(get(ENTITY_API_URL_ID, workSet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(workSet.getId().intValue()))
            .andExpect(jsonPath("$.setNumber").value(DEFAULT_SET_NUMBER))
            .andExpect(jsonPath("$.repetitions").value(DEFAULT_REPETITIONS))
            .andExpect(jsonPath("$.timeUnderLoad").value(DEFAULT_TIME_UNDER_LOAD))
            .andExpect(jsonPath("$.bandResistance").value(DEFAULT_BAND_RESISTANCE.doubleValue()))
            .andExpect(jsonPath("$.cableResistance").value(DEFAULT_CABLE_RESISTANCE.doubleValue()))
            .andExpect(jsonPath("$.freeWeightResistance").value(DEFAULT_FREE_WEIGHT_RESISTANCE.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingWorkSet() throws Exception {
        // Get the workSet
        restWorkSetMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingWorkSet() throws Exception {
        // Initialize the database
        workSetRepository.saveAndFlush(workSet);

        int databaseSizeBeforeUpdate = workSetRepository.findAll().size();

        // Update the workSet
        WorkSet updatedWorkSet = workSetRepository.findById(workSet.getId()).get();
        // Disconnect from session so that the updates on updatedWorkSet are not directly saved in db
        em.detach(updatedWorkSet);
        updatedWorkSet
            .setNumber(UPDATED_SET_NUMBER)
            .repetitions(UPDATED_REPETITIONS)
            .timeUnderLoad(UPDATED_TIME_UNDER_LOAD)
            .bandResistance(UPDATED_BAND_RESISTANCE)
            .cableResistance(UPDATED_CABLE_RESISTANCE)
            .freeWeightResistance(UPDATED_FREE_WEIGHT_RESISTANCE);

        restWorkSetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedWorkSet.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedWorkSet))
            )
            .andExpect(status().isOk());

        // Validate the WorkSet in the database
        List<WorkSet> workSetList = workSetRepository.findAll();
        assertThat(workSetList).hasSize(databaseSizeBeforeUpdate);
        WorkSet testWorkSet = workSetList.get(workSetList.size() - 1);
        assertThat(testWorkSet.getSetNumber()).isEqualTo(UPDATED_SET_NUMBER);
        assertThat(testWorkSet.getRepetitions()).isEqualTo(UPDATED_REPETITIONS);
        assertThat(testWorkSet.getTimeUnderLoad()).isEqualTo(UPDATED_TIME_UNDER_LOAD);
        assertThat(testWorkSet.getBandResistance()).isEqualTo(UPDATED_BAND_RESISTANCE);
        assertThat(testWorkSet.getCableResistance()).isEqualTo(UPDATED_CABLE_RESISTANCE);
        assertThat(testWorkSet.getFreeWeightResistance()).isEqualTo(UPDATED_FREE_WEIGHT_RESISTANCE);
    }

    @Test
    @Transactional
    void putNonExistingWorkSet() throws Exception {
        int databaseSizeBeforeUpdate = workSetRepository.findAll().size();
        workSet.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkSetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, workSet.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workSet))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkSet in the database
        List<WorkSet> workSetList = workSetRepository.findAll();
        assertThat(workSetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWorkSet() throws Exception {
        int databaseSizeBeforeUpdate = workSetRepository.findAll().size();
        workSet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkSetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workSet))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkSet in the database
        List<WorkSet> workSetList = workSetRepository.findAll();
        assertThat(workSetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWorkSet() throws Exception {
        int databaseSizeBeforeUpdate = workSetRepository.findAll().size();
        workSet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkSetMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(workSet)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorkSet in the database
        List<WorkSet> workSetList = workSetRepository.findAll();
        assertThat(workSetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWorkSetWithPatch() throws Exception {
        // Initialize the database
        workSetRepository.saveAndFlush(workSet);

        int databaseSizeBeforeUpdate = workSetRepository.findAll().size();

        // Update the workSet using partial update
        WorkSet partialUpdatedWorkSet = new WorkSet();
        partialUpdatedWorkSet.setId(workSet.getId());

        partialUpdatedWorkSet
            .setNumber(UPDATED_SET_NUMBER)
            .timeUnderLoad(UPDATED_TIME_UNDER_LOAD)
            .bandResistance(UPDATED_BAND_RESISTANCE)
            .cableResistance(UPDATED_CABLE_RESISTANCE)
            .freeWeightResistance(UPDATED_FREE_WEIGHT_RESISTANCE);

        restWorkSetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkSet.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWorkSet))
            )
            .andExpect(status().isOk());

        // Validate the WorkSet in the database
        List<WorkSet> workSetList = workSetRepository.findAll();
        assertThat(workSetList).hasSize(databaseSizeBeforeUpdate);
        WorkSet testWorkSet = workSetList.get(workSetList.size() - 1);
        assertThat(testWorkSet.getSetNumber()).isEqualTo(UPDATED_SET_NUMBER);
        assertThat(testWorkSet.getRepetitions()).isEqualTo(DEFAULT_REPETITIONS);
        assertThat(testWorkSet.getTimeUnderLoad()).isEqualTo(UPDATED_TIME_UNDER_LOAD);
        assertThat(testWorkSet.getBandResistance()).isEqualTo(UPDATED_BAND_RESISTANCE);
        assertThat(testWorkSet.getCableResistance()).isEqualTo(UPDATED_CABLE_RESISTANCE);
        assertThat(testWorkSet.getFreeWeightResistance()).isEqualTo(UPDATED_FREE_WEIGHT_RESISTANCE);
    }

    @Test
    @Transactional
    void fullUpdateWorkSetWithPatch() throws Exception {
        // Initialize the database
        workSetRepository.saveAndFlush(workSet);

        int databaseSizeBeforeUpdate = workSetRepository.findAll().size();

        // Update the workSet using partial update
        WorkSet partialUpdatedWorkSet = new WorkSet();
        partialUpdatedWorkSet.setId(workSet.getId());

        partialUpdatedWorkSet
            .setNumber(UPDATED_SET_NUMBER)
            .repetitions(UPDATED_REPETITIONS)
            .timeUnderLoad(UPDATED_TIME_UNDER_LOAD)
            .bandResistance(UPDATED_BAND_RESISTANCE)
            .cableResistance(UPDATED_CABLE_RESISTANCE)
            .freeWeightResistance(UPDATED_FREE_WEIGHT_RESISTANCE);

        restWorkSetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkSet.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWorkSet))
            )
            .andExpect(status().isOk());

        // Validate the WorkSet in the database
        List<WorkSet> workSetList = workSetRepository.findAll();
        assertThat(workSetList).hasSize(databaseSizeBeforeUpdate);
        WorkSet testWorkSet = workSetList.get(workSetList.size() - 1);
        assertThat(testWorkSet.getSetNumber()).isEqualTo(UPDATED_SET_NUMBER);
        assertThat(testWorkSet.getRepetitions()).isEqualTo(UPDATED_REPETITIONS);
        assertThat(testWorkSet.getTimeUnderLoad()).isEqualTo(UPDATED_TIME_UNDER_LOAD);
        assertThat(testWorkSet.getBandResistance()).isEqualTo(UPDATED_BAND_RESISTANCE);
        assertThat(testWorkSet.getCableResistance()).isEqualTo(UPDATED_CABLE_RESISTANCE);
        assertThat(testWorkSet.getFreeWeightResistance()).isEqualTo(UPDATED_FREE_WEIGHT_RESISTANCE);
    }

    @Test
    @Transactional
    void patchNonExistingWorkSet() throws Exception {
        int databaseSizeBeforeUpdate = workSetRepository.findAll().size();
        workSet.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkSetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, workSet.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workSet))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkSet in the database
        List<WorkSet> workSetList = workSetRepository.findAll();
        assertThat(workSetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWorkSet() throws Exception {
        int databaseSizeBeforeUpdate = workSetRepository.findAll().size();
        workSet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkSetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workSet))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkSet in the database
        List<WorkSet> workSetList = workSetRepository.findAll();
        assertThat(workSetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWorkSet() throws Exception {
        int databaseSizeBeforeUpdate = workSetRepository.findAll().size();
        workSet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkSetMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(workSet)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorkSet in the database
        List<WorkSet> workSetList = workSetRepository.findAll();
        assertThat(workSetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWorkSet() throws Exception {
        // Initialize the database
        workSetRepository.saveAndFlush(workSet);

        int databaseSizeBeforeDelete = workSetRepository.findAll().size();

        // Delete the workSet
        restWorkSetMockMvc
            .perform(delete(ENTITY_API_URL_ID, workSet.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WorkSet> workSetList = workSetRepository.findAll();
        assertThat(workSetList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
