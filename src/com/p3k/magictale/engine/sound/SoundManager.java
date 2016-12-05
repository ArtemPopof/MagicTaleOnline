package com.p3k.magictale.engine.sound;

/**
 * Created by jorgen on 05.12.16.
 */

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.util.WaveData;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.FloatBuffer;
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

    private static final String SOUNDS_DIR = "res/sounds/";

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

    public IntBuffer getSound(String soundname) {
        return soundData.get(soundname);
    }

    public void setListenerPos(float x, float y) throws Exception {
        FloatBuffer listPos = (FloatBuffer) BufferUtils.createFloatBuffer(3)
                .put(new float[] { x, y, 0.0f }).rewind();

        AL10.alListener(AL10.AL_POSITION, listPos);
        //TODO: set listener pos

        if ( AL10.alGetError() != AL10.AL_NO_ERROR ) {
            throw new Exception("SoundManager setListenerPos error:" +
                    " cannot update listener position!");
        }

    }

    public void registerSound(String soundname) throws Exception {

        IntBuffer buf = BufferUtils.createIntBuffer(1);

        AL10.alGenBuffers(buf);
        if ( AL10.alGetError() != AL10.AL_NO_ERROR ) {
            throw new Exception("SoundManager registerSound error:" +
                    " cannot generate buffer (lack of memory)");
        }

        WaveData wavData;
        try (BufferedInputStream fin = new BufferedInputStream(
                     new FileInputStream(SOUNDS_DIR + soundname))) {

            wavData = WaveData.create(fin);

            if ( wavData == null ) {
                throw new FileNotFoundException();
            }
        } catch (FileNotFoundException e) {
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

    public static void destroy() {
        // buffers free with GC

        AL.destroy();
    }
}
