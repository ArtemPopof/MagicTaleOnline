package com.p3k.magictale.engine.graphics;

/**
 * Class contains all spriteId's for specific
 * spriteSheet.
 *
 * For usage on server side. For client see AnimationSheetResource.
 *
 * Created by artem96 on 16.01.17.
 */
public class AnimationInfo extends AnimationSheetResource {

    public AnimationInfo(int startOffset, int length) {

        setLength(length);
        setStartOffset(startOffset);

    }
}
