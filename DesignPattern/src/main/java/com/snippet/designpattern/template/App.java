package com.snippet.designpattern.template;

import com.snippet.designpattern.template.menu.InstantNoodles;
import com.snippet.designpattern.template.menu.KungPaoChicken;

public class App {

    public static void main(String[] args) {
        KungPaoChicken kungPaoChicken = new KungPaoChicken();
        kungPaoChicken.cook();
        InstantNoodles instantNoodles = new InstantNoodles();
        instantNoodles.cook();
    }
}
