package com.p3k.magictale.engine.graphics;

import com.p3k.magictale.game.objects.CharacterTypes;

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

        Animation waitAnimation = new Animation("res/animation/test",
                0, 8, 106, 129);
        characterTestAnims.add(waitAnimation);

        animations.put(CharacterTypes.ABSTRACT_CHARACTER, characterTestAnims);

        //next

        ArrayList<Animation> playerTestAnims = new ArrayList<>();

        Animation waitAnim = new Animation("res/animation/bigfoot/bigfoot",
                0, 20, 78, 112);

        playerTestAnims.add(waitAnim);

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
