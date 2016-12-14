package com.p3k.magictale.engine.gui;

/**
 * Created by jorgen on 14.12.16.
 */
public abstract class ComponentFactory {

    protected String RES_PATH = "";

    public abstract MButton createButton(String text, float x, float y);
    //public abstract MWindow createWindow();

    // TODO more components

}
