package com.sneddsy.musclemachine.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sneddsy.musclemachine.IntegrationTest;
import com.sneddsy.musclemachine.domain.Resistance;
import com.sneddsy.musclemachine.repository.ResistanceRepository;
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
 * Integration tests for the {@link ResistanceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ResistanceResourceIT {

    private static final Float DEFAULT_BAND = 1F;
    private static final Float UPDATED_BAND = 2F;

    private static final Float DEFAULT_CABLE = 1F;
    private static final Float UPDATED_CABLE = 2F;

    private static final Float DEFAULT_FREE_WEIGHT = 1F;
    private static final Float UPDATED_FREE_WEIGHT = 2F;

    private static final String ENTITY_API_URL = "/api/resistances";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ResistanceRepository resistanceRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restResistanceMockMvc;

    private Resistance resistance;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Resistance createEntity(EntityManager em) {
        Resistance resistance = new Resistance().band(DEFAULT_BAND).cable(DEFAULT_CABLE).freeWeight(DEFAULT_FREE_WEIGHT);
        return resistance;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Resistance createUpdatedEntity(EntityManager em) {
        Resistance resistance = new Resistance().band(UPDATED_BAND).cable(UPDATED_CABLE).freeWeight(UPDATED_FREE_WEIGHT);
        return resistance;
    }

    @BeforeEach
    public void initTest() {
        resistance = createEntity(em);
    }

    @Test
    @Transactional
    void createResistance() throws Exception {
        int databaseSizeBeforeCreate = resistanceRepository.findAll().size();
        // Create the Resistance
        restResistanceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(resistance)))
            .andExpect(status().isCreated());

        // Validate the Resistance in the database
        List<Resistance> resistanceList = resistanceRepository.findAll();
        assertThat(resistanceList).hasSize(databaseSizeBeforeCreate + 1);
        Resistance testResistance = resistanceList.get(resistanceList.size() - 1);
        assertThat(testResistance.getBand()).isEqualTo(DEFAULT_BAND);
        assertThat(testResistance.getCable()).isEqualTo(DEFAULT_CABLE);
        assertThat(testResistance.getFreeWeight()).isEqualTo(DEFAULT_FREE_WEIGHT);
    }

    @Test
    @Transactional
    void createResistanceWithExistingId() throws Exception {
        // Create the Resistance with an existing ID
        resistance.setId(1L);

        int databaseSizeBeforeCreate = resistanceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restResistanceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(resistance)))
            .andExpect(status().isBadRequest());

        // Validate the Resistance in the database
        List<Resistance> resistanceList = resistanceRepository.findAll();
        assertThat(resistanceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllResistances() throws Exception {
        // Initialize the database
        resistanceRepository.saveAndFlush(resistance);

        // Get all the resistanceList
        restResistanceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(resistance.getId().intValue())))
            .andExpect(jsonPath("$.[*].band").value(hasItem(DEFAULT_BAND.doubleValue())))
            .andExpect(jsonPath("$.[*].cable").value(hasItem(DEFAULT_CABLE.doubleValue())))
            .andExpect(jsonPath("$.[*].freeWeight").value(hasItem(DEFAULT_FREE_WEIGHT.doubleValue())));
    }

    @Test
    @Transactional
    void getResistance() throws Exception {
        // Initialize the database
        resistanceRepository.saveAndFlush(resistance);

        // Get the resistance
        restResistanceMockMvc
            .perform(get(ENTITY_API_URL_ID, resistance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(resistance.getId().intValue()))
            .andExpect(jsonPath("$.band").value(DEFAULT_BAND.doubleValue()))
            .andExpect(jsonPath("$.cable").value(DEFAULT_CABLE.doubleValue()))
            .andExpect(jsonPath("$.freeWeight").value(DEFAULT_FREE_WEIGHT.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingResistance() throws Exception {
        // Get the resistance
        restResistanceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingResistance() throws Exception {
        // Initialize the database
        resistanceRepository.saveAndFlush(resistance);

        int databaseSizeBeforeUpdate = resistanceRepository.findAll().size();

        // Update the resistance
        Resistance updatedResistance = resistanceRepository.findById(resistance.getId()).get();
        // Disconnect from session so that the updates on updatedResistance are not directly saved in db
        em.detach(updatedResistance);
        updatedResistance.band(UPDATED_BAND).cable(UPDATED_CABLE).freeWeight(UPDATED_FREE_WEIGHT);

        restResistanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedResistance.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedResistance))
            )
            .andExpect(status().isOk());

        // Validate the Resistance in the database
        List<Resistance> resistanceList = resistanceRepository.findAll();
        assertThat(resistanceList).hasSize(databaseSizeBeforeUpdate);
        Resistance testResistance = resistanceList.get(resistanceList.size() - 1);
        assertThat(testResistance.getBand()).isEqualTo(UPDATED_BAND);
        assertThat(testResistance.getCable()).isEqualTo(UPDATED_CABLE);
        assertThat(testResistance.getFreeWeight()).isEqualTo(UPDATED_FREE_WEIGHT);
    }

    @Test
    @Transactional
    void putNonExistingResistance() throws Exception {
        int databaseSizeBeforeUpdate = resistanceRepository.findAll().size();
        resistance.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restResistanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, resistance.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(resistance))
            )
            .andExpect(status().isBadRequest());

        // Validate the Resistance in the database
        List<Resistance> resistanceList = resistanceRepository.findAll();
        assertThat(resistanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchResistance() throws Exception {
        int databaseSizeBeforeUpdate = resistanceRepository.findAll().size();
        resistance.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResistanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(resistance))
            )
            .andExpect(status().isBadRequest());

        // Validate the Resistance in the database
        List<Resistance> resistanceList = resistanceRepository.findAll();
        assertThat(resistanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamResistance() throws Exception {
        int databaseSizeBeforeUpdate = resistanceRepository.findAll().size();
        resistance.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResistanceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(resistance)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Resistance in the database
        List<Resistance> resistanceList = resistanceRepository.findAll();
        assertThat(resistanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateResistanceWithPatch() throws Exception {
        // Initialize the database
        resistanceRepository.saveAndFlush(resistance);

        int databaseSizeBeforeUpdate = resistanceRepository.findAll().size();

        // Update the resistance using partial update
        Resistance partialUpdatedResistance = new Resistance();
        partialUpdatedResistance.setId(resistance.getId());

        partialUpdatedResistance.band(UPDATED_BAND).cable(UPDATED_CABLE);

        restResistanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedResistance.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedResistance))
            )
            .andExpect(status().isOk());

        // Validate the Resistance in the database
        List<Resistance> resistanceList = resistanceRepository.findAll();
        assertThat(resistanceList).hasSize(databaseSizeBeforeUpdate);
        Resistance testResistance = resistanceList.get(resistanceList.size() - 1);
        assertThat(testResistance.getBand()).isEqualTo(UPDATED_BAND);
        assertThat(testResistance.getCable()).isEqualTo(UPDATED_CABLE);
        assertThat(testResistance.getFreeWeight()).isEqualTo(DEFAULT_FREE_WEIGHT);
    }

    @Test
    @Transactional
    void fullUpdateResistanceWithPatch() throws Exception {
        // Initialize the database
        resistanceRepository.saveAndFlush(resistance);

        int databaseSizeBeforeUpdate = resistanceRepository.findAll().size();

        // Update the resistance using partial update
        Resistance partialUpdatedResistance = new Resistance();
        partialUpdatedResistance.setId(resistance.getId());

        partialUpdatedResistance.band(UPDATED_BAND).cable(UPDATED_CABLE).freeWeight(UPDATED_FREE_WEIGHT);

        restResistanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedResistance.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedResistance))
            )
            .andExpect(status().isOk());

        // Validate the Resistance in the database
        List<Resistance> resistanceList = resistanceRepository.findAll();
        assertThat(resistanceList).hasSize(databaseSizeBeforeUpdate);
        Resistance testResistance = resistanceList.get(resistanceList.size() - 1);
        assertThat(testResistance.getBand()).isEqualTo(UPDATED_BAND);
        assertThat(testResistance.getCable()).isEqualTo(UPDATED_CABLE);
        assertThat(testResistance.getFreeWeight()).isEqualTo(UPDATED_FREE_WEIGHT);
    }

    @Test
    @Transactional
    void patchNonExistingResistance() throws Exception {
        int databaseSizeBeforeUpdate = resistanceRepository.findAll().size();
        resistance.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restResistanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, resistance.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(resistance))
            )
            .andExpect(status().isBadRequest());

        // Validate the Resistance in the database
        List<Resistance> resistanceList = resistanceRepository.findAll();
        assertThat(resistanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchResistance() throws Exception {
        int databaseSizeBeforeUpdate = resistanceRepository.findAll().size();
        resistance.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResistanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(resistance))
            )
            .andExpect(status().isBadRequest());

        // Validate the Resistance in the database
        List<Resistance> resistanceList = resistanceRepository.findAll();
        assertThat(resistanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamResistance() throws Exception {
        int databaseSizeBeforeUpdate = resistanceRepository.findAll().size();
        resistance.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResistanceMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(resistance))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Resistance in the database
        List<Resistance> resistanceList = resistanceRepository.findAll();
        assertThat(resistanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteResistance() throws Exception {
        // Initialize the database
        resistanceRepository.saveAndFlush(resistance);

        int databaseSizeBeforeDelete = resistanceRepository.findAll().size();

        // Delete the resistance
        restResistanceMockMvc
            .perform(delete(ENTITY_API_URL_ID, resistance.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Resistance> resistanceList = resistanceRepository.findAll();
        assertThat(resistanceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
