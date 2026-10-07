package com.studyagent.demo.cet6;

import java.util.List;

public class Cet6SubjectiveEvaluationResponse {

    private String section;
    private String answerSource;
    private String scoreType;
    private Integer score;
    private Integer wordCount;
    private String feedback;
    private List<String> strengths;
    private List<String> weakPoints;
    private List<String> weakPointTags;

    public Cet6SubjectiveEvaluationResponse(
            String section,
            String answerSource,
            String scoreType,
            Integer score,
            Integer wordCount,
            String feedback,
            List<String> strengths,
            List<String> weakPoints,
            List<String> weakPointTags
    ) {
        this.section = section;
        this.answerSource = answerSource;
        this.scoreType = scoreType;
        this.score = score;
        this.wordCount = wordCount;
        this.feedback = feedback;
        this.strengths = strengths;
        this.weakPoints = weakPoints;
        this.weakPointTags = weakPointTags;
    }

    public String getSection() {
        return section;
    }

    public String getAnswerSource() {
        return answerSource;
    }

    public String getScoreType() {
        return scoreType;
    }

    public Integer getScore() {
        return score;
    }

    public Integer getWordCount() {
        return wordCount;
    }

    public String getFeedback() {
        return feedback;
    }

    public List<String> getStrengths() {
        return strengths;
    }

    public List<String> getWeakPoints() {
        return weakPoints;
    }

    public List<String> getWeakPointTags() {
        return weakPointTags;
    }
}