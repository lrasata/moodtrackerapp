package com.mycompany.moodtrackerapp.repository;

import com.mycompany.moodtrackerapp.domain.MoodHistory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the MoodHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MoodHistoryRepository extends JpaRepository<MoodHistory, Long> {}
