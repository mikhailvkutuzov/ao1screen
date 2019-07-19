package com.ao1;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ReaderManagerTest {

    @Test
    public void justFedBuTooMuchFoodACoupleOfTimes() throws InterruptedException {

        CountDownLatch latch = new CountDownLatch(5);

        DMCheckForDatFed dm = new DMCheckForDatFed(latch);
        new ItemsReaderManager(new File("src/test/resources"), dm);

        latch.await(5, TimeUnit.SECONDS);

        Assert.assertEquals(0, latch.getCount());
    }


    private class DMCheckForDatFed implements ItemsDividerManager {
        private CountDownLatch latch;
        private int counter;

        public DMCheckForDatFed(CountDownLatch latch) {
            this.latch = latch;
            this.counter = 0;
        }

        @Override
        public int amountOfDividers() {
            return 3;
        }

        @Override
        public void feed(String data) throws TooMuchFood {
            if (counter != 1) {
                for (int i = 0; i < countCsvItems(data); i++) {
                    latch.countDown();
                }
                counter++;
            } else {
                counter++;
                throw new TooMuchFood(10);
            }
        }

        private int countCsvItems(String data) {
            return data.split("\n").length - 1;
        }

    }
}
