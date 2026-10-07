package com.studyagent.demo.cet6;

public class Cet6PreviewResponse {

    private Integer examQuestionNo;
    private String section;
    private String userAnswer;
    private String referenceAnswer;
    private boolean correct;
    private String answerSource;

    public Cet6PreviewResponse(
            Integer examQuestionNo,
            String section,
            String userAnswer,
            String referenceAnswer,
            boolean correct,
            String answerSource
    ) {
        this.examQuestionNo = examQuestionNo;
        this.section = section;
        this.userAnswer = userAnswer;
        this.referenceAnswer = referenceAnswer;
        this.correct = correct;
        this.answerSource = answerSource;
    }

    public Integer getExamQuestionNo() {
        return examQuestionNo;
    }

    public String getSection() {
        return section;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public String getReferenceAnswer() {
        return referenceAnswer;
    }

    public boolean isCorrect() {
        return correct;
    }

    public String getAnswerSource() {
        return answerSource;
    }
}