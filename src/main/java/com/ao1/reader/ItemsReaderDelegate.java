package com.ao1.reader;

import com.ao1.data.Item;

import java.util.List;

public class ItemsReaderDelegate implements ItemsReader {
    private ItemsReader delegate;

    public ItemsReaderDelegate(ItemsReader delegate) {
        this.delegate = delegate;
    }

    @Override
    public List<Item> read() throws NoMoreDataAvailable {
        return delegate.read();
    }
}
