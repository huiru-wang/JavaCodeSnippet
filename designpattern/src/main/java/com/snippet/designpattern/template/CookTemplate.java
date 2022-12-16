package com.snippet.designpattern.template;

public abstract class CookTemplate {
    public void cook() {
        this.prepareMaterials();
        this.cooking();
        this.plating();
    }

    protected abstract void prepareMaterials();

    protected abstract void cooking();

    protected abstract void plating();
}
