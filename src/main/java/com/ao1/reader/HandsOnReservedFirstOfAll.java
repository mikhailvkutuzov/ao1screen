package com.ao1.reader;

public class HandsOnReservedFirstOfAll extends StringDataReaderDelegate {
    private String reserved;

    public HandsOnReservedFirstOfAll(StringDataReader delegate) {
        super(delegate);
    }

    public void reserveForNextRead(String items) {
        this.reserved = items;
    }

    @Override
    public String read() throws NoMoreDataAvailable {
        if(reserved == null) {
            return super.read();
        } else {
            String data = reserved;
            reserved = null;
            return data;
        }
    }
}
