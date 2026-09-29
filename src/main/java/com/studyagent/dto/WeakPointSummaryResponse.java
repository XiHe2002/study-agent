package com.studyagent.dto;

public class WeakPointSummaryResponse {

    private String weakPoint;
    private Integer count;

    public WeakPointSummaryResponse(
            String weakPoint,
            Integer count
    ) {
        this.weakPoint = weakPoint;
        this.count = count;
    }

    public String getWeakPoint() {
        return weakPoint;
    }

    public Integer getCount() {
        return count;
    }
}