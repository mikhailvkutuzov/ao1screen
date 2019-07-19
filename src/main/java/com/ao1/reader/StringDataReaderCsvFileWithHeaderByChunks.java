package com.ao1.reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class StringDataReaderCsvFileWithHeaderByChunks implements StringDataReader {
    private static final Logger logger = LoggerFactory.getLogger(StringDataReaderCsvFileWithHeaderByChunks.class);
    private String csvFile;
    private BufferedReader input;
    private String header;
    private boolean active;
    private int chunk;

    public StringDataReaderCsvFileWithHeaderByChunks(File csvFile, int chunk) throws IOException {
        this.input = new BufferedReader(new InputStreamReader(new FileInputStream(csvFile)));
        this.header = this.input.readLine() + "\n";
        this.csvFile = csvFile.getAbsolutePath();
        this.active = true;
        this.chunk = chunk;
    }

    @Override
    public String read() throws NoMoreDataAvailable {
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

            logger.debug("DATA TO BE HANDED ON {} " , buffer.toString());
            return buffer.toString();
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
