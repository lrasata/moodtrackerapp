package com.mycompany.moodtrackerapp.web.rest.moodtracker;

import com.mycompany.moodtrackerapp.domain.MoodHistory;
import com.mycompany.moodtrackerapp.repository.moodtracker.MtrMoodHistoryRepository;
import com.mycompany.moodtrackerapp.web.rest.errors.BadRequestAlertException;
import com.mycompany.moodtrackerapp.web.rest.errors.MtrMoodAlreadySubmittedException;
import com.mycompany.moodtrackerapp.web.rest.errors.MtrUnvalidDateException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import javax.transaction.Transactional;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.jhipster.web.util.HeaderUtil;

/**
 * REST controller for managing {@link com.mycompany.moodtrackerapp.domain.MoodHistory}.
 *
 * @author lrasata
 */
@RestController
@RequestMapping("/api")
@Transactional
public class MtrMoodHistoryResource {

    private final Logger log = LoggerFactory.getLogger(MtrMoodHistoryResource.class);

    private static final String ENTITY_NAME = "moodHistory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MtrMoodHistoryRepository moodHistoryRepository;

    public MtrMoodHistoryResource(MtrMoodHistoryRepository moodHistoryRepository) {
        this.moodHistoryRepository = moodHistoryRepository;
    }

    /**
     * {@code POST  /track-mood-histories} : Create a new moodHistory.
     *
     * @param moodHistory the moodHistory to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new moodHistory, or with status {@code 400 (Bad Request)} if the moodHistory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/track-mood-histories")
    public ResponseEntity<MoodHistory> createMoodHistory(@Valid @RequestBody MoodHistory moodHistory) throws URISyntaxException {
        log.debug("REST request to save MoodHistory : {}", moodHistory);
        if (moodHistory.getId() != null) {
            throw new BadRequestAlertException("A new moodHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }

        // Throw MtrUnvalidDateException if SubmissionDate is not currentDate
        LocalDate currenDate = LocalDate.now();
        String formattedCurrentDate = currenDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        if (!moodHistory.getSubmissionDate().toString().equals(formattedCurrentDate)) {
            throw new MtrUnvalidDateException();
        }

        Optional<MoodHistory> mOptional =
            this.moodHistoryRepository.findOneByIdentifierandByDate(moodHistory.getIdentifier(), LocalDate.parse(formattedCurrentDate));
        if (mOptional.isPresent()) {
            throw new MtrMoodAlreadySubmittedException();
        }

        MoodHistory result = moodHistoryRepository.save(moodHistory);
        return ResponseEntity
            .created(new URI("/api/mood-tracker/mood-histories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
}
