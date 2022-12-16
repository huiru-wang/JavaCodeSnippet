package com.snippet.designpattern.template.menu;

import com.snippet.designpattern.template.CookTemplate;

public class KungPaoChicken extends CookTemplate {
    protected void prepareMaterials() {
        System.out.println("prepare chicken and pickled cucumber and carrot");
    }

    protected void cooking() {
        System.out.println("cooking KungPaoChicken");
    }

    protected void plating() {
        System.out.println("pour in a plate");
    }
}
