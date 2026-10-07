package com.studyagent.demo.cet6;

import java.util.List;

public class Cet6SectionDiagnosisResponse {

    private String section;
    private int wrongCount;
    private String feedback;
    private List<String> weakPoints;
    private List<String> weakPointTags;

    public Cet6SectionDiagnosisResponse(
            String section,
            int wrongCount,
            String feedback,
            List<String> weakPoints,
            List<String> weakPointTags
    ) {
        this.section = section;
        this.wrongCount = wrongCount;
        this.feedback = feedback;
        this.weakPoints = weakPoints;
        this.weakPointTags = weakPointTags;
    }

    public String getSection() {
        return section;
    }

    public int getWrongCount() {
        return wrongCount;
    }

    public String getFeedback() {
        return feedback;
    }

    public List<String> getWeakPoints() {
        return weakPoints;
    }

    public List<String> getWeakPointTags() {
        return weakPointTags;
    }
}