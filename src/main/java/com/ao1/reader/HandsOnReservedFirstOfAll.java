package com.ao1.reader;

import com.ao1.data.Item;

import java.util.List;

public class HandsOnReservedFirstOfAll extends ItemsReaderDelegate {
    private List<Item> reserved;

    public HandsOnReservedFirstOfAll(ItemsReader delegate) {
        super(delegate);
    }

    public void reserveForNextRead(List<Item> items) {
        this.reserved = items;
    }

    @Override
    public List<Item> read() throws NoMoreDataAvailable {
        if(reserved == null) {
            return super.read();
        } else {
            List<Item> data = reserved;
            reserved = null;
            return data;
        }
    }
}
