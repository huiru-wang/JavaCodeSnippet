package com.snippet.designpattern.template.menu;

import com.snippet.designpattern.template.CookTemplate;

public class InstantNoodles extends CookTemplate {
    protected void prepareMaterials() {
        System.out.println("unpack an Instant Noodles");
    }

    protected void cooking() {
        System.out.println("pour hot water waiting 10 mins");
    }

    protected void plating() {

    }
}
