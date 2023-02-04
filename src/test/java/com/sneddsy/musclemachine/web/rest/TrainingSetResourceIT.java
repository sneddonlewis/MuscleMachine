package com.sneddsy.musclemachine.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sneddsy.musclemachine.IntegrationTest;
import com.sneddsy.musclemachine.domain.TrainingSet;
import com.sneddsy.musclemachine.repository.TrainingSetRepository;
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
 * Integration tests for the {@link TrainingSetResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TrainingSetResourceIT {

    private static final Integer DEFAULT_SET_NUMBER = 1;
    private static final Integer UPDATED_SET_NUMBER = 2;

    private static final Integer DEFAULT_REPETITIONS = 1;
    private static final Integer UPDATED_REPETITIONS = 2;

    private static final Integer DEFAULT_TIME_UNDER_LOAD = 1;
    private static final Integer UPDATED_TIME_UNDER_LOAD = 2;

    private static final String ENTITY_API_URL = "/api/training-sets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TrainingSetRepository trainingSetRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTrainingSetMockMvc;

    private TrainingSet trainingSet;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TrainingSet createEntity(EntityManager em) {
        TrainingSet trainingSet = new TrainingSet()
            .setNumber(DEFAULT_SET_NUMBER)
            .repetitions(DEFAULT_REPETITIONS)
            .timeUnderLoad(DEFAULT_TIME_UNDER_LOAD);
        return trainingSet;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TrainingSet createUpdatedEntity(EntityManager em) {
        TrainingSet trainingSet = new TrainingSet()
            .setNumber(UPDATED_SET_NUMBER)
            .repetitions(UPDATED_REPETITIONS)
            .timeUnderLoad(UPDATED_TIME_UNDER_LOAD);
        return trainingSet;
    }

    @BeforeEach
    public void initTest() {
        trainingSet = createEntity(em);
    }

    @Test
    @Transactional
    void createTrainingSet() throws Exception {
        int databaseSizeBeforeCreate = trainingSetRepository.findAll().size();
        // Create the TrainingSet
        restTrainingSetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trainingSet)))
            .andExpect(status().isCreated());

        // Validate the TrainingSet in the database
        List<TrainingSet> trainingSetList = trainingSetRepository.findAll();
        assertThat(trainingSetList).hasSize(databaseSizeBeforeCreate + 1);
        TrainingSet testTrainingSet = trainingSetList.get(trainingSetList.size() - 1);
        assertThat(testTrainingSet.getSetNumber()).isEqualTo(DEFAULT_SET_NUMBER);
        assertThat(testTrainingSet.getRepetitions()).isEqualTo(DEFAULT_REPETITIONS);
        assertThat(testTrainingSet.getTimeUnderLoad()).isEqualTo(DEFAULT_TIME_UNDER_LOAD);
    }

    @Test
    @Transactional
    void createTrainingSetWithExistingId() throws Exception {
        // Create the TrainingSet with an existing ID
        trainingSet.setId(1L);

        int databaseSizeBeforeCreate = trainingSetRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTrainingSetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trainingSet)))
            .andExpect(status().isBadRequest());

        // Validate the TrainingSet in the database
        List<TrainingSet> trainingSetList = trainingSetRepository.findAll();
        assertThat(trainingSetList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSetNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = trainingSetRepository.findAll().size();
        // set the field null
        trainingSet.setSetNumber(null);

        // Create the TrainingSet, which fails.

        restTrainingSetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trainingSet)))
            .andExpect(status().isBadRequest());

        List<TrainingSet> trainingSetList = trainingSetRepository.findAll();
        assertThat(trainingSetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTrainingSets() throws Exception {
        // Initialize the database
        trainingSetRepository.saveAndFlush(trainingSet);

        // Get all the trainingSetList
        restTrainingSetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(trainingSet.getId().intValue())))
            .andExpect(jsonPath("$.[*].setNumber").value(hasItem(DEFAULT_SET_NUMBER)))
            .andExpect(jsonPath("$.[*].repetitions").value(hasItem(DEFAULT_REPETITIONS)))
            .andExpect(jsonPath("$.[*].timeUnderLoad").value(hasItem(DEFAULT_TIME_UNDER_LOAD)));
    }

    @Test
    @Transactional
    void getTrainingSet() throws Exception {
        // Initialize the database
        trainingSetRepository.saveAndFlush(trainingSet);

        // Get the trainingSet
        restTrainingSetMockMvc
            .perform(get(ENTITY_API_URL_ID, trainingSet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(trainingSet.getId().intValue()))
            .andExpect(jsonPath("$.setNumber").value(DEFAULT_SET_NUMBER))
            .andExpect(jsonPath("$.repetitions").value(DEFAULT_REPETITIONS))
            .andExpect(jsonPath("$.timeUnderLoad").value(DEFAULT_TIME_UNDER_LOAD));
    }

    @Test
    @Transactional
    void getNonExistingTrainingSet() throws Exception {
        // Get the trainingSet
        restTrainingSetMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTrainingSet() throws Exception {
        // Initialize the database
        trainingSetRepository.saveAndFlush(trainingSet);

        int databaseSizeBeforeUpdate = trainingSetRepository.findAll().size();

        // Update the trainingSet
        TrainingSet updatedTrainingSet = trainingSetRepository.findById(trainingSet.getId()).get();
        // Disconnect from session so that the updates on updatedTrainingSet are not directly saved in db
        em.detach(updatedTrainingSet);
        updatedTrainingSet.setNumber(UPDATED_SET_NUMBER).repetitions(UPDATED_REPETITIONS).timeUnderLoad(UPDATED_TIME_UNDER_LOAD);

        restTrainingSetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTrainingSet.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTrainingSet))
            )
            .andExpect(status().isOk());

        // Validate the TrainingSet in the database
        List<TrainingSet> trainingSetList = trainingSetRepository.findAll();
        assertThat(trainingSetList).hasSize(databaseSizeBeforeUpdate);
        TrainingSet testTrainingSet = trainingSetList.get(trainingSetList.size() - 1);
        assertThat(testTrainingSet.getSetNumber()).isEqualTo(UPDATED_SET_NUMBER);
        assertThat(testTrainingSet.getRepetitions()).isEqualTo(UPDATED_REPETITIONS);
        assertThat(testTrainingSet.getTimeUnderLoad()).isEqualTo(UPDATED_TIME_UNDER_LOAD);
    }

    @Test
    @Transactional
    void putNonExistingTrainingSet() throws Exception {
        int databaseSizeBeforeUpdate = trainingSetRepository.findAll().size();
        trainingSet.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrainingSetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, trainingSet.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(trainingSet))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrainingSet in the database
        List<TrainingSet> trainingSetList = trainingSetRepository.findAll();
        assertThat(trainingSetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTrainingSet() throws Exception {
        int databaseSizeBeforeUpdate = trainingSetRepository.findAll().size();
        trainingSet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrainingSetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(trainingSet))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrainingSet in the database
        List<TrainingSet> trainingSetList = trainingSetRepository.findAll();
        assertThat(trainingSetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTrainingSet() throws Exception {
        int databaseSizeBeforeUpdate = trainingSetRepository.findAll().size();
        trainingSet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrainingSetMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trainingSet)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TrainingSet in the database
        List<TrainingSet> trainingSetList = trainingSetRepository.findAll();
        assertThat(trainingSetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTrainingSetWithPatch() throws Exception {
        // Initialize the database
        trainingSetRepository.saveAndFlush(trainingSet);

        int databaseSizeBeforeUpdate = trainingSetRepository.findAll().size();

        // Update the trainingSet using partial update
        TrainingSet partialUpdatedTrainingSet = new TrainingSet();
        partialUpdatedTrainingSet.setId(trainingSet.getId());

        partialUpdatedTrainingSet.repetitions(UPDATED_REPETITIONS);

        restTrainingSetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTrainingSet.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTrainingSet))
            )
            .andExpect(status().isOk());

        // Validate the TrainingSet in the database
        List<TrainingSet> trainingSetList = trainingSetRepository.findAll();
        assertThat(trainingSetList).hasSize(databaseSizeBeforeUpdate);
        TrainingSet testTrainingSet = trainingSetList.get(trainingSetList.size() - 1);
        assertThat(testTrainingSet.getSetNumber()).isEqualTo(DEFAULT_SET_NUMBER);
        assertThat(testTrainingSet.getRepetitions()).isEqualTo(UPDATED_REPETITIONS);
        assertThat(testTrainingSet.getTimeUnderLoad()).isEqualTo(DEFAULT_TIME_UNDER_LOAD);
    }

    @Test
    @Transactional
    void fullUpdateTrainingSetWithPatch() throws Exception {
        // Initialize the database
        trainingSetRepository.saveAndFlush(trainingSet);

        int databaseSizeBeforeUpdate = trainingSetRepository.findAll().size();

        // Update the trainingSet using partial update
        TrainingSet partialUpdatedTrainingSet = new TrainingSet();
        partialUpdatedTrainingSet.setId(trainingSet.getId());

        partialUpdatedTrainingSet.setNumber(UPDATED_SET_NUMBER).repetitions(UPDATED_REPETITIONS).timeUnderLoad(UPDATED_TIME_UNDER_LOAD);

        restTrainingSetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTrainingSet.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTrainingSet))
            )
            .andExpect(status().isOk());

        // Validate the TrainingSet in the database
        List<TrainingSet> trainingSetList = trainingSetRepository.findAll();
        assertThat(trainingSetList).hasSize(databaseSizeBeforeUpdate);
        TrainingSet testTrainingSet = trainingSetList.get(trainingSetList.size() - 1);
        assertThat(testTrainingSet.getSetNumber()).isEqualTo(UPDATED_SET_NUMBER);
        assertThat(testTrainingSet.getRepetitions()).isEqualTo(UPDATED_REPETITIONS);
        assertThat(testTrainingSet.getTimeUnderLoad()).isEqualTo(UPDATED_TIME_UNDER_LOAD);
    }

    @Test
    @Transactional
    void patchNonExistingTrainingSet() throws Exception {
        int databaseSizeBeforeUpdate = trainingSetRepository.findAll().size();
        trainingSet.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrainingSetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, trainingSet.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(trainingSet))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrainingSet in the database
        List<TrainingSet> trainingSetList = trainingSetRepository.findAll();
        assertThat(trainingSetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTrainingSet() throws Exception {
        int databaseSizeBeforeUpdate = trainingSetRepository.findAll().size();
        trainingSet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrainingSetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(trainingSet))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrainingSet in the database
        List<TrainingSet> trainingSetList = trainingSetRepository.findAll();
        assertThat(trainingSetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTrainingSet() throws Exception {
        int databaseSizeBeforeUpdate = trainingSetRepository.findAll().size();
        trainingSet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrainingSetMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(trainingSet))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TrainingSet in the database
        List<TrainingSet> trainingSetList = trainingSetRepository.findAll();
        assertThat(trainingSetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTrainingSet() throws Exception {
        // Initialize the database
        trainingSetRepository.saveAndFlush(trainingSet);

        int databaseSizeBeforeDelete = trainingSetRepository.findAll().size();

        // Delete the trainingSet
        restTrainingSetMockMvc
            .perform(delete(ENTITY_API_URL_ID, trainingSet.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TrainingSet> trainingSetList = trainingSetRepository.findAll();
        assertThat(trainingSetList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
