package com.ao1;

public class NoDataReady extends Exception {
    private int tasksToBeFinished;

    public NoDataReady(int tasksToBeFinished) {
        this.tasksToBeFinished = tasksToBeFinished;
    }
}
