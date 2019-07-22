package com.ao1;

import com.ao1.data.Item;
import com.ao1.divider.ItemsDividerByProductId;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class SortingApplicationTest {

    @Test
    public void stepByStepTest() throws InterruptedException, NoDataReady {

        CountDownLatch finish = new CountDownLatch(3);

        ItemsSorterManagerStopMergeGet sorter = new ItemsSorterManagerStopMergeGet(2, 2, 3, 4);

        ItemsDividerManagerUponExecutor divider = new ItemsDividerManagerUponExecutor(1, 5, 3,
                new ItemsDividerByProductId(sorter.conveyorsAmount()),
                sorter, () -> {
            finish.countDown();
            sorter.stop(() -> finish.countDown());
        });

        new ItemReaderManagerCsvDirectories(new String[]{"src/test/resources/1", "src/test/resources/2"}, divider,
                () -> {
                    finish.countDown();
                    divider.stop();
                });

        finish.await(10, TimeUnit.SECONDS);

        Assert.assertEquals(0, finish.getCount());

        List<Item> result = sorter.getSorted();

        Assert.assertEquals(3, result.size());

        List<Item> shouldBe = new ArrayList<>();
        shouldBe.add(new Item(1, "name1", "condition1", "sate1", new BigDecimal("1")));
        shouldBe.add(new Item(11, "name1", "condition1", "sate1", new BigDecimal("1.04")));
        shouldBe.add(new Item(1, "name1", "condition1", "sate1", new BigDecimal("1.2")));

        for (int i = 0; i < shouldBe.size(); i++) {
            System.out.println(result.get(i));
            Assert.assertEquals(shouldBe.get(i).getProductId(), result.get(i).getProductId());
            Assert.assertEquals(shouldBe.get(i).getDecimalPrice(), result.get(i).getDecimalPrice());
        }
    }

    @Test
    public void countSorterInput() throws InterruptedException, NoDataReady {

        CountDownLatch finish = new CountDownLatch(3);

        ItemsSorterManager sorter = new CountingItemsSorter(2);


        ItemsDividerManagerUponExecutor divider = new ItemsDividerManagerUponExecutor(1, 5, 3,
                new ItemsDividerByProductId(sorter.conveyorsAmount()),
                sorter, () -> {
            finish.countDown();
            sorter.stop(() -> finish.countDown());
        });

        new ItemReaderManagerCsvDirectories(new String[]{"src/test/resources/1", "src/test/resources/2"}, divider,
                () -> {
                    finish.countDown();
                    divider.stop();
                });

        finish.await(10, TimeUnit.SECONDS);

        Assert.assertEquals(0, finish.getCount());

        Assert.assertEquals(20, sorter.getSorted().size());

    }

    class CountingItemsSorter implements ItemsSorterManager {
        private int conveyorsAmount;
        private List<Item> keep;

        public CountingItemsSorter(int conveyorsAmount) {
            this.conveyorsAmount = conveyorsAmount;
            this.keep = new ArrayList<>();
        }

        @Override
        public int conveyorsAmount() {
            return conveyorsAmount;
        }

        @Override
        public synchronized void feed(List<Item>[] items) {
            for (List<Item> i : items) {
                keep.addAll(i);
            }
        }

        @Override
        public List<Item> getSorted() {
            return keep;
        }

        @Override
        public void stop(Runnable workDone) {
            workDone.run();
        }
    }


    @Test
    public void writeDownResults() throws InterruptedException {

        File report = new File("src/test/resources/results/report.csv");

        report.delete();

        new CsvSortingApplication()
                .sort(report,
                        new String[]{"src/test/resources/1", "src/test/resources/2"},
                        2,
                        1,
                        5,
                        2,
                        5);

        int i = 0;

        while (true) {
            if (report.exists()) {
                break;
            } else {
                System.out.println("there is no file");
                Thread.sleep(1000);
                if (i == 10) {
                    break;
                }
            }
            i++;
        }

    }


}
