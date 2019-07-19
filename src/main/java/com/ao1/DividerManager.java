package com.ao1;

import com.ao1.data.Item;

import java.util.List;

public interface DividerManager {

    int amountOfDividers();

    void feed(List<Item> data) throws TooMuchFood;

    class TooMuchFood extends Exception{
        public int millisecondsToWait;

        public TooMuchFood(int millisecondsToWait) {
            this.millisecondsToWait = millisecondsToWait;
        }
    }

}
