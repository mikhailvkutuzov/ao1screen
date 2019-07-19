package com.ao1.reader;

import com.ao1.data.Item;
import org.csveed.api.CsvClient;
import org.csveed.api.CsvClientImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;

public class ItemsReaderCsvFileWithHeaderByChunks implements ItemsReader {
    private static final Logger logger = LoggerFactory.getLogger(ItemsReaderCsvFileWithHeaderByChunks.class);
    private String csvFile;
    private BufferedReader input;
    private String header;
    private boolean active;
    private int chunk;

    public ItemsReaderCsvFileWithHeaderByChunks(File csvFile, int chunk) throws IOException {
        this.input = new BufferedReader(new InputStreamReader(new FileInputStream(csvFile)));
        this.header = this.input.readLine() + "\n";
        this.csvFile = csvFile.getAbsolutePath();
        this.active = true;
        this.chunk = chunk;
    }

    @Override
    public List<Item> read() throws NoMoreDataAvailable {
        if (!active) {
            throw new NoMoreDataAvailable();
        }
        try {
            StringBuffer buffer = new StringBuffer(100 * chunk);
            buffer.append(header);
            String data;
            for (int i = 0; i < chunk; i++) {
                if ((data = input.readLine()) != null) {
                    buffer.append(data).append('\n') ;
                } else {
                    active = false;
                    input.close();
                    break;
                }
            }

            logger.error("DATA TO BE PARSED {} " , buffer.toString());

            CsvClient<Item> reader = new CsvClientImpl<>(new StringReader(buffer.toString()), Item.class);
            return reader.readBeans();
        } catch (IOException e) {
            active = false;
            logger.error("there were problems to read {}", csvFile, e);
            try {
                input.close();
            } catch (IOException ex) {
                logger.error("there were problems to close {}", csvFile, ex);
            }
            throw new NoMoreDataAvailable();
        }
    }
}
