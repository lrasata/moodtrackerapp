package com.mycompany.moodtrackerapp.web.rest;

import com.mycompany.moodtrackerapp.domain.MoodHistory;
import com.mycompany.moodtrackerapp.repository.MoodHistoryRepository;
import com.mycompany.moodtrackerapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.moodtrackerapp.domain.MoodHistory}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class MoodHistoryResource {

    private final Logger log = LoggerFactory.getLogger(MoodHistoryResource.class);

    private static final String ENTITY_NAME = "moodHistory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MoodHistoryRepository moodHistoryRepository;

    public MoodHistoryResource(MoodHistoryRepository moodHistoryRepository) {
        this.moodHistoryRepository = moodHistoryRepository;
    }

    /**
     * {@code POST  /mood-histories} : Create a new moodHistory.
     *
     * @param moodHistory the moodHistory to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new moodHistory, or with status {@code 400 (Bad Request)} if the moodHistory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/mood-histories")
    public ResponseEntity<MoodHistory> createMoodHistory(@Valid @RequestBody MoodHistory moodHistory) throws URISyntaxException {
        log.debug("REST request to save MoodHistory : {}", moodHistory);
        if (moodHistory.getId() != null) {
            throw new BadRequestAlertException("A new moodHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MoodHistory result = moodHistoryRepository.save(moodHistory);
        return ResponseEntity
            .created(new URI("/api/mood-histories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /mood-histories/:id} : Updates an existing moodHistory.
     *
     * @param id the id of the moodHistory to save.
     * @param moodHistory the moodHistory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated moodHistory,
     * or with status {@code 400 (Bad Request)} if the moodHistory is not valid,
     * or with status {@code 500 (Internal Server Error)} if the moodHistory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/mood-histories/{id}")
    public ResponseEntity<MoodHistory> updateMoodHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MoodHistory moodHistory
    ) throws URISyntaxException {
        log.debug("REST request to update MoodHistory : {}, {}", id, moodHistory);
        if (moodHistory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, moodHistory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!moodHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MoodHistory result = moodHistoryRepository.save(moodHistory);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, moodHistory.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /mood-histories/:id} : Partial updates given fields of an existing moodHistory, field will ignore if it is null
     *
     * @param id the id of the moodHistory to save.
     * @param moodHistory the moodHistory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated moodHistory,
     * or with status {@code 400 (Bad Request)} if the moodHistory is not valid,
     * or with status {@code 404 (Not Found)} if the moodHistory is not found,
     * or with status {@code 500 (Internal Server Error)} if the moodHistory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/mood-histories/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MoodHistory> partialUpdateMoodHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MoodHistory moodHistory
    ) throws URISyntaxException {
        log.debug("REST request to partial update MoodHistory partially : {}, {}", id, moodHistory);
        if (moodHistory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, moodHistory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!moodHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MoodHistory> result = moodHistoryRepository
            .findById(moodHistory.getId())
            .map(existingMoodHistory -> {
                if (moodHistory.getIdentifier() != null) {
                    existingMoodHistory.setIdentifier(moodHistory.getIdentifier());
                }
                if (moodHistory.getSubmissionDate() != null) {
                    existingMoodHistory.setSubmissionDate(moodHistory.getSubmissionDate());
                }
                if (moodHistory.getMoodStatus() != null) {
                    existingMoodHistory.setMoodStatus(moodHistory.getMoodStatus());
                }
                if (moodHistory.getMoodDetails() != null) {
                    existingMoodHistory.setMoodDetails(moodHistory.getMoodDetails());
                }

                return existingMoodHistory;
            })
            .map(moodHistoryRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, moodHistory.getId().toString())
        );
    }

    /**
     * {@code GET  /mood-histories} : get all the moodHistories.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of moodHistories in body.
     */
    @GetMapping("/mood-histories")
    public List<MoodHistory> getAllMoodHistories() {
        log.debug("REST request to get all MoodHistories");
        return moodHistoryRepository.findAll();
    }

    /**
     * {@code GET  /mood-histories/:id} : get the "id" moodHistory.
     *
     * @param id the id of the moodHistory to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the moodHistory, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/mood-histories/{id}")
    public ResponseEntity<MoodHistory> getMoodHistory(@PathVariable Long id) {
        log.debug("REST request to get MoodHistory : {}", id);
        Optional<MoodHistory> moodHistory = moodHistoryRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(moodHistory);
    }

    /**
     * {@code DELETE  /mood-histories/:id} : delete the "id" moodHistory.
     *
     * @param id the id of the moodHistory to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/mood-histories/{id}")
    public ResponseEntity<Void> deleteMoodHistory(@PathVariable Long id) {
        log.debug("REST request to delete MoodHistory : {}", id);
        moodHistoryRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
