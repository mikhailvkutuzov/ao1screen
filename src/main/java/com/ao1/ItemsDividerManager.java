package com.ao1;

public interface ItemsDividerManager {

    /**
     * This amount should help {@link java.io.InputStreamReader} to feed {@link ItemsDividerManager} with a
     * reasonable chunk of data.
     * @return
     */
    int desiredDataChunk();

    /**
     *
     * @param data
     * @throws TooMuchFood
     */
    void feed(String data) throws TooMuchFood;

}
