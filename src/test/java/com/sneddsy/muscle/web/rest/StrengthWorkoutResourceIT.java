package com.sneddsy.muscle.web.rest;

import static com.sneddsy.muscle.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sneddsy.muscle.IntegrationTest;
import com.sneddsy.muscle.domain.StrengthWorkout;
import com.sneddsy.muscle.repository.StrengthWorkoutRepository;
import com.sneddsy.muscle.service.StrengthWorkoutService;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link StrengthWorkoutResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class StrengthWorkoutResourceIT {

    private static final ZonedDateTime DEFAULT_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/strength-workouts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private StrengthWorkoutRepository strengthWorkoutRepository;

    @Mock
    private StrengthWorkoutRepository strengthWorkoutRepositoryMock;

    @Mock
    private StrengthWorkoutService strengthWorkoutServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStrengthWorkoutMockMvc;

    private StrengthWorkout strengthWorkout;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StrengthWorkout createEntity(EntityManager em) {
        StrengthWorkout strengthWorkout = new StrengthWorkout().time(DEFAULT_TIME);
        return strengthWorkout;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StrengthWorkout createUpdatedEntity(EntityManager em) {
        StrengthWorkout strengthWorkout = new StrengthWorkout().time(UPDATED_TIME);
        return strengthWorkout;
    }

    @BeforeEach
    public void initTest() {
        strengthWorkout = createEntity(em);
    }

    @Test
    @Transactional
    void createStrengthWorkout() throws Exception {
        int databaseSizeBeforeCreate = strengthWorkoutRepository.findAll().size();
        // Create the StrengthWorkout
        restStrengthWorkoutMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(strengthWorkout))
            )
            .andExpect(status().isCreated());

        // Validate the StrengthWorkout in the database
        List<StrengthWorkout> strengthWorkoutList = strengthWorkoutRepository.findAll();
        assertThat(strengthWorkoutList).hasSize(databaseSizeBeforeCreate + 1);
        StrengthWorkout testStrengthWorkout = strengthWorkoutList.get(strengthWorkoutList.size() - 1);
        assertThat(testStrengthWorkout.getTime()).isEqualTo(DEFAULT_TIME);
    }

    @Test
    @Transactional
    void createStrengthWorkoutWithExistingId() throws Exception {
        // Create the StrengthWorkout with an existing ID
        strengthWorkout.setId(1L);

        int databaseSizeBeforeCreate = strengthWorkoutRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStrengthWorkoutMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(strengthWorkout))
            )
            .andExpect(status().isBadRequest());

        // Validate the StrengthWorkout in the database
        List<StrengthWorkout> strengthWorkoutList = strengthWorkoutRepository.findAll();
        assertThat(strengthWorkoutList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = strengthWorkoutRepository.findAll().size();
        // set the field null
        strengthWorkout.setTime(null);

        // Create the StrengthWorkout, which fails.

        restStrengthWorkoutMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(strengthWorkout))
            )
            .andExpect(status().isBadRequest());

        List<StrengthWorkout> strengthWorkoutList = strengthWorkoutRepository.findAll();
        assertThat(strengthWorkoutList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllStrengthWorkouts() throws Exception {
        // Initialize the database
        strengthWorkoutRepository.saveAndFlush(strengthWorkout);

        // Get all the strengthWorkoutList
        restStrengthWorkoutMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(strengthWorkout.getId().intValue())))
            .andExpect(jsonPath("$.[*].time").value(hasItem(sameInstant(DEFAULT_TIME))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllStrengthWorkoutsWithEagerRelationshipsIsEnabled() throws Exception {
        when(strengthWorkoutServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restStrengthWorkoutMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(strengthWorkoutServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllStrengthWorkoutsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(strengthWorkoutServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restStrengthWorkoutMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(strengthWorkoutRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getStrengthWorkout() throws Exception {
        // Initialize the database
        strengthWorkoutRepository.saveAndFlush(strengthWorkout);

        // Get the strengthWorkout
        restStrengthWorkoutMockMvc
            .perform(get(ENTITY_API_URL_ID, strengthWorkout.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(strengthWorkout.getId().intValue()))
            .andExpect(jsonPath("$.time").value(sameInstant(DEFAULT_TIME)));
    }

    @Test
    @Transactional
    void getNonExistingStrengthWorkout() throws Exception {
        // Get the strengthWorkout
        restStrengthWorkoutMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingStrengthWorkout() throws Exception {
        // Initialize the database
        strengthWorkoutRepository.saveAndFlush(strengthWorkout);

        int databaseSizeBeforeUpdate = strengthWorkoutRepository.findAll().size();

        // Update the strengthWorkout
        StrengthWorkout updatedStrengthWorkout = strengthWorkoutRepository.findById(strengthWorkout.getId()).get();
        // Disconnect from session so that the updates on updatedStrengthWorkout are not directly saved in db
        em.detach(updatedStrengthWorkout);
        updatedStrengthWorkout.time(UPDATED_TIME);

        restStrengthWorkoutMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedStrengthWorkout.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedStrengthWorkout))
            )
            .andExpect(status().isOk());

        // Validate the StrengthWorkout in the database
        List<StrengthWorkout> strengthWorkoutList = strengthWorkoutRepository.findAll();
        assertThat(strengthWorkoutList).hasSize(databaseSizeBeforeUpdate);
        StrengthWorkout testStrengthWorkout = strengthWorkoutList.get(strengthWorkoutList.size() - 1);
        assertThat(testStrengthWorkout.getTime()).isEqualTo(UPDATED_TIME);
    }

    @Test
    @Transactional
    void putNonExistingStrengthWorkout() throws Exception {
        int databaseSizeBeforeUpdate = strengthWorkoutRepository.findAll().size();
        strengthWorkout.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStrengthWorkoutMockMvc
            .perform(
                put(ENTITY_API_URL_ID, strengthWorkout.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(strengthWorkout))
            )
            .andExpect(status().isBadRequest());

        // Validate the StrengthWorkout in the database
        List<StrengthWorkout> strengthWorkoutList = strengthWorkoutRepository.findAll();
        assertThat(strengthWorkoutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStrengthWorkout() throws Exception {
        int databaseSizeBeforeUpdate = strengthWorkoutRepository.findAll().size();
        strengthWorkout.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStrengthWorkoutMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(strengthWorkout))
            )
            .andExpect(status().isBadRequest());

        // Validate the StrengthWorkout in the database
        List<StrengthWorkout> strengthWorkoutList = strengthWorkoutRepository.findAll();
        assertThat(strengthWorkoutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStrengthWorkout() throws Exception {
        int databaseSizeBeforeUpdate = strengthWorkoutRepository.findAll().size();
        strengthWorkout.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStrengthWorkoutMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(strengthWorkout))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the StrengthWorkout in the database
        List<StrengthWorkout> strengthWorkoutList = strengthWorkoutRepository.findAll();
        assertThat(strengthWorkoutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStrengthWorkoutWithPatch() throws Exception {
        // Initialize the database
        strengthWorkoutRepository.saveAndFlush(strengthWorkout);

        int databaseSizeBeforeUpdate = strengthWorkoutRepository.findAll().size();

        // Update the strengthWorkout using partial update
        StrengthWorkout partialUpdatedStrengthWorkout = new StrengthWorkout();
        partialUpdatedStrengthWorkout.setId(strengthWorkout.getId());

        restStrengthWorkoutMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStrengthWorkout.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStrengthWorkout))
            )
            .andExpect(status().isOk());

        // Validate the StrengthWorkout in the database
        List<StrengthWorkout> strengthWorkoutList = strengthWorkoutRepository.findAll();
        assertThat(strengthWorkoutList).hasSize(databaseSizeBeforeUpdate);
        StrengthWorkout testStrengthWorkout = strengthWorkoutList.get(strengthWorkoutList.size() - 1);
        assertThat(testStrengthWorkout.getTime()).isEqualTo(DEFAULT_TIME);
    }

    @Test
    @Transactional
    void fullUpdateStrengthWorkoutWithPatch() throws Exception {
        // Initialize the database
        strengthWorkoutRepository.saveAndFlush(strengthWorkout);

        int databaseSizeBeforeUpdate = strengthWorkoutRepository.findAll().size();

        // Update the strengthWorkout using partial update
        StrengthWorkout partialUpdatedStrengthWorkout = new StrengthWorkout();
        partialUpdatedStrengthWorkout.setId(strengthWorkout.getId());

        partialUpdatedStrengthWorkout.time(UPDATED_TIME);

        restStrengthWorkoutMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStrengthWorkout.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStrengthWorkout))
            )
            .andExpect(status().isOk());

        // Validate the StrengthWorkout in the database
        List<StrengthWorkout> strengthWorkoutList = strengthWorkoutRepository.findAll();
        assertThat(strengthWorkoutList).hasSize(databaseSizeBeforeUpdate);
        StrengthWorkout testStrengthWorkout = strengthWorkoutList.get(strengthWorkoutList.size() - 1);
        assertThat(testStrengthWorkout.getTime()).isEqualTo(UPDATED_TIME);
    }

    @Test
    @Transactional
    void patchNonExistingStrengthWorkout() throws Exception {
        int databaseSizeBeforeUpdate = strengthWorkoutRepository.findAll().size();
        strengthWorkout.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStrengthWorkoutMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, strengthWorkout.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(strengthWorkout))
            )
            .andExpect(status().isBadRequest());

        // Validate the StrengthWorkout in the database
        List<StrengthWorkout> strengthWorkoutList = strengthWorkoutRepository.findAll();
        assertThat(strengthWorkoutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStrengthWorkout() throws Exception {
        int databaseSizeBeforeUpdate = strengthWorkoutRepository.findAll().size();
        strengthWorkout.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStrengthWorkoutMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(strengthWorkout))
            )
            .andExpect(status().isBadRequest());

        // Validate the StrengthWorkout in the database
        List<StrengthWorkout> strengthWorkoutList = strengthWorkoutRepository.findAll();
        assertThat(strengthWorkoutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStrengthWorkout() throws Exception {
        int databaseSizeBeforeUpdate = strengthWorkoutRepository.findAll().size();
        strengthWorkout.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStrengthWorkoutMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(strengthWorkout))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the StrengthWorkout in the database
        List<StrengthWorkout> strengthWorkoutList = strengthWorkoutRepository.findAll();
        assertThat(strengthWorkoutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStrengthWorkout() throws Exception {
        // Initialize the database
        strengthWorkoutRepository.saveAndFlush(strengthWorkout);

        int databaseSizeBeforeDelete = strengthWorkoutRepository.findAll().size();

        // Delete the strengthWorkout
        restStrengthWorkoutMockMvc
            .perform(delete(ENTITY_API_URL_ID, strengthWorkout.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<StrengthWorkout> strengthWorkoutList = strengthWorkoutRepository.findAll();
        assertThat(strengthWorkoutList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
