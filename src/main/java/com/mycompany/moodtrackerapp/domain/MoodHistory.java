package com.mycompany.moodtrackerapp.domain;

import com.mycompany.moodtrackerapp.domain.enumeration.MoodStatus;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A MoodHistory.
 */
@Entity
@Table(name = "mood_history")
public class MoodHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "identifier", nullable = false)
    private String identifier;

    @NotNull
    @Column(name = "submission_date", nullable = false)
    private LocalDate submissionDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "mood_status", nullable = false)
    private MoodStatus moodStatus;

    @Size(max = 350)
    @Column(name = "mood_details", length = 350)
    private String moodDetails;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MoodHistory id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public MoodHistory identifier(String identifier) {
        this.setIdentifier(identifier);
        return this;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public LocalDate getSubmissionDate() {
        return this.submissionDate;
    }

    public MoodHistory submissionDate(LocalDate submissionDate) {
        this.setSubmissionDate(submissionDate);
        return this;
    }

    public void setSubmissionDate(LocalDate submissionDate) {
        this.submissionDate = submissionDate;
    }

    public MoodStatus getMoodStatus() {
        return this.moodStatus;
    }

    public MoodHistory moodStatus(MoodStatus moodStatus) {
        this.setMoodStatus(moodStatus);
        return this;
    }

    public void setMoodStatus(MoodStatus moodStatus) {
        this.moodStatus = moodStatus;
    }

    public String getMoodDetails() {
        return this.moodDetails;
    }

    public MoodHistory moodDetails(String moodDetails) {
        this.setMoodDetails(moodDetails);
        return this;
    }

    public void setMoodDetails(String moodDetails) {
        this.moodDetails = moodDetails;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MoodHistory)) {
            return false;
        }
        return id != null && id.equals(((MoodHistory) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MoodHistory{" +
            "id=" + getId() +
            ", identifier='" + getIdentifier() + "'" +
            ", submissionDate='" + getSubmissionDate() + "'" +
            ", moodStatus='" + getMoodStatus() + "'" +
            ", moodDetails='" + getMoodDetails() + "'" +
            "}";
    }
}
