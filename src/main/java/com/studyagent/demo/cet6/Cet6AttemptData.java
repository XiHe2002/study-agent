package com.studyagent.demo.cet6;

import java.util.List;

public class Cet6AttemptData {

    private String exam;
    private String mode;
    private List<Cet6QuestionData> objectiveQuestions;
    private Cet6SubjectiveAnswerData writing;
    private Cet6SubjectiveAnswerData translation;

    public String getExam() {
        return exam;
    }

    public void setExam(String exam) {
        this.exam = exam;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public List<Cet6QuestionData> getObjectiveQuestions() {
        return objectiveQuestions;
    }

    public void setObjectiveQuestions(
            List<Cet6QuestionData> objectiveQuestions
    ) {
        this.objectiveQuestions = objectiveQuestions;
    }

    public Cet6SubjectiveAnswerData getWriting() {
        return writing;
    }

    public void setWriting(
            Cet6SubjectiveAnswerData writing
    ) {
        this.writing = writing;
    }

    public Cet6SubjectiveAnswerData getTranslation() {
        return translation;
    }

    public void setTranslation(
            Cet6SubjectiveAnswerData translation
    ) {
        this.translation = translation;
    }
}