package com.p3k.magictale.engine.graphics;

import com.p3k.magictale.game.Characters.CharacterTypes;

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

        SpriteSheet playerSprites = new SpriteSheet("res/animation/prof/play_prof.png", 64, 64);

        ArrayList<Animation> playerTestAnims = new ArrayList<>();

        Animation waitAnim = new Animation(playerSprites, 18, 1);
        Animation rightMoveAnim = new Animation(playerSprites, 27, 9);
        Animation leftMoveAnim = new Animation(playerSprites, 9, 9);
        Animation upMoveAnim = new Animation(playerSprites, 0, 9);
        Animation downMoveAnim = new Animation(playerSprites, 18, 9);
     //   Animation deathAnim = new Animation(playerSprites, 44, 5);


        //  Animation moveAnim = new Animation("res/animation/bigfoot/bigfoot_move",
       //         0, 5, 64, 64);

        playerTestAnims.add(waitAnim);
        playerTestAnims.add(rightMoveAnim);
        playerTestAnims.add(leftMoveAnim);
        playerTestAnims.add(upMoveAnim);
        playerTestAnims.add(downMoveAnim);
    //    playerTestAnims.add(deathAnim);


        animations.put(CharacterTypes.ABSTRACT_PLAYER, playerTestAnims);
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
}
