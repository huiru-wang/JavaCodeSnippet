package com.snippet.designpattern.decorator;

public class App {
    public static void main(String[] args) {
        ByteReader byteReader = new ByteReader();
        byteReader.read("test.yml");

        System.out.println("----------------------------------");

        StringReader stringReader = new StringReader(byteReader);
        stringReader.read("test.yml");
    }
}
