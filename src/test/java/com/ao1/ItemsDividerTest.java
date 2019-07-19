package com.ao1;

import com.ao1.data.ItemToBeRead;
import com.ao1.divider.ItemsDivider;
import com.ao1.divider.ItemsDividerByProductId;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ItemsDividerTest {

    @Test
    public void divide() {
        ItemsDivider divider = new ItemsDividerByProductId(3);

        List<ItemToBeRead> items = new ArrayList<>(5);

        for(int  i =0 ; i < 5; i++) {
            ItemToBeRead item = new ItemToBeRead();
            item.setCondition("condition" + i);
            item.setName("name" + i);
            item.setPrice(""+i);
            item.setProductId(i);
            item.setState("state" + i);
            items.add(item);
        }

        List<ItemToBeRead>[] result = divider.divide(items);

        Assert.assertEquals(result.length, 3);
        Assert.assertEquals(result[0].size(), 2);
        Assert.assertEquals(result[1].size(), 2);
        Assert.assertEquals(result[2].size(), 1);
    }

}
