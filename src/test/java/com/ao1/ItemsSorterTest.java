package com.ao1;

import com.ao1.data.ItemToBeRead;
import com.ao1.data.ItemToBeSorted;
import com.ao1.sorter.ItemsSorter;
import com.ao1.sorter.ItemsSorterWithProductGroupsAndHoleAmountRestrictedUsingPresortedData;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ItemsSorterTest {

    @Test
    public void test() {
        ItemsSorter sorter = new ItemsSorterWithProductGroupsAndHoleAmountRestrictedUsingPresortedData(3, 10);

        List<ItemToBeRead> first = new ArrayList<>();
        first.add(new ItemToBeRead(1, "1", "a", "a", "10.432"));//5
        first.add(new ItemToBeRead(1, "1", "a", "a", "8.32"));//2
        first.add(new ItemToBeRead(2, "2", "b", "a", "20"));//9
        first.add(new ItemToBeRead(1, "1", "a", "a", "4.6"));//1
        first.add(new ItemToBeRead(5, "2", "b", "a", "19"));//8
        first.add(new ItemToBeRead(3, "3", "c", "a", "10.5"));//6
        first.add(new ItemToBeRead(2, "2", "c", "a", "9.3"));//3
        first.add(new ItemToBeRead(3, "3", "a", "a", "11.5"));//7
        first.add(new ItemToBeRead(2, "2", "c", "a", "9.9"));//4
        first.add(new ItemToBeRead(1, "1", "a", "a", "16.432"));//
        first.add(new ItemToBeRead(0, "0", "a", "a", "0.5"));//0

        List<ItemToBeSorted> sorted = sorter.sort(first);

        Assert.assertEquals(10, sorted.size());

        List<ItemToBeSorted> shouldBe = new ArrayList<>();
        shouldBe.add(new ItemToBeSorted(first.get(10)));
        shouldBe.add(new ItemToBeSorted(first.get(3)));
        shouldBe.add(new ItemToBeSorted(first.get(1)));
        shouldBe.add(new ItemToBeSorted(first.get(6)));
        shouldBe.add(new ItemToBeSorted(first.get(8)));
        shouldBe.add(new ItemToBeSorted(first.get(0)));
        shouldBe.add(new ItemToBeSorted(first.get(5)));
        shouldBe.add(new ItemToBeSorted(first.get(7)));
        shouldBe.add(new ItemToBeSorted(first.get(4)));
        shouldBe.add(new ItemToBeSorted(first.get(2)));

        for (int i = 0; i < shouldBe.size(); i++) {
            Assert.assertEquals("elements in  the " + i + "'th position are not equal ", shouldBe.get(i).getPrice(), sorted.get(i).getPrice());
            Assert.assertEquals("elements in  the " + i + "'th position are not equal ", shouldBe.get(i).getProductId(), sorted.get(i).getProductId());
            Assert.assertEquals("elements in  the " + i + "'th position are not equal ", shouldBe.get(i).getName(), sorted.get(i).getName());
        }

        first = new ArrayList<>();
        first.add(new ItemToBeRead(0, "0", "a", "a", "11.5"));//
        first.add(new ItemToBeRead(0, "0", "a", "a", "8.5")); //
        first.add(new ItemToBeRead(0, "0", "a", "a", "12.5"));
        first.add(new ItemToBeRead(4, "4", "c", "a", "23.5"));
        first.add(new ItemToBeRead(4, "4", "a", "a", "9.333"));//
        first.add(new ItemToBeRead(4, "4", "b", "a", "15.565"));

        sorted = sorter.sort(first);

        shouldBe = new ArrayList<>();
        shouldBe.add(new ItemToBeSorted(new ItemToBeRead(0, "0", "a", "a", "0.5")));
        shouldBe.add(new ItemToBeSorted(new ItemToBeRead(1, "1", "a", "a", "4.6")));
        shouldBe.add(new ItemToBeSorted(new ItemToBeRead(1, "1", "a", "a", "8.32")));
        shouldBe.add(new ItemToBeSorted(new ItemToBeRead(0, "0", "a", "a", "8.5")));
        shouldBe.add(new ItemToBeSorted(new ItemToBeRead(2, "2", "c", "a", "9.3")));
        shouldBe.add(new ItemToBeSorted(new ItemToBeRead(4, "4", "a", "a", "9.333")));
        shouldBe.add(new ItemToBeSorted(new ItemToBeRead(2, "2", "c", "a", "9.9")));
        shouldBe.add(new ItemToBeSorted(new ItemToBeRead(1, "1", "a", "a", "10.432")));
        shouldBe.add(new ItemToBeSorted(new ItemToBeRead(3, "3", "c", "a", "10.5")));
        shouldBe.add(new ItemToBeSorted(new ItemToBeRead(0, "0", "a", "a", "11.5")));

        for (int i = 0; i < shouldBe.size(); i++) {
            Assert.assertEquals("elements in  the " + i + "'th position are not equal ", shouldBe.get(i).getPrice(), sorted.get(i).getPrice());
            Assert.assertEquals("elements in  the " + i + "'th position are not equal ", shouldBe.get(i).getProductId(), sorted.get(i).getProductId());
            Assert.assertEquals("elements in  the " + i + "'th position are not equal ", shouldBe.get(i).getName(), sorted.get(i).getName());
        }

        first = new ArrayList<>();

        first.add(new ItemToBeRead(5, "5", "b", "a", "17.465"));
        first.add(new ItemToBeRead(5, "5", "b", "a", "5.565"));
        first.add(new ItemToBeRead(4, "4", "b", "a", "15.565"));
        first.add(new ItemToBeRead(5, "5", "b", "a", "22.37"));
        first.add(new ItemToBeRead(6, "6", "b", "a", "14.37"));
        first.add(new ItemToBeRead(6, "6", "b", "a", "17.37"));
        first.add(new ItemToBeRead(6, "6", "b", "a", "6.37"));

        sorted = sorter.sort(first);

        shouldBe = new ArrayList<>();
        shouldBe.add(new ItemToBeSorted(new ItemToBeRead(0, "0", "a", "a", "0.5")));
        shouldBe.add(new ItemToBeSorted(new ItemToBeRead(1, "1", "a", "a", "4.6")));
        shouldBe.add(new ItemToBeSorted(new ItemToBeRead(5, "5", "b", "a", "5.565")));
        shouldBe.add(new ItemToBeSorted(new ItemToBeRead(6, "6", "b", "a", "6.37")));
        shouldBe.add(new ItemToBeSorted(new ItemToBeRead(1, "1", "a", "a", "8.32")));
        shouldBe.add(new ItemToBeSorted(new ItemToBeRead(0, "0", "a", "a", "8.5")));
        shouldBe.add(new ItemToBeSorted(new ItemToBeRead(2, "2", "c", "a", "9.3")));
        shouldBe.add(new ItemToBeSorted(new ItemToBeRead(4, "4", "a", "a", "9.333")));
        shouldBe.add(new ItemToBeSorted(new ItemToBeRead(2, "2", "c", "a", "9.9")));
        shouldBe.add(new ItemToBeSorted(new ItemToBeRead(1, "1", "a", "a", "10.432")));

        for (int i = 0; i < shouldBe.size(); i++) {
            Assert.assertEquals("elements in  the " + i + "'th position are not equal ", shouldBe.get(i).getPrice(), sorted.get(i).getPrice());
            Assert.assertEquals("elements in  the " + i + "'th position are not equal ", shouldBe.get(i).getProductId(), sorted.get(i).getProductId());
            Assert.assertEquals("elements in  the " + i + "'th position are not equal ", shouldBe.get(i).getName(), sorted.get(i).getName());
        }
    }


    @Test
    public void limits() {
        ItemsSorter sorter = new ItemsSorterWithProductGroupsAndHoleAmountRestrictedUsingPresortedData(3, 10);
        Assert.assertEquals(0, sorter.sort(new ArrayList<>()).size());

        List<ItemToBeRead> first = new ArrayList<>();

        first.add(new ItemToBeRead(6, "6", "b", "a", "14.37"));
        first.add(new ItemToBeRead(6, "6", "b", "a", "17.37"));
        first.add(new ItemToBeRead(6, "6", "b", "a", "6.37"));
        first.add(new ItemToBeRead(6, "6", "b", "a", "6.5"));


        List<ItemToBeSorted> sorted = sorter.sort(first);

        List<ItemToBeSorted> shouldBe = new ArrayList<>();
        shouldBe.add(new ItemToBeSorted(new ItemToBeRead(6, "6", "b", "a", "6.5")));
        shouldBe.add(new ItemToBeSorted(new ItemToBeRead(6, "6", "b", "a", "6.37")));
        shouldBe.add(new ItemToBeSorted(new ItemToBeRead(6, "6", "b", "a", "14.37")));

        Assert.assertEquals(shouldBe.size(), first.size());

        for (int i = 0; i < shouldBe.size(); i++) {
            Assert.assertEquals("elements in  the " + i + "'th position are not equal ", shouldBe.get(i).getPrice(), sorted.get(i).getPrice());
            Assert.assertEquals("elements in  the " + i + "'th position are not equal ", shouldBe.get(i).getProductId(), sorted.get(i).getProductId());
            Assert.assertEquals("elements in  the " + i + "'th position are not equal ", shouldBe.get(i).getName(), sorted.get(i).getName());
        }

        sorter.sort(first);

        shouldBe = new ArrayList<>();
        shouldBe.add(new ItemToBeSorted(new ItemToBeRead(6, "6", "b", "a", "6.5")));
        shouldBe.add(new ItemToBeSorted(new ItemToBeRead(6, "6", "b", "a", "6.5")));
        shouldBe.add(new ItemToBeSorted(new ItemToBeRead(6, "6", "b", "a", "6.37")));

        Assert.assertEquals(shouldBe.size(), first.size());

        for (int i = 0; i < shouldBe.size(); i++) {
            Assert.assertEquals("elements in  the " + i + "'th position are not equal ", shouldBe.get(i).getPrice(), sorted.get(i).getPrice());
            Assert.assertEquals("elements in  the " + i + "'th position are not equal ", shouldBe.get(i).getProductId(), sorted.get(i).getProductId());
            Assert.assertEquals("elements in  the " + i + "'th position are not equal ", shouldBe.get(i).getName(), sorted.get(i).getName());
        }

    }

}
