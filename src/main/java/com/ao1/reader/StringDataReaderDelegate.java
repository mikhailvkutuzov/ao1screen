package com.ao1.reader;

public class StringDataReaderDelegate implements StringDataReader {
    private StringDataReader delegate;

    public StringDataReaderDelegate(StringDataReader delegate) {
        this.delegate = delegate;
    }

    @Override
    public String read() throws NoMoreDataAvailable {
        return delegate.read();
    }
}
