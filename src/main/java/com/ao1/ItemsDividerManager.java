package com.ao1;

public interface ItemsDividerManager {

    /**
     * This amount should help {@link java.io.InputStreamReader} to feed {@link ItemsDividerManager} with a
     * reasonable chunk of data.
     * @return
     */
    int desiredDataChunk();

    void feed(String data) throws TooMuchFood;

    class TooMuchFood extends Exception{
        public int millisecondsToWait;

        public TooMuchFood(int millisecondsToWait) {
            this.millisecondsToWait = millisecondsToWait;
        }
    }

}
