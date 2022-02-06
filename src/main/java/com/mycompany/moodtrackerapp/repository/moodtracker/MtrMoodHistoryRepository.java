package com.mycompany.moodtrackerapp.repository.moodtracker;

import com.mycompany.moodtrackerapp.domain.MoodHistory;
import com.mycompany.moodtrackerapp.repository.MoodHistoryRepository;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the MoodHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MtrMoodHistoryRepository extends MoodHistoryRepository {
    @Query(
        "select moodHistory from MoodHistory moodHistory where moodHistory.identifier =:identifier and moodHistory.submissionDate =:date"
    )
    Optional<MoodHistory> findOneByIdentifierandByDate(@Param("identifier") String identifier, @Param("date") LocalDate date);
}
