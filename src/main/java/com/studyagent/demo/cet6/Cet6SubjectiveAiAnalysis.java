package com.studyagent.demo.cet6;

import java.util.List;

public class Cet6SubjectiveAiAnalysis {

    private Integer score;
    private String feedback;
    private List<String> strengths;
    private List<String> weakPoints;
    private List<String> weakPointTags;

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public List<String> getStrengths() {
        return strengths;
    }

    public void setStrengths(List<String> strengths) {
        this.strengths = strengths;
    }

    public List<String> getWeakPoints() {
        return weakPoints;
    }

    public void setWeakPoints(List<String> weakPoints) {
        this.weakPoints = weakPoints;
    }

    public List<String> getWeakPointTags() {
        return weakPointTags;
    }

    public void setWeakPointTags(
            List<String> weakPointTags
    ) {
        this.weakPointTags = weakPointTags;
    }
}