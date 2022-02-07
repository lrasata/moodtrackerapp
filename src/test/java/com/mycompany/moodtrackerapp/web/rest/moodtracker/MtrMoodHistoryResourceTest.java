package com.mycompany.moodtrackerapp.web.rest.moodtracker;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.moodtrackerapp.IntegrationTest;
import com.mycompany.moodtrackerapp.domain.MoodHistory;
import com.mycompany.moodtrackerapp.domain.enumeration.MoodStatus;
import com.mycompany.moodtrackerapp.repository.moodtracker.MtrMoodHistoryRepository;
import com.mycompany.moodtrackerapp.web.rest.TestUtil;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/*
 * @author lrasata
 *
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MtrMoodHistoryResourceTest {

    private static final String DEFAULT_IDENTIFIER = "AAAAAAAAAA";
    private static final String UPDATED_IDENTIFIER = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_SUBMISSION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final MoodStatus DEFAULT_MOOD_STATUS = MoodStatus.HAPPY;

    private static final String DEFAULT_MOOD_DETAILS = "AAAAAAAAAA";

    private static final String ENTITY_API_URL = "/api/track-mood-histories";

    @Autowired
    private MtrMoodHistoryRepository moodHistoryRepository;

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
    void createMoodHistoryWithSubmissionDateNotCurrentDate() throws Exception {
        // Create the MoodHistory with an existing ID
        moodHistory.setSubmissionDate(LocalDate.ofEpochDay(0L));

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
    void createMoodHistoryWithUserAlreadySubmittedForTheCurrentDate() throws Exception {
        // Create the MoodHistory with an existing ID
        moodHistoryRepository.saveAndFlush(moodHistory);

        MoodHistory newMoodHistory = createEntity(em);

        int databaseSizeBeforeCreate = moodHistoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMoodHistoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(newMoodHistory))
            )
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
}
