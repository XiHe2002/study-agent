package com.studyagent.demo.cet6;

public class Cet6DemoReportResponse {

    private String exam;
    private String dataNotice;

    private Cet6ReadingProfileResponse reading;
    private Cet6ObjectiveSectionReport listening;
    private Cet6SubjectiveEvaluationResponse writing;
    private Cet6SubjectiveEvaluationResponse translation;

    public Cet6DemoReportResponse(
            String exam,
            String dataNotice,
            Cet6ReadingProfileResponse reading,
            Cet6ObjectiveSectionReport listening,
            Cet6SubjectiveEvaluationResponse writing,
            Cet6SubjectiveEvaluationResponse translation
    ) {
        this.exam = exam;
        this.dataNotice = dataNotice;
        this.reading = reading;
        this.listening = listening;
        this.writing = writing;
        this.translation = translation;
    }

    public String getExam() {
        return exam;
    }

    public String getDataNotice() {
        return dataNotice;
    }

    public Cet6ReadingProfileResponse getReading() {
        return reading;
    }

    public Cet6ObjectiveSectionReport getListening() {
        return listening;
    }

    public Cet6SubjectiveEvaluationResponse getWriting() {
        return writing;
    }

    public Cet6SubjectiveEvaluationResponse getTranslation() {
        return translation;
    }
}