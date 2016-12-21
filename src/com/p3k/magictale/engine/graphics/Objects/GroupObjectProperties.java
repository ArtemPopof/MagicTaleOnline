package com.p3k.magictale.engine.graphics.Objects;



/**
 * Created by COMar-PC on 19.12.2016.
 *
 * When you want to add new attributes to class.
 * Don't forget add "enum" that name equals string name in .tmx file
 */
enum GroupObjectAttrs { DESTR, MOVE, ITEM }

public class GroupObjectProperties {
    private boolean isDestructable = false;
    private boolean isMovable = false;
    private boolean isItem = false;

    public GroupObjectProperties(){
    }

    public GroupObjectProperties(boolean isDestructable) {
        this.isDestructable = isDestructable;
    }

    public GroupObjectProperties(boolean isDestructable, boolean isMovable, boolean isItem) {
        this.isDestructable = isDestructable;
        this.isMovable = isMovable;
        this.isItem = isItem;
    }

    public boolean isDestructable() {
        return isDestructable;
    }

    public void setDestructable(boolean destructable) {
        isDestructable = destructable;
    }

    public boolean isMovable() {
        return isMovable;
    }

    public void setMovable(boolean movable) {
        isMovable = movable;
    }

    public boolean isItem() {
        return isItem;
    }

    public void setItem(boolean item) {
        isItem = item;
    }
}
