package com.ao1;

import com.ao1.data.Item;
import com.ao1.reader.ItemsReaderCsvFileWithHeaderByChunks;
import com.ao1.reader.ItemsReader;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class CsvFileTest {

    @Test
    public void readByChunk() throws IOException, ItemsReader.NoMoreDataAvailable {
        ItemsReader reader = new ItemsReaderCsvFileWithHeaderByChunks(new File("src/test/resources/product.csv"), 2);

        List<Item> items = reader.read();

        Assert.assertEquals(2, items.size());

        items = reader.read();

        Assert.assertEquals(2, items.size());

        items = reader.read();

        Assert.assertEquals(1, items.size());
    }


    @Test(expected = ItemsReader.NoMoreDataAvailable.class)
    public void noMoreData() throws IOException, ItemsReader.NoMoreDataAvailable {
        ItemsReader reader = new ItemsReaderCsvFileWithHeaderByChunks(new File("src/test/resources/product.csv"), 2);
        reader.read();
        reader.read();
        reader.read();

        reader.read();
    }

}
