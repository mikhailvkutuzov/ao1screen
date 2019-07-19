package com.ao1;

public interface ItemsDividerManager {

    int amountOfDividers();

    void feed(String data) throws TooMuchFood;

    class TooMuchFood extends Exception{
        public int millisecondsToWait;

        public TooMuchFood(int millisecondsToWait) {
            this.millisecondsToWait = millisecondsToWait;
        }
    }

}
