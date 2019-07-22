package com.ao1;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ReaderManagerTest {

    @Test
    public void fedWithTooMuchFoodACoupleOfTimes() throws InterruptedException {

        CountDownLatch latch = new CountDownLatch(5);

        DMCheckForDatFed dm = new DMCheckForDatFed(latch);
        new ItemsReaderManagerCsvDirectory1Thread(new File("src/test/resources"), dm, ()->{});

        latch.await(5, TimeUnit.SECONDS);

        Assert.assertEquals(0, latch.getCount());
    }

    @Test
    public void readFromDirectories() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(20);

        CountDownLatch finish = new CountDownLatch(1);

        new ItemReaderManagerCsvDirectories(new String[]{"src/test/resources/1", "src/test/resources/2"},
                new DMCheckForDatFed(latch), () -> finish.countDown());

        latch.await(10, TimeUnit.SECONDS);

        Assert.assertEquals(0, latch.getCount());

        finish.await(10, TimeUnit.SECONDS);

        Assert.assertEquals(0, finish.getCount());

    }

    private class DMCheckForDatFed implements ItemsDividerManager {
        private CountDownLatch latch;
        private int counter;

        public DMCheckForDatFed(CountDownLatch latch) {
            this.latch = latch;
            this.counter = 0;
        }

        @Override
        public int desiredDataChunk() {
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
