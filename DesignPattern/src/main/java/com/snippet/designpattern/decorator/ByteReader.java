package com.snippet.designpattern.decorator;

public class ByteReader implements Reader {
    @Override
    public void read(String filename) {
        System.out.println("read from" + filename + "as bytes");
    }
}
