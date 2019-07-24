package com.ao1;

import com.ao1.data.Item;
import com.ao1.sorter.ItemsSorter;
import com.ao1.sorter.ItemsSorterProductGroupsRestrictedAmountInsertsIntoSorted;
import com.ao1.sorter.ItemsSorterProductGroupsRestrictedAmountMergeSorted;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ItemsSorterTest {

    @Test
    public void test() {
        ItemsSorter sorter = new ItemsSorterProductGroupsRestrictedAmountInsertsIntoSorted(3, 10);

        List<Item> first = new ArrayList<>();
        first.add(new Item(1, "1", "a", "a", new BigDecimal("10.432")));//5
        first.add(new Item(1, "1", "a", "a", new BigDecimal("8.32")));//2
        first.add(new Item(2, "2", "b", "a", new BigDecimal("20")));//9
        first.add(new Item(1, "1", "a", "a", new BigDecimal("4.6")));//1
        first.add(new Item(5, "2", "b", "a", new BigDecimal("19")));//8
        first.add(new Item(3, "3", "c", "a", new BigDecimal("10.5")));//6
        first.add(new Item(2, "2", "c", "a", new BigDecimal("9.3")));//3
        first.add(new Item(3, "3", "a", "a", new BigDecimal("11.5")));//7
        first.add(new Item(2, "2", "c", "a", new BigDecimal("9.9")));//4
        first.add(new Item(1, "1", "a", "a", new BigDecimal("16.432")));//
        first.add(new Item(0, "0", "a", "a", new BigDecimal("0.5")));//0

        List<Item> sorted = sorter.sort(new ArrayList<>(first));

        Assert.assertEquals(10, sorted.size());

        List<Item> shouldBe = new ArrayList<>();
        shouldBe.add(first.get(10));
        shouldBe.add(first.get(3));
        shouldBe.add(first.get(1));
        shouldBe.add(first.get(6));
        shouldBe.add(first.get(8));
        shouldBe.add(first.get(0));
        shouldBe.add(first.get(5));
        shouldBe.add(first.get(7));
        shouldBe.add(first.get(4));
        shouldBe.add(first.get(2));

        for (int i = 0; i < shouldBe.size(); i++) {
            Assert.assertEquals("elements in  the " + i + "'th position are not equal ", shouldBe.get(i).getPrice(), sorted.get(i).getPrice());
            Assert.assertEquals("elements in  the " + i + "'th position are not equal ", shouldBe.get(i).getProductId(), sorted.get(i).getProductId());
            Assert.assertEquals("elements in  the " + i + "'th position are not equal ", shouldBe.get(i).getName(), sorted.get(i).getName());
        }

        first = new ArrayList<>();
        first.add(new Item(0, "0", "a", "a", new BigDecimal("11.5")));//
        first.add(new Item(0, "0", "a", "a", new BigDecimal("8.5"))); //
        first.add(new Item(0, "0", "a", "a", new BigDecimal("12.5")));
        first.add(new Item(4, "4", "c", "a", new BigDecimal("23.5")));
        first.add(new Item(4, "4", "a", "a", new BigDecimal("9.333")));//
        first.add(new Item(4, "4", "b", "a", new BigDecimal("15.565")));

        sorted = sorter.sort(new ArrayList<>(first));

        shouldBe = new ArrayList<>();
        shouldBe.add(new Item(0, "0", "a", "a", new BigDecimal("0.5")));
        shouldBe.add(new Item(1, "1", "a", "a", new BigDecimal("4.6")));
        shouldBe.add(new Item(1, "1", "a", "a", new BigDecimal("8.32")));
        shouldBe.add(new Item(0, "0", "a", "a", new BigDecimal("8.5")));
        shouldBe.add(new Item(2, "2", "c", "a", new BigDecimal("9.3")));
        shouldBe.add(new Item(4, "4", "a", "a", new BigDecimal("9.333")));
        shouldBe.add(new Item(2, "2", "c", "a", new BigDecimal("9.9")));
        shouldBe.add(new Item(1, "1", "a", "a", new BigDecimal("10.432")));
        shouldBe.add(new Item(3, "3", "c", "a", new BigDecimal("10.5")));
        shouldBe.add(new Item(0, "0", "a", "a", new BigDecimal("11.5")));

        for (int i = 0; i < shouldBe.size(); i++) {
            Assert.assertEquals("elements in  the " + i + "'th position are not equal ", shouldBe.get(i).getPrice(), sorted.get(i).getPrice());
            Assert.assertEquals("elements in  the " + i + "'th position are not equal ", shouldBe.get(i).getProductId(), sorted.get(i).getProductId());
            Assert.assertEquals("elements in  the " + i + "'th position are not equal ", shouldBe.get(i).getName(), sorted.get(i).getName());
        }

        first = new ArrayList<>();

        first.add(new Item(5, "5", "b", "a", new BigDecimal("17.465")));
        first.add(new Item(5, "5", "b", "a", new BigDecimal("5.565")));
        first.add(new Item(4, "4", "b", "a", new BigDecimal("15.565")));
        first.add(new Item(5, "5", "b", "a", new BigDecimal("22.37")));
        first.add(new Item(6, "6", "b", "a", new BigDecimal("14.37")));
        first.add(new Item(6, "6", "b", "a", new BigDecimal("17.37")));
        first.add(new Item(6, "6", "b", "a", new BigDecimal("6.37")));

        sorted = sorter.sort(new ArrayList<>(first));

        shouldBe = new ArrayList<>();
        shouldBe.add(new Item(0, "0", "a", "a", new BigDecimal("0.5")));
        shouldBe.add(new Item(1, "1", "a", "a", new BigDecimal("4.6")));
        shouldBe.add(new Item(5, "5", "b", "a", new BigDecimal("5.565")));
        shouldBe.add(new Item(6, "6", "b", "a", new BigDecimal("6.37")));
        shouldBe.add(new Item(1, "1", "a", "a", new BigDecimal("8.32")));
        shouldBe.add(new Item(0, "0", "a", "a", new BigDecimal("8.5")));
        shouldBe.add(new Item(2, "2", "c", "a", new BigDecimal("9.3")));
        shouldBe.add(new Item(4, "4", "a", "a", new BigDecimal("9.333")));
        shouldBe.add(new Item(2, "2", "c", "a", new BigDecimal("9.9")));
        shouldBe.add(new Item(1, "1", "a", "a", new BigDecimal("10.432")));

        for (int i = 0; i < shouldBe.size(); i++) {
            Assert.assertEquals("elements in  the " + i + "'th position are not equal ", shouldBe.get(i).getPrice(), sorted.get(i).getPrice());
            Assert.assertEquals("elements in  the " + i + "'th position are not equal ", shouldBe.get(i).getProductId(), sorted.get(i).getProductId());
            Assert.assertEquals("elements in  the " + i + "'th position are not equal ", shouldBe.get(i).getName(), sorted.get(i).getName());
        }
    }


    @Test
    public void limits() {
        ItemsSorter sorter = new ItemsSorterProductGroupsRestrictedAmountInsertsIntoSorted(3, 10);
        Assert.assertEquals(0, sorter.sort(new ArrayList<>()).size());

        List<Item> first = new ArrayList<>();

        first.add(new Item(6, "6", "b", "a", new BigDecimal("14.37")));
        first.add(new Item(6, "6", "b", "a", new BigDecimal("17.37")));
        first.add(new Item(6, "6", "b", "a", new BigDecimal("6.5")));
        first.add(new Item(6, "6", "b", "a", new BigDecimal("6.37")));


        List<Item> sorted = sorter.sort(new ArrayList<>(first));

        List<Item> shouldBe = new ArrayList<>();
        shouldBe.add(new Item(6, "6", "b", "a", new BigDecimal("6.37")));
        shouldBe.add(new Item(6, "6", "b", "a", new BigDecimal("6.5")));
        shouldBe.add(new Item(6, "6", "b", "a", new BigDecimal("14.37")));

        Assert.assertEquals(shouldBe.size(), sorted.size());

        for (int i = 0; i < shouldBe.size(); i++) {
            Assert.assertEquals("elements in  the " + i + "'th position are not equal ", shouldBe.get(i).getPrice(), sorted.get(i).getPrice());
            Assert.assertEquals("elements in  the " + i + "'th position are not equal ", shouldBe.get(i).getProductId(), sorted.get(i).getProductId());
            Assert.assertEquals("elements in  the " + i + "'th position are not equal ", shouldBe.get(i).getName(), sorted.get(i).getName());
        }

        sorter.sort(new ArrayList<>(first));

        shouldBe = new ArrayList<>();
        shouldBe.add(new Item(6, "6", "b", "a", new BigDecimal("6.37")));
        shouldBe.add(new Item(6, "6", "b", "a", new BigDecimal("6.37")));
        shouldBe.add(new Item(6, "6", "b", "a", new BigDecimal("6.5")));

        Assert.assertEquals(shouldBe.size(), sorted.size());

        for (int i = 0; i < shouldBe.size(); i++) {
            Assert.assertEquals("elements in  the " + i + "'th position are not equal ", shouldBe.get(i).getPrice(), sorted.get(i).getPrice());
            Assert.assertEquals("elements in  the " + i + "'th position are not equal ", shouldBe.get(i).getProductId(), sorted.get(i).getProductId());
            Assert.assertEquals("elements in  the " + i + "'th position are not equal ", shouldBe.get(i).getName(), sorted.get(i).getName());
        }

    }



    @Test
    public void testMerge() {
        ItemsSorter sorter = new ItemsSorterProductGroupsRestrictedAmountMergeSorted(3, 10);

        List<Item> first = new ArrayList<>();
        first.add(new Item(1, "1", "a", "a", new BigDecimal("10.432")));//5
        first.add(new Item(1, "1", "a", "a", new BigDecimal("8.32")));//2
        first.add(new Item(2, "2", "b", "a", new BigDecimal("20")));//9
        first.add(new Item(1, "1", "a", "a", new BigDecimal("4.6")));//1
        first.add(new Item(5, "2", "b", "a", new BigDecimal("19")));//8
        first.add(new Item(3, "3", "c", "a", new BigDecimal("10.5")));//6
        first.add(new Item(2, "2", "c", "a", new BigDecimal("9.3")));//3
        first.add(new Item(3, "3", "a", "a", new BigDecimal("11.5")));//7
        first.add(new Item(2, "2", "c", "a", new BigDecimal("9.9")));//4
        first.add(new Item(1, "1", "a", "a", new BigDecimal("16.432")));//
        first.add(new Item(0, "0", "a", "a", new BigDecimal("0.5")));//0

        List<Item> sorted = sorter.sort(new ArrayList<>(first));

        Assert.assertEquals(10, sorted.size());

        List<Item> shouldBe = new ArrayList<>();
        shouldBe.add(first.get(10));
        shouldBe.add(first.get(3));
        shouldBe.add(first.get(1));
        shouldBe.add(first.get(6));
        shouldBe.add(first.get(8));
        shouldBe.add(first.get(0));
        shouldBe.add(first.get(5));
        shouldBe.add(first.get(7));
        shouldBe.add(first.get(4));
        shouldBe.add(first.get(2));

        for (int i = 0; i < shouldBe.size(); i++) {
            Assert.assertEquals("elements in  the " + i + "'th position are not equal ", shouldBe.get(i).getPrice(), sorted.get(i).getPrice());
            Assert.assertEquals("elements in  the " + i + "'th position are not equal ", shouldBe.get(i).getProductId(), sorted.get(i).getProductId());
            Assert.assertEquals("elements in  the " + i + "'th position are not equal ", shouldBe.get(i).getName(), sorted.get(i).getName());
        }

        first = new ArrayList<>();
        first.add(new Item(0, "0", "a", "a", new BigDecimal("11.5")));//
        first.add(new Item(0, "0", "a", "a", new BigDecimal("8.5"))); //
        first.add(new Item(0, "0", "a", "a", new BigDecimal("12.5")));
        first.add(new Item(4, "4", "c", "a", new BigDecimal("23.5")));
        first.add(new Item(4, "4", "a", "a", new BigDecimal("9.333")));//
        first.add(new Item(4, "4", "b", "a", new BigDecimal("15.565")));

        sorted = sorter.sort(new ArrayList<>(first));

        shouldBe = new ArrayList<>();
        shouldBe.add(new Item(0, "0", "a", "a", new BigDecimal("0.5")));
        shouldBe.add(new Item(1, "1", "a", "a", new BigDecimal("4.6")));
        shouldBe.add(new Item(1, "1", "a", "a", new BigDecimal("8.32")));
        shouldBe.add(new Item(0, "0", "a", "a", new BigDecimal("8.5")));
        shouldBe.add(new Item(2, "2", "c", "a", new BigDecimal("9.3")));
        shouldBe.add(new Item(4, "4", "a", "a", new BigDecimal("9.333")));
        shouldBe.add(new Item(2, "2", "c", "a", new BigDecimal("9.9")));
        shouldBe.add(new Item(1, "1", "a", "a", new BigDecimal("10.432")));
        shouldBe.add(new Item(3, "3", "c", "a", new BigDecimal("10.5")));
        shouldBe.add(new Item(0, "0", "a", "a", new BigDecimal("11.5")));

        for (int i = 0; i < shouldBe.size(); i++) {
            Assert.assertEquals("elements in  the " + i + "'th position are not equal ", shouldBe.get(i).getPrice(), sorted.get(i).getPrice());
            Assert.assertEquals("elements in  the " + i + "'th position are not equal ", shouldBe.get(i).getProductId(), sorted.get(i).getProductId());
            Assert.assertEquals("elements in  the " + i + "'th position are not equal ", shouldBe.get(i).getName(), sorted.get(i).getName());
        }

        first = new ArrayList<>();

        first.add(new Item(5, "5", "b", "a", new BigDecimal("17.465")));
        first.add(new Item(5, "5", "b", "a", new BigDecimal("5.565")));
        first.add(new Item(4, "4", "b", "a", new BigDecimal("15.565")));
        first.add(new Item(5, "5", "b", "a", new BigDecimal("22.37")));
        first.add(new Item(6, "6", "b", "a", new BigDecimal("14.37")));
        first.add(new Item(6, "6", "b", "a", new BigDecimal("17.37")));
        first.add(new Item(6, "6", "b", "a", new BigDecimal("6.37")));

        sorted = sorter.sort(new ArrayList<>(first));

        shouldBe = new ArrayList<>();
        shouldBe.add(new Item(0, "0", "a", "a", new BigDecimal("0.5")));
        shouldBe.add(new Item(1, "1", "a", "a", new BigDecimal("4.6")));
        shouldBe.add(new Item(5, "5", "b", "a", new BigDecimal("5.565")));
        shouldBe.add(new Item(6, "6", "b", "a", new BigDecimal("6.37")));
        shouldBe.add(new Item(1, "1", "a", "a", new BigDecimal("8.32")));
        shouldBe.add(new Item(0, "0", "a", "a", new BigDecimal("8.5")));
        shouldBe.add(new Item(2, "2", "c", "a", new BigDecimal("9.3")));
        shouldBe.add(new Item(4, "4", "a", "a", new BigDecimal("9.333")));
        shouldBe.add(new Item(2, "2", "c", "a", new BigDecimal("9.9")));
        shouldBe.add(new Item(1, "1", "a", "a", new BigDecimal("10.432")));

        for (int i = 0; i < shouldBe.size(); i++) {
            Assert.assertEquals("elements in  the " + i + "'th position are not equal ", shouldBe.get(i).getPrice(), sorted.get(i).getPrice());
            Assert.assertEquals("elements in  the " + i + "'th position are not equal ", shouldBe.get(i).getProductId(), sorted.get(i).getProductId());
            Assert.assertEquals("elements in  the " + i + "'th position are not equal ", shouldBe.get(i).getName(), sorted.get(i).getName());
        }
    }


    @Test
    public void limitsMerge() {
        ItemsSorter sorter = new ItemsSorterProductGroupsRestrictedAmountMergeSorted(3, 10);
        Assert.assertEquals(0, sorter.sort(new ArrayList<>()).size());

        List<Item> first = new ArrayList<>();

        first.add(new Item(6, "6", "b", "a", new BigDecimal("14.37")));
        first.add(new Item(6, "6", "b", "a", new BigDecimal("17.37")));
        first.add(new Item(6, "6", "b", "a", new BigDecimal("6.5")));
        first.add(new Item(6, "6", "b", "a", new BigDecimal("6.37")));


        List<Item> sorted = sorter.sort(new ArrayList<>(first));

        List<Item> shouldBe = new ArrayList<>();
        shouldBe.add(new Item(6, "6", "b", "a", new BigDecimal("6.37")));
        shouldBe.add(new Item(6, "6", "b", "a", new BigDecimal("6.5")));
        shouldBe.add(new Item(6, "6", "b", "a", new BigDecimal("14.37")));

        Assert.assertEquals(shouldBe.size(), sorted.size());

        for (int i = 0; i < shouldBe.size(); i++) {
            Assert.assertEquals("elements in  the " + i + "'th position are not equal ", shouldBe.get(i).getPrice(), sorted.get(i).getPrice());
            Assert.assertEquals("elements in  the " + i + "'th position are not equal ", shouldBe.get(i).getProductId(), sorted.get(i).getProductId());
            Assert.assertEquals("elements in  the " + i + "'th position are not equal ", shouldBe.get(i).getName(), sorted.get(i).getName());
        }

        sorter.sort(new ArrayList<>(first));

        shouldBe = new ArrayList<>();
        shouldBe.add(new Item(6, "6", "b", "a", new BigDecimal("6.37")));
        shouldBe.add(new Item(6, "6", "b", "a", new BigDecimal("6.37")));
        shouldBe.add(new Item(6, "6", "b", "a", new BigDecimal("6.5")));

        Assert.assertEquals(shouldBe.size(), sorted.size());

        for (int i = 0; i < shouldBe.size(); i++) {
            Assert.assertEquals("elements in  the " + i + "'th position are not equal ", shouldBe.get(i).getPrice(), sorted.get(i).getPrice());
            Assert.assertEquals("elements in  the " + i + "'th position are not equal ", shouldBe.get(i).getProductId(), sorted.get(i).getProductId());
            Assert.assertEquals("elements in  the " + i + "'th position are not equal ", shouldBe.get(i).getName(), sorted.get(i).getName());
        }

    }




}
