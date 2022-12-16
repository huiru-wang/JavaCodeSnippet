package com.snippet.designpattern.decorator;

public class StringReader implements Reader {

    private ByteReader byteReader;

    public StringReader(ByteReader byteReader) {
        this.byteReader = byteReader;
    }

    @Override
    public void read(String filename) {
        byteReader.read(filename);
        System.out.println("transfer bytes to string.");
    }
}
