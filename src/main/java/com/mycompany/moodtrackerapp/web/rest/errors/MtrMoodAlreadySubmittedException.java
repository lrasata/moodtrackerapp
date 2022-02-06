package com.mycompany.moodtrackerapp.web.rest.errors;

public class MtrMoodAlreadySubmittedException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public MtrMoodAlreadySubmittedException() {
        super(
            ErrorConstants.MOOD_ALREADY_SUBMITTED,
            "Sorry, you have already submitted your response for today, try again tomorrow!",
            "moodHistory",
            "moodalreadysubmitted"
        );
    }
}
