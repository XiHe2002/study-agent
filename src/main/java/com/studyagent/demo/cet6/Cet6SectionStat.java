package com.studyagent.demo.cet6;

public class Cet6SectionStat {

    private String section;
    private int total;
    private int correct;
    private int wrong;
    private double accuracy;

    public Cet6SectionStat(
            String section,
            int total,
            int correct,
            int wrong,
            double accuracy
    ) {
        this.section = section;
        this.total = total;
        this.correct = correct;
        this.wrong = wrong;
        this.accuracy = accuracy;
    }

    public String getSection() {
        return section;
    }

    public int getTotal() {
        return total;
    }

    public int getCorrect() {
        return correct;
    }

    public int getWrong() {
        return wrong;
    }

    public double getAccuracy() {
        return accuracy;
    }
}