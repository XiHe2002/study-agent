package com.studyagent.demo.cet6;

import java.util.List;

public class Cet6AiAnalysis {

    private String feedback;
    private List<String> weakPoints;
    private List<String> weakPointTags;

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
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

    public void setWeakPointTags(List<String> weakPointTags) {
        this.weakPointTags = weakPointTags;
    }
}