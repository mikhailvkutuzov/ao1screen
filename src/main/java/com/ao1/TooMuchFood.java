package com.ao1;

public class TooMuchFood extends Exception{
    public int millisecondsToWait;

    public TooMuchFood(int millisecondsToWait) {
        this.millisecondsToWait = millisecondsToWait;
    }
}
