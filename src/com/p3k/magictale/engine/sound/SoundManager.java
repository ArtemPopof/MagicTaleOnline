package com.p3k.magictale.engine.sound;

/**
 * Created by jorgen on 05.12.16.
 */

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.util.WaveData;

import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * SoundManager - global manager for all sounds
 * Loads sounds, stores it in map, access it...
 *
 * @version 1.0
 * @author popoff96@live.com
 */
public class SoundManager {

    private static SoundManager instance = null;

    private static final String SOUNDS_DIR = "/res/sounds/";

    private Map<String, IntBuffer> soundData;

    private SoundManager() throws Exception {

        try {
            AL.create();
        } catch (LWJGLException e) {
            e.printStackTrace();
            throw new Exception("SoundManager init error: " + e);
        } finally {
            AL10.alGetError();
        }

        soundData = new HashMap<>();
    }

    public static SoundManager getInstance() throws Exception {
        if ( instance == null ) {
            instance = new SoundManager();
        }

        return instance;
    }

    public void registerSound(String soundname) throws Exception {

        IntBuffer buf = BufferUtils.createIntBuffer(1);

        AL10.alGenBuffers(buf);
        if ( AL10.alGetError() != AL10.AL_NO_ERROR ) {
            throw new Exception("SoundManager registerSound error:" +
                    " cannot generate buffer (lack of memory)");
        }

        WaveData wavData = WaveData.create(SOUNDS_DIR + soundname);
        if ( wavData == null ) {
            throw new Exception("SoundManager registerSound error:" +
                    " error opening sound file " + soundname + "! Check this file!");
        }

        AL10.alBufferData(buf.get(0), wavData.format, wavData.data, wavData.samplerate);
        if ( AL10.alGetError() != AL10.AL_NO_ERROR ) {
            throw new Exception("SoundManager registerSound error:" +
                    " cannot fill buffer with data! Maybe sound is corrupted!");
        }

        wavData.dispose();

        soundData.put(soundname, buf);
    }

    public static void close() {
        // buffers free with GC

        AL.destroy();
    }
}
