package com.ao1;

import com.ao1.data.Item;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.List;
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


    private class DMCheckForDatFed implements DividerManager {
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
        public void feed(List<Item> data) throws TooMuchFood {
            if (counter != 1) {
                for (int i = 0; i < data.size(); i++) {
                    latch.countDown();
                }
                counter++;
            } else {
                counter++;
                throw new TooMuchFood(10);
            }
        }
    }
}
