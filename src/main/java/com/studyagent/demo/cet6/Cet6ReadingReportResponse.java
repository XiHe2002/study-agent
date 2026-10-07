package com.studyagent.demo.cet6;

import java.util.List;

public class Cet6ReadingReportResponse {

    private int totalQuestions;
    private int correctCount;
    private int wrongCount;
    private double accuracy;
    private List<Cet6SectionStat> sections;
    private List<Cet6PreviewResponse> wrongQuestions;

    public Cet6ReadingReportResponse(
            int totalQuestions,
            int correctCount,
            int wrongCount,
            double accuracy,
            List<Cet6SectionStat> sections,
            List<Cet6PreviewResponse> wrongQuestions
    ) {
        this.totalQuestions = totalQuestions;
        this.correctCount = correctCount;
        this.wrongCount = wrongCount;
        this.accuracy = accuracy;
        this.sections = sections;
        this.wrongQuestions = wrongQuestions;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public int getCorrectCount() {
        return correctCount;
    }

    public int getWrongCount() {
        return wrongCount;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public List<Cet6SectionStat> getSections() {
        return sections;
    }

    public List<Cet6PreviewResponse> getWrongQuestions() {
        return wrongQuestions;
    }
}