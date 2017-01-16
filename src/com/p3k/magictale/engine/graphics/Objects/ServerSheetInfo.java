package com.p3k.magictale.engine.graphics.Objects;

import com.p3k.magictale.engine.graphics.AnimationSheetResource;

/**
 * Contain information about spriteId of all sprites in
 * sprite sheet
 *
 * Created by artem96 on 16.01.17.
 */
public class ServerSheetInfo extends AnimationSheetResource {


    public ServerSheetInfo(int startOffset, int length) {
        setStartOffset(startOffset);
        setLength(length);


    }


}
