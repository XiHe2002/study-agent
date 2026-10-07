package com.studyagent.demo.cet6;

import java.util.List;

public class Cet6ReadingProfileResponse {

    private Cet6ReadingReportResponse report;
    private List<Cet6SectionDiagnosisResponse> sectionDiagnoses;

    private String prioritySection;
    private double prioritySectionAccuracy;

    private List<String> priorityWeakPointTags;

    private String reviewSuggestion;

    public Cet6ReadingProfileResponse(
            Cet6ReadingReportResponse report,
            List<Cet6SectionDiagnosisResponse> sectionDiagnoses,
            String prioritySection,
            double prioritySectionAccuracy,
            List<String> priorityWeakPointTags,
            String reviewSuggestion
    ) {
        this.report = report;
        this.sectionDiagnoses = sectionDiagnoses;
        this.prioritySection = prioritySection;
        this.prioritySectionAccuracy =
                prioritySectionAccuracy;
        this.priorityWeakPointTags =
                priorityWeakPointTags;
        this.reviewSuggestion =
                reviewSuggestion;
    }

    public Cet6ReadingReportResponse getReport() {
        return report;
    }

    public List<Cet6SectionDiagnosisResponse>
    getSectionDiagnoses() {
        return sectionDiagnoses;
    }

    public String getPrioritySection() {
        return prioritySection;
    }

    public double getPrioritySectionAccuracy() {
        return prioritySectionAccuracy;
    }

    public List<String> getPriorityWeakPointTags() {
        return priorityWeakPointTags;
    }

    public String getReviewSuggestion() {
        return reviewSuggestion;
    }
}