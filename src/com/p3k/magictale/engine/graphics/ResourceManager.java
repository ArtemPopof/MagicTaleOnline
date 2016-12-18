package com.p3k.magictale.engine.graphics;

import com.p3k.magictale.engine.Utils;
import com.p3k.magictale.game.Characters.CharacterTypes;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * ResourceManager class contains all game
 * resources and give interface to work with them
 *
 * Created by artem96 on 04.12.16.
 */
public class ResourceManager {

    /**
     * Map of textures,
     * first parm is key, it must be like
     * STONE_WITH_BLOOD, and second parameter is textureId
     * of texture, containing this resource;
     */
    private HashMap<Integer, Integer> textures;

    // all animations for all players.
    // each player's animations should
    // be loaded in memory when level loaded
    // or when game begins.
    private HashMap<CharacterTypes, ArrayList<Animation>> animations;

    private static ResourceManager instance = null;

    private ResourceManager() throws IllegalStateException {

        if (instance != null) {
            throw new IllegalStateException("Cannot init ResourceManager for the second time, bandit!");
        }

        textures = new HashMap<>();
        animations = new HashMap<>();

        loadAllAnimationsInformation();

        instance = this;

    }

    public static ResourceManager getInstance() throws Exception {
        if ( instance == null ) {
            instance = new ResourceManager();
        }

        return instance;
    }

    /**
     * Load all map textures from spritesheet
     * and map them according to the given
     * id.
     *
     * example: loadMapTextures(String myMapTextures = "/res/map1.mtf
     *                          , int firstId = 5000);
     *  will load all texutres from map1.mtf to memory,
     *  and it will be accessable with ID's from 5000 to
     *  5000 + numOfSpritesInTexture.
     *
     *  Can throw ill. arg. exc if id's alredy mapped by
     *  some other sprite sheet, you can contain all resources in one place
     *  maybe.
     *
     *  @throws IllegalArgumentException
     *
     */
    public void loadMapTextures(String spriteSheetPath, int firstId)
            throws IllegalArgumentException {

        //TODO implement

        SpriteSheet mapSS = new SpriteSheet(spriteSheetPath);
        System.out.println("Textures:");

        for (int i = 0; i < mapSS.size(); i++) {
            int textureId = mapSS.getSpriteTextureId(i);

            if (textures.put(firstId++, textureId) != null) {
                throw new IllegalArgumentException();
            }
//            System.out.println("id = " + (firstId-1) + "  sprId = " + textures.get(firstId-1));
        }
        System.out.println("firstId - mapSS.size - 1= " + (firstId - mapSS.size() - 1) + "  sprId = "
                + textures.get(firstId - mapSS.size() - 1));
        System.out.println("firstId - mapSS.size    = " + (firstId - mapSS.size()) + "  sprId = "
                + textures.get(firstId - mapSS.size()));
        System.out.println("firstId - 1             = " + (firstId-1) + "  sprId = " + textures.get(firstId-1));
        System.out.println("firstId                 = " + (firstId) + "  sprId = " + textures.get(firstId));
        System.out.println("MapSS.size              = " + (mapSS.size()));

    }

    /** @TODO
     * This function will be read file
     * that contains information about each
     * character animations and load this info
     * into Animations arrays.
     *
     * By now, this class is under development,
     * so it's do fake actions
     */
    private void loadAllAnimationsInformation() {

        ArrayList<Animation> characterTestAnims = new ArrayList<>();

        SpriteSheet charSprites = new SpriteSheet
                ("res/animation/goblin/goblin.png", 64, 64);

        Animation waitAnimation = new Animation(charSprites, 0, 11);

        characterTestAnims.add(waitAnimation);

        animations.put(CharacterTypes.ABSTRACT_CHARACTER, characterTestAnims);

        //next

        ArrayList<Animation> playerTestAnims = new ArrayList<>();

        Animation waitAnim = new Animation(charSprites, 0, 1);
        Animation rightMoveAnim = new Animation(charSprites, 11, 8);
        Animation leftMoveAnim = new Animation(charSprites, 33, 7);
        Animation upMoveAnim = new Animation(charSprites, 22, 7);
        Animation downMoveAnim =new Animation(charSprites, 0, 7);
        Animation deathAnim = new Animation(charSprites, 44, 5);

        Animation leftAttackAnim = new Animation(charSprites, 40, 3);
        Animation upAttackAnim = new Animation(charSprites, 29, 3);
        Animation rightAttackAnim = new Animation(charSprites, 18, 3);
        Animation downAttackAnim = new Animation(charSprites, 7, 3);

        playerTestAnims.add(waitAnim);
        playerTestAnims.add(rightMoveAnim);
        playerTestAnims.add(leftMoveAnim);
        playerTestAnims.add(upMoveAnim);
        playerTestAnims.add(downMoveAnim);
        playerTestAnims.add(deathAnim);

        playerTestAnims.add(leftAttackAnim);
        playerTestAnims.add(upAttackAnim);
        playerTestAnims.add(rightAttackAnim);
        playerTestAnims.add(downAttackAnim);


        //    playerTestAnims.add(deathAnim);

        animations.put(CharacterTypes.ABSTRACT_PLAYER, playerTestAnims);

        // For bots

        ArrayList<Animation> botTestAnims = new ArrayList<>();

        Animation botWaitAnim = new Animation(charSprites, 0, 1);
        Animation botRightMoveAnim = new Animation(charSprites, 11, 9);
        Animation botLeftMoveAnim = new Animation(charSprites, 33, 8);
        Animation botUpMoveAnim = new Animation(charSprites, 22, 8);
        Animation botDownMoveAnim = new Animation(charSprites, 0, 8);

        botTestAnims.add(botWaitAnim);
        botTestAnims.add(botRightMoveAnim);
        botTestAnims.add(botLeftMoveAnim);
        botTestAnims.add(botUpMoveAnim);
        botTestAnims.add(botDownMoveAnim);

        animations.put(CharacterTypes.ABSTRACT_BOT, botTestAnims);
    }

    /**
     * Returns textureId, or -1 if there are no such
     * texture
     *
     * @param textureName
     * @return textureId
     */
    public Integer getTexture(int textureName) {
        return textures.get(new Integer(textureName));
    }

    /**
     * Return character animation by id.
     *
     * @param character
     * @return animations
     */
    public ArrayList<Animation> getAnimations(GameCharacter character) {
        return animations.get(character.getCharacterId());
    }

    public Cursor loadCursor(String path) {
        BufferedImage img = null;

        try {
            img = Utils.loadImage(path);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        final int w = img.getWidth();
        final int h = img.getHeight();

        int rgbData[] = new int[w * h];

        for (int i = 0; i < rgbData.length; i++)
        {
            int x = i % w;
            int y = h - 1 - i / w; // this will also flip the image vertically

            rgbData[i] = img.getRGB(x, y);
        }

        IntBuffer buffer = BufferUtils.createIntBuffer(w * h);
        buffer.put(rgbData);
        buffer.rewind();

        Cursor cursor = null;
        try {
            cursor = new Cursor(w, h, 2, h - 2, 1, buffer, null);

            return cursor;
        } catch (LWJGLException e) {
            e.printStackTrace();
            return null;
        }


    }
}
