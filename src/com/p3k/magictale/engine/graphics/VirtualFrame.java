package com.p3k.magictale.engine.graphics;

/**
 * Created by artem96 on 16.01.17.
 */
public class VirtualFrame extends Frame{


    /**
     * Create virtual frame, that cannot contain
     * any renderable things
     *
     * @param spriteId - id in ResourceManager of sprite
     * @param length - how many ticks frame will be displayed
     */
    public VirtualFrame(int spriteId, int length) {

        super(spriteId, length);

    }



}
