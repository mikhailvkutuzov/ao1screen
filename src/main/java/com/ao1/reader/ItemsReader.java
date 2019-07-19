package com.ao1.reader;

import com.ao1.data.Item;

import java.util.List;

public interface ItemsReader {

    List<Item> read() throws NoMoreDataAvailable;

    class NoMoreDataAvailable extends Exception{}

}
