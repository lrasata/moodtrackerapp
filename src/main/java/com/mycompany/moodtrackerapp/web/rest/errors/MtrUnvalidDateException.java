package com.mycompany.moodtrackerapp.web.rest.errors;

public class MtrUnvalidDateException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public MtrUnvalidDateException() {
        super(ErrorConstants.UNVALID_DATE, "Submitted date is not valid!", "moodHistory", "unvaliddate");
    }
}
