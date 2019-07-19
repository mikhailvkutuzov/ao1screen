package com.ao1.reader;

public interface StringDataReader {

    String read() throws NoMoreDataAvailable;

    class NoMoreDataAvailable extends Exception{}

}
