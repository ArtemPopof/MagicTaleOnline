package com.p3k.magictale.engine.graphics;

import com.p3k.magictale.engine.Utils;
import com.p3k.magictale.game.Characters.Bat;
import com.p3k.magictale.engine.graphics.Objects.ObjectSheetResource;
import com.p3k.magictale.game.Characters.CharacterTypes;
import com.sun.corba.se.spi.activation.Server;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

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
//    private HashMap<Integer, Integer> textures;
    private HashMap<Integer, Sprite> textures;

    // all animations for all players.
    // each player's animations should
    // be loaded in memory when level loaded
    // or when game begins.
    private HashMap<CharacterTypes, ArrayList<Animation>> animations;

    /**
     * Server animations (without sprites)
     */
    private HashMap<CharacterTypes, ArrayList<ServerAnimation>> serverAnimations;

    private static ResourceManager instance = null;

    /**
     * where animation textures will be mapped
     */
    private static final int ANIMATION_TEXTURES_POOL_ID = 3000;

    private static final String ANIMATION_PATH = "res/animation/animations.mta";

    private int animationPoolFreeIndex;

    protected ResourceManager(boolean isClientSideCall) {

        textures = new HashMap<>();
        animations = new HashMap<>();

        animationPoolFreeIndex = ANIMATION_TEXTURES_POOL_ID;

        loadAllAnimationsInformation(isClientSideCall);

        instance = this;

    }

    public static ResourceManager getInstance(boolean isClientCall) {
        if ( instance == null ) {
            instance = new ResourceManager(isClientCall);
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
     *  will loadClient all texutres from map1.mtf to memory,
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

        for (int i = 0; i < mapSS.size(); i++) {
            Sprite sprite = mapSS.getSprites().get(i);

            if (textures.put(firstId++, sprite) != null) {
                throw new IllegalArgumentException();
            }
        }

    }

    /**
     * Load all obj textures
     */
    public void loadObjTextures(String spriteSheetPath, int firstId, ArrayList<ObjectSheetResource> objectsSheet) {
        SpriteSheet sprSheet = new SpriteSheet();
        try {
            sprSheet.setImage(spriteSheetPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (ObjectSheetResource obj : objectsSheet) {
            loadTexture(sprSheet, firstId + obj.getId(),
                    obj.getxInObjectSheet(), obj.getyInObjectSheet(),
                    obj.getWidth(), obj.getHeight());
        }
    }

    /**
     * Load all map textures from spritesheet
     * and map them according to the given
     * id.
     *
     *  @throws IllegalArgumentException
     *
     */
    public AnimationSheetResource loadAnimationTextures(SpriteSheet mapSS)
            throws IllegalArgumentException {

        int firstId = animationPoolFreeIndex;

        // create sheetResource struct to use in Animation Constructor
        AnimationSheetResource struct = new AnimationSheetResource(mapSS, firstId);

        for (int i = 0; i < mapSS.size(); i++) {
            Sprite sprite = mapSS.getSprites().get(i);

            if (textures.put(firstId++, sprite) != null) {
                throw new IllegalArgumentException();
            }

            animationPoolFreeIndex++;
        }

        return struct;
    }

    /**
     * Function load texture at specific Id
     * Using in parse Objects
     * @param spriteSheet Use constructor SpriteSheet. Use function setImage(SpriteSheetPath)
     * @param id Specific id
     * @param xInSheet x in spriteSheet
     * @param yInSheet y in spriteSheet
     * @param width width of subImage
     * @param height height of subImage
     */
    private void loadTexture(SpriteSheet spriteSheet, int id, int xInSheet, int yInSheet, int width, int height){
        Sprite sprite = spriteSheet.loadSprite(xInSheet, yInSheet, width, height);
        textures.put(id, sprite);
    }

    /** @TODO
     * This function will be read file
     * that contains information about each
     * character animations and loadClient this info
     * into Animations arrays.
     *
     * By now, this class is under development,
     * so it's do fake actions
     *
     * @param isClientSideCall - is this call came from client side
     */
    private void loadAllAnimationsInformation(boolean isClientSideCall) {

        if (!isClientSideCall) {
            loadAllAnimationsInformationForServer();
            return;
        }

        ArrayList<Animation> characterTestAnims = new ArrayList<>();

        SpriteSheet goblinSheet = new SpriteSheet
                ("res/animation/goblin/goblin.png", 64, 64);

        AnimationSheetResource charSprites = loadAnimationTextures(goblinSheet);

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
        deathAnim.setLooped(false);
        deathAnim.setShouldStartOver(false);

        Animation leftAttackAnim = new Animation(charSprites, 40, 3);
        leftAttackAnim.setLooped(false);
        Animation upAttackAnim = new Animation(charSprites, 29, 3);
        upAttackAnim.setLooped(false);
        Animation rightAttackAnim = new Animation(charSprites, 18, 3);
        rightAttackAnim.setLooped(false);
        Animation downAttackAnim = new Animation(charSprites, 7, 3);
        downAttackAnim.setLooped(false);

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
        Animation botDeathAnim = new Animation(charSprites, 44, 5);
        botDeathAnim.setLooped(false);
        botDeathAnim.setShouldStartOver(false);

        Animation botLeftAttackAnim = new Animation(charSprites, 40, 3);
        Animation botUpAttackAnim = new Animation(charSprites, 29, 3);
        Animation botRightAttackAnim = new Animation(charSprites, 18, 3);
        Animation botDownAttackAnim = new Animation(charSprites, 7, 3);

        botTestAnims.add(botWaitAnim);
        botTestAnims.add(botRightMoveAnim);
        botTestAnims.add(botLeftMoveAnim);
        botTestAnims.add(botUpMoveAnim);
        botTestAnims.add(botDownMoveAnim);
        botTestAnims.add(botDeathAnim);

        botTestAnims.add(botLeftAttackAnim);
        botTestAnims.add(botUpAttackAnim);
        botTestAnims.add(botRightAttackAnim);
        botTestAnims.add(botDownAttackAnim);

        animations.put(CharacterTypes.ABSTRACT_BOT, botTestAnims);

        ArrayList<Animation> batAnims = new ArrayList<>();

        SpriteSheet batSheet = new SpriteSheet
                (Bat.BAT_SPRITE_PATH, 32, 32);

        AnimationSheetResource batSprites = loadAnimationTextures(batSheet);

        Animation batWaitAnim = new Animation(batSprites, 1, 3);
        Animation batDownMoveAnim = new Animation(batSprites, 1, 3);
        Animation batRightMoveAnim = new Animation(batSprites, 5, 3);
        Animation batLeftMoveAnim = new Animation(batSprites, 13, 3);
        Animation batUpMoveAnim = new Animation(batSprites, 9, 3);
        Animation batDeathAnim = new Animation(batSprites, 12, 1);
        batDeathAnim.setLooped(false);
        batDeathAnim.setShouldStartOver(false);

        batAnims.add(batWaitAnim);
        batAnims.add(batRightMoveAnim);
        batAnims.add(batLeftMoveAnim);
        batAnims.add(batUpMoveAnim);
        batAnims.add(batDownMoveAnim);
        batAnims.add(batDeathAnim);

        batAnims.add(batLeftMoveAnim);
        batAnims.add(batUpMoveAnim);
        batAnims.add(batRightMoveAnim);
        batAnims.add(batDownMoveAnim);

        animations.put(CharacterTypes.BAT_BOT, batAnims);
    }

    private void loadAllAnimationsInformationForServer() {

        serverAnimations = new HashMap<>();

        //read information about animations from file

        Scanner s = null;

        try {
            s = new Scanner(new BufferedReader(new FileReader(ANIMATION_PATH)));

            while (s.hasNext()) {
                String next = s.nextLine();

                if (next.startsWith("//") || next.equals("")) continue;

                if (next.startsWith("#")) {

                    next = s.nextLine();

                    String[] splitedNext = next.split(" ");

                    // parse animations for next Character Type

                    int characterType = Integer.parseInt(splitedNext[0]);

                    int startOffset = Integer.parseInt(splitedNext[4]);
                    int length = Integer.parseInt(splitedNext[5]);

                    AnimationInfo nextInfo = new AnimationInfo(startOffset, length);

                    ArrayList<ServerAnimation> nextAnimations = new ArrayList<>();


                    next = s.nextLine();
                    splitedNext = next.split(" ");

                    while (!splitedNext[0].equals("$")) {

                        // creating next Animation

                        int offset = Integer.parseInt(splitedNext[0]);
                        int len = Integer.parseInt(splitedNext[1]);

                        ServerAnimation nextAnimation = new ServerAnimation(nextInfo, offset, len);

                        nextAnimations.add(nextAnimation);

                        next = s.nextLine();
                        splitedNext = next.split(" ");

                    }

                    CharacterTypes charType = CharacterTypes.ABSTRACT_CHARACTER;

                    if (characterType == 0) {
                        charType = CharacterTypes.ABSTRACT_CHARACTER;
                    } else if (characterType == 1) {
                        charType = CharacterTypes.ABSTRACT_PLAYER;
                    } else if (characterType == 2) {
                        charType = CharacterTypes.ABSTRACT_BOT;
                    } else if (characterType == 3) {
                        charType = CharacterTypes.BAT_BOT;
                    }

                    if (nextAnimations.size() >= 6) {
                        // there are death animation
                        nextAnimations.get(5).setShouldStartOver(false);
                        nextAnimations.get(5).setLooped(false);
                    }

                    serverAnimations.put(charType, nextAnimations);

                }


            }
        } catch(FileNotFoundException e) {

            System.err.println("ANIMATION PATH INVALID");
            e.printStackTrace();

        }finally {

            if (s != null) {
                s.close();
            }

        }
    }

    /**
     * Returns textureId, or -1 if there are no such
     * texture
     *
     * @param textureName
     * @return textureId
     */
    public Integer getTexture(int textureName) {
        return textures.get(new Integer(textureName)).getTextureId();
    }

    public Sprite getSprite(int textureName) { return textures.get(new Integer(textureName)); }
    /**
     * Return character animation by id.
     *
     * @param character
     * @return animations
     */
    public ArrayList<Animation> getAnimations(GameCharacter character) {
        return animations.get(character.getCharacterId());
    }

    public ArrayList<ServerAnimation> getServerAnimations(GameCharacter character) {
        return serverAnimations.get(character.getCharacterId());
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
