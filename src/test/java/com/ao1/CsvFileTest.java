package com.ao1;

import com.ao1.reader.StringDataReader;
import com.ao1.reader.StringDataReaderCsvFileWithHeaderByChunks;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class CsvFileTest {

    @Test
    public void readByChunk() throws IOException, StringDataReader.NoMoreDataAvailable {
        StringDataReader reader = new StringDataReaderCsvFileWithHeaderByChunks(new File("src/test/resources/product.csv"), 2);

        String items = reader.read();

        Assert.assertEquals(2, countCsvItems(items));

        items = reader.read();

        Assert.assertEquals(2, countCsvItems(items));

        items = reader.read();

        Assert.assertEquals(1, countCsvItems(items));
    }

    private int countCsvItems(String data) {
        return data.split("\n").length - 1;
    }

    @Test(expected = StringDataReader.NoMoreDataAvailable.class)
    public void noMoreData() throws IOException, StringDataReader.NoMoreDataAvailable {
        StringDataReader reader = new StringDataReaderCsvFileWithHeaderByChunks(new File("src/test/resources/product.csv"), 2);
        reader.read();
        reader.read();
        reader.read();

        reader.read();
    }

}
