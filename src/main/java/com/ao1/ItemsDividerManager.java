package com.ao1;

public interface ItemsDividerManager extends Manager {

    /**
     * This amount should help {@link java.io.InputStreamReader} to feed {@link ItemsDividerManager} with a
     * reasonable chunk of data.
     * @return
     */
    int desiredDataChunk();

    void feed(String data) throws TooMuchFood;

}
