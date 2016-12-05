package com.p3k.magictale.engine.sound;

/**
 * Created by jorgen on 05.12.16.
 */

import com.p3k.magictale.engine.graphics.GameObject;

import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;

import java.nio.IntBuffer;
import java.util.NoSuchElementException;

/**
 * Represents a sound source:
 *  e.a. object which played sounds and
 *  can be moved.
 */
public class SoundSource {

    private IntBuffer buffer;
    private GameObject object;

    private String currentSoundName;

    /**
     * If true - that sound source is global
     * (like BGM music).
     */
    boolean isGlobal = false;

    /**
     * Creates a sound source.
     * @param obj game object whose generate sounds.
     * @param looped is playback again and again?
     */
    public SoundSource(GameObject obj, boolean looped) throws Exception {
        currentSoundName = "";
        buffer = BufferUtils.createIntBuffer(1);

        if ( obj != null ) {
            object = obj;
        } else {
            isGlobal = true;
        }

        AL10.alGenSources(buffer);
        if ( AL10.alGetError() != AL10.AL_NO_ERROR ) {
            throw new Exception("SoundSource constructor error: " +
                    "cannot generate source buffer (lack of memory)!");
        }

        // Concrete sound buffer will be attached when invoke play method

        if ( looped ) {
            AL10.alSourcei(buffer.get(0), AL10.AL_LOOPING, AL10.AL_TRUE);
        }

        AL10.alSourcef(buffer.get(0),   AL10.AL_PITCH,      1.0f);
        AL10.alSourcef(buffer.get(0),   AL10.AL_GAIN,       1.0f);

        if ( AL10.alGetError() != AL10.AL_NO_ERROR ) {
            throw new Exception("SoundSource constructor error: " +
                    "cannot set source properties");
        }
    }

    /**
     * Set gain level of source
     *
     * @param level positive float value (1.0f - normal value)
     * @return for chaining
     */
    public SoundSource setLevel(float level)
            throws IllegalArgumentException {
        if ( level < 0.0 ) {
            throw new IllegalArgumentException("level must be positive!");
        }

        AL10.alSourcef(buffer.get(0), AL10.AL_GAIN, level);

        return this;
    }

    public SoundSource play(String soundname) {

        if ( !currentSoundName.equals(soundname) ) {
            try {
                setSound(soundname);
            } catch (Exception e) {
                System.err.println("Cannot play sound "
                        + soundname + "! Error: " + e);
                e.printStackTrace();
                return this;
            }
        }

        // Update source position if not global
        if ( !isGlobal ) {
            AL10.alSource3f(buffer.get(0), AL10.AL_POSITION,
                    object.getX(), object.getY(), 0.0f);
        }


        AL10.alSourcePlay(buffer.get(0));

        return this;
    }

    public SoundSource pause() {
        AL10.alSourcePause(buffer.get(0));
        return this;
    }

    public  SoundSource stop() {
        AL10.alSourceStop(buffer.get(0));
        return this;
    }

    private void setSound(String soundname) throws Exception {
        IntBuffer sound = SoundManager.getInstance().getSound(soundname);

        if ( sound == null ) {
            throw new NoSuchElementException("No sound with name "
                    + soundname + " registered!");
        }

        AL10.alSourcei(buffer.get(0), AL10.AL_BUFFER, sound.get(0));

        if ( AL10.alGetError() != AL10.AL_NO_ERROR ) {
            throw new Exception("Cannot attach sound to source!");
        }

        currentSoundName = soundname;
    }
}
