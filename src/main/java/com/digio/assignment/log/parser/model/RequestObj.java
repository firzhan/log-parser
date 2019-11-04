package com.digio.assignment.log.parser.model;

public class RequestObj {

    private long startTime = 0l;
    private long endTime = 0l;
    private int count = -1;

    public long getStartTime() {

        return startTime;
    }

    public void setStartTime(long startTime) {

        this.startTime = startTime;
    }

    public long getEndTime() {

        return endTime;
    }

    public void setEndTime(long endTime) {

        this.endTime = endTime;
    }

    public int getCount() {

        return count;
    }

    public void setCount(int count) {

        this.count = count;
    }
}
