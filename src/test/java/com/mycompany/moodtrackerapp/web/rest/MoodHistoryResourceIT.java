package com.mycompany.moodtrackerapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.moodtrackerapp.IntegrationTest;
import com.mycompany.moodtrackerapp.domain.MoodHistory;
import com.mycompany.moodtrackerapp.domain.enumeration.MoodStatus;
import com.mycompany.moodtrackerapp.repository.MoodHistoryRepository;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link MoodHistoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MoodHistoryResourceIT {

    private static final String DEFAULT_IDENTIFIER = "AAAAAAAAAA";
    private static final String UPDATED_IDENTIFIER = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_SUBMISSION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_SUBMISSION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final MoodStatus DEFAULT_MOOD_STATUS = MoodStatus.HAPPY;
    private static final MoodStatus UPDATED_MOOD_STATUS = MoodStatus.NORMAL;

    private static final String DEFAULT_MOOD_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_MOOD_DETAILS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/mood-histories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MoodHistoryRepository moodHistoryRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMoodHistoryMockMvc;

    private MoodHistory moodHistory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MoodHistory createEntity(EntityManager em) {
        MoodHistory moodHistory = new MoodHistory()
            .identifier(DEFAULT_IDENTIFIER)
            .submissionDate(DEFAULT_SUBMISSION_DATE)
            .moodStatus(DEFAULT_MOOD_STATUS)
            .moodDetails(DEFAULT_MOOD_DETAILS);
        return moodHistory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MoodHistory createUpdatedEntity(EntityManager em) {
        MoodHistory moodHistory = new MoodHistory()
            .identifier(UPDATED_IDENTIFIER)
            .submissionDate(UPDATED_SUBMISSION_DATE)
            .moodStatus(UPDATED_MOOD_STATUS)
            .moodDetails(UPDATED_MOOD_DETAILS);
        return moodHistory;
    }

    @BeforeEach
    public void initTest() {
        moodHistory = createEntity(em);
    }

    @Test
    @Transactional
    void createMoodHistory() throws Exception {
        int databaseSizeBeforeCreate = moodHistoryRepository.findAll().size();
        // Create the MoodHistory
        restMoodHistoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(moodHistory)))
            .andExpect(status().isCreated());

        // Validate the MoodHistory in the database
        List<MoodHistory> moodHistoryList = moodHistoryRepository.findAll();
        assertThat(moodHistoryList).hasSize(databaseSizeBeforeCreate + 1);
        MoodHistory testMoodHistory = moodHistoryList.get(moodHistoryList.size() - 1);
        assertThat(testMoodHistory.getIdentifier()).isEqualTo(DEFAULT_IDENTIFIER);
        assertThat(testMoodHistory.getSubmissionDate()).isEqualTo(DEFAULT_SUBMISSION_DATE);
        assertThat(testMoodHistory.getMoodStatus()).isEqualTo(DEFAULT_MOOD_STATUS);
        assertThat(testMoodHistory.getMoodDetails()).isEqualTo(DEFAULT_MOOD_DETAILS);
    }

    @Test
    @Transactional
    void createMoodHistoryWithExistingId() throws Exception {
        // Create the MoodHistory with an existing ID
        moodHistory.setId(1L);

        int databaseSizeBeforeCreate = moodHistoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMoodHistoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(moodHistory)))
            .andExpect(status().isBadRequest());

        // Validate the MoodHistory in the database
        List<MoodHistory> moodHistoryList = moodHistoryRepository.findAll();
        assertThat(moodHistoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIdentifierIsRequired() throws Exception {
        int databaseSizeBeforeTest = moodHistoryRepository.findAll().size();
        // set the field null
        moodHistory.setIdentifier(null);

        // Create the MoodHistory, which fails.

        restMoodHistoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(moodHistory)))
            .andExpect(status().isBadRequest());

        List<MoodHistory> moodHistoryList = moodHistoryRepository.findAll();
        assertThat(moodHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSubmissionDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = moodHistoryRepository.findAll().size();
        // set the field null
        moodHistory.setSubmissionDate(null);

        // Create the MoodHistory, which fails.

        restMoodHistoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(moodHistory)))
            .andExpect(status().isBadRequest());

        List<MoodHistory> moodHistoryList = moodHistoryRepository.findAll();
        assertThat(moodHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMoodStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = moodHistoryRepository.findAll().size();
        // set the field null
        moodHistory.setMoodStatus(null);

        // Create the MoodHistory, which fails.

        restMoodHistoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(moodHistory)))
            .andExpect(status().isBadRequest());

        List<MoodHistory> moodHistoryList = moodHistoryRepository.findAll();
        assertThat(moodHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMoodHistories() throws Exception {
        // Initialize the database
        moodHistoryRepository.saveAndFlush(moodHistory);

        // Get all the moodHistoryList
        restMoodHistoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(moodHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].identifier").value(hasItem(DEFAULT_IDENTIFIER)))
            .andExpect(jsonPath("$.[*].submissionDate").value(hasItem(DEFAULT_SUBMISSION_DATE.toString())))
            .andExpect(jsonPath("$.[*].moodStatus").value(hasItem(DEFAULT_MOOD_STATUS.toString())))
            .andExpect(jsonPath("$.[*].moodDetails").value(hasItem(DEFAULT_MOOD_DETAILS)));
    }

    @Test
    @Transactional
    void getMoodHistory() throws Exception {
        // Initialize the database
        moodHistoryRepository.saveAndFlush(moodHistory);

        // Get the moodHistory
        restMoodHistoryMockMvc
            .perform(get(ENTITY_API_URL_ID, moodHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(moodHistory.getId().intValue()))
            .andExpect(jsonPath("$.identifier").value(DEFAULT_IDENTIFIER))
            .andExpect(jsonPath("$.submissionDate").value(DEFAULT_SUBMISSION_DATE.toString()))
            .andExpect(jsonPath("$.moodStatus").value(DEFAULT_MOOD_STATUS.toString()))
            .andExpect(jsonPath("$.moodDetails").value(DEFAULT_MOOD_DETAILS));
    }

    @Test
    @Transactional
    void getNonExistingMoodHistory() throws Exception {
        // Get the moodHistory
        restMoodHistoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMoodHistory() throws Exception {
        // Initialize the database
        moodHistoryRepository.saveAndFlush(moodHistory);

        int databaseSizeBeforeUpdate = moodHistoryRepository.findAll().size();

        // Update the moodHistory
        MoodHistory updatedMoodHistory = moodHistoryRepository.findById(moodHistory.getId()).get();
        // Disconnect from session so that the updates on updatedMoodHistory are not directly saved in db
        em.detach(updatedMoodHistory);
        updatedMoodHistory
            .identifier(UPDATED_IDENTIFIER)
            .submissionDate(UPDATED_SUBMISSION_DATE)
            .moodStatus(UPDATED_MOOD_STATUS)
            .moodDetails(UPDATED_MOOD_DETAILS);

        restMoodHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMoodHistory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMoodHistory))
            )
            .andExpect(status().isOk());

        // Validate the MoodHistory in the database
        List<MoodHistory> moodHistoryList = moodHistoryRepository.findAll();
        assertThat(moodHistoryList).hasSize(databaseSizeBeforeUpdate);
        MoodHistory testMoodHistory = moodHistoryList.get(moodHistoryList.size() - 1);
        assertThat(testMoodHistory.getIdentifier()).isEqualTo(UPDATED_IDENTIFIER);
        assertThat(testMoodHistory.getSubmissionDate()).isEqualTo(UPDATED_SUBMISSION_DATE);
        assertThat(testMoodHistory.getMoodStatus()).isEqualTo(UPDATED_MOOD_STATUS);
        assertThat(testMoodHistory.getMoodDetails()).isEqualTo(UPDATED_MOOD_DETAILS);
    }

    @Test
    @Transactional
    void putNonExistingMoodHistory() throws Exception {
        int databaseSizeBeforeUpdate = moodHistoryRepository.findAll().size();
        moodHistory.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMoodHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, moodHistory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(moodHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the MoodHistory in the database
        List<MoodHistory> moodHistoryList = moodHistoryRepository.findAll();
        assertThat(moodHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMoodHistory() throws Exception {
        int databaseSizeBeforeUpdate = moodHistoryRepository.findAll().size();
        moodHistory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMoodHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(moodHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the MoodHistory in the database
        List<MoodHistory> moodHistoryList = moodHistoryRepository.findAll();
        assertThat(moodHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMoodHistory() throws Exception {
        int databaseSizeBeforeUpdate = moodHistoryRepository.findAll().size();
        moodHistory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMoodHistoryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(moodHistory)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MoodHistory in the database
        List<MoodHistory> moodHistoryList = moodHistoryRepository.findAll();
        assertThat(moodHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMoodHistoryWithPatch() throws Exception {
        // Initialize the database
        moodHistoryRepository.saveAndFlush(moodHistory);

        int databaseSizeBeforeUpdate = moodHistoryRepository.findAll().size();

        // Update the moodHistory using partial update
        MoodHistory partialUpdatedMoodHistory = new MoodHistory();
        partialUpdatedMoodHistory.setId(moodHistory.getId());

        partialUpdatedMoodHistory.moodDetails(UPDATED_MOOD_DETAILS);

        restMoodHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMoodHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMoodHistory))
            )
            .andExpect(status().isOk());

        // Validate the MoodHistory in the database
        List<MoodHistory> moodHistoryList = moodHistoryRepository.findAll();
        assertThat(moodHistoryList).hasSize(databaseSizeBeforeUpdate);
        MoodHistory testMoodHistory = moodHistoryList.get(moodHistoryList.size() - 1);
        assertThat(testMoodHistory.getIdentifier()).isEqualTo(DEFAULT_IDENTIFIER);
        assertThat(testMoodHistory.getSubmissionDate()).isEqualTo(DEFAULT_SUBMISSION_DATE);
        assertThat(testMoodHistory.getMoodStatus()).isEqualTo(DEFAULT_MOOD_STATUS);
        assertThat(testMoodHistory.getMoodDetails()).isEqualTo(UPDATED_MOOD_DETAILS);
    }

    @Test
    @Transactional
    void fullUpdateMoodHistoryWithPatch() throws Exception {
        // Initialize the database
        moodHistoryRepository.saveAndFlush(moodHistory);

        int databaseSizeBeforeUpdate = moodHistoryRepository.findAll().size();

        // Update the moodHistory using partial update
        MoodHistory partialUpdatedMoodHistory = new MoodHistory();
        partialUpdatedMoodHistory.setId(moodHistory.getId());

        partialUpdatedMoodHistory
            .identifier(UPDATED_IDENTIFIER)
            .submissionDate(UPDATED_SUBMISSION_DATE)
            .moodStatus(UPDATED_MOOD_STATUS)
            .moodDetails(UPDATED_MOOD_DETAILS);

        restMoodHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMoodHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMoodHistory))
            )
            .andExpect(status().isOk());

        // Validate the MoodHistory in the database
        List<MoodHistory> moodHistoryList = moodHistoryRepository.findAll();
        assertThat(moodHistoryList).hasSize(databaseSizeBeforeUpdate);
        MoodHistory testMoodHistory = moodHistoryList.get(moodHistoryList.size() - 1);
        assertThat(testMoodHistory.getIdentifier()).isEqualTo(UPDATED_IDENTIFIER);
        assertThat(testMoodHistory.getSubmissionDate()).isEqualTo(UPDATED_SUBMISSION_DATE);
        assertThat(testMoodHistory.getMoodStatus()).isEqualTo(UPDATED_MOOD_STATUS);
        assertThat(testMoodHistory.getMoodDetails()).isEqualTo(UPDATED_MOOD_DETAILS);
    }

    @Test
    @Transactional
    void patchNonExistingMoodHistory() throws Exception {
        int databaseSizeBeforeUpdate = moodHistoryRepository.findAll().size();
        moodHistory.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMoodHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, moodHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(moodHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the MoodHistory in the database
        List<MoodHistory> moodHistoryList = moodHistoryRepository.findAll();
        assertThat(moodHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMoodHistory() throws Exception {
        int databaseSizeBeforeUpdate = moodHistoryRepository.findAll().size();
        moodHistory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMoodHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(moodHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the MoodHistory in the database
        List<MoodHistory> moodHistoryList = moodHistoryRepository.findAll();
        assertThat(moodHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMoodHistory() throws Exception {
        int databaseSizeBeforeUpdate = moodHistoryRepository.findAll().size();
        moodHistory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMoodHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(moodHistory))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MoodHistory in the database
        List<MoodHistory> moodHistoryList = moodHistoryRepository.findAll();
        assertThat(moodHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMoodHistory() throws Exception {
        // Initialize the database
        moodHistoryRepository.saveAndFlush(moodHistory);

        int databaseSizeBeforeDelete = moodHistoryRepository.findAll().size();

        // Delete the moodHistory
        restMoodHistoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, moodHistory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MoodHistory> moodHistoryList = moodHistoryRepository.findAll();
        assertThat(moodHistoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
