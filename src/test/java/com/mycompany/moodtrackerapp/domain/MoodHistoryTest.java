package com.mycompany.moodtrackerapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.moodtrackerapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MoodHistoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MoodHistory.class);
        MoodHistory moodHistory1 = new MoodHistory();
        moodHistory1.setId(1L);
        MoodHistory moodHistory2 = new MoodHistory();
        moodHistory2.setId(moodHistory1.getId());
        assertThat(moodHistory1).isEqualTo(moodHistory2);
        moodHistory2.setId(2L);
        assertThat(moodHistory1).isNotEqualTo(moodHistory2);
        moodHistory1.setId(null);
        assertThat(moodHistory1).isNotEqualTo(moodHistory2);
    }
}
