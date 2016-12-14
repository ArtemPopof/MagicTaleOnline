package com.p3k.magictale.map.level;

import com.p3k.magictale.engine.Constants;
import com.p3k.magictale.engine.MagicMain;
import com.p3k.magictale.engine.graphics.GameObject;
import com.p3k.magictale.engine.graphics.ResourceManager;
import com.p3k.magictale.engine.graphics.Sprite;
import com.p3k.magictale.engine.graphics.TileObject;
import com.p3k.magictale.engine.graphics.TileProperties;
import com.p3k.magictale.engine.graphics.TileMap;
import com.p3k.magictale.game.Game;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Arrays;

import static org.lwjgl.opengl.GL11.*;

// TODO Static constant

public class LevelManager implements Level {
    private static LevelManager instance = null;
    private static DocumentBuilderFactory dbf = null;
    private static final String LEVEL_DIR = "res/map/levels/";
    String mapName = "forest";
    String pathName = "res/map/levels/lvl_forest.tmx";
//    private ArrayList<Sprite> sprites = null;
//    private ArrayList<TileObject> tileObjects = null;
    private int lvlHeight = 0;
    private int lvlWidth = 0;
    private TileMap[][] tileMap = null;
//    private LinkedList<TileObject> tileMap[][] = null;


    private LevelManager() throws Exception {
//        sprites = new ArrayList<>();
//        tileObjects = new ArrayList<>();
//        LinkedList<TileObject>[][] tileMap = new LinkedList[48][32];

        try {
            dbf = DocumentBuilderFactory.newInstance();
        } catch (Exception e) {
            System.err.println("Can't create document builder factory: " + e);
        }
    }

    public static LevelManager getInstance() throws Exception {
        if ( instance == null ) {
            instance = new LevelManager();
        }

        return instance;
    }

    public void load(String mapName, ResourceManager resourceManager) {
        XmlParser xml = null;
        try {
            xml = new XmlParser(dbf, LEVEL_DIR + "lvl_" + mapName + ".tmx");
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<String> spriteSheetPaths = null;
        if (xml != null) {
            spriteSheetPaths = xml.getSpriteSheetPaths();
        }

        System.out.println("HERE Load");
        try {
            int firstId = 5000;
//            for (String spriteSheetPath:
//                 spriteSheetPaths) {
//                System.out.println("TRY ");
//                resourceManager.loadMapTextures(spriteSheetPath, firstId);
//            }
            resourceManager.loadMapTextures(LEVEL_DIR + "tiles_lvl_forest.png", firstId);
        } catch (Exception e) {
            System.out.println("Error level load: " + e);
        }

        ArrayList<String> layerGrContext = null;
        ArrayList<TileProperties> lvlTilesProperties = null;
        int sprWidth = 32;
        int sprHeight = 32;
        if (xml != null) {
            layerGrContext = xml.getLayerTextContextByName("gr");
            lvlTilesProperties = xml.getTilesPropertiesByTilesetName("tiles_lvl_forest");
            lvlWidth = xml.getMapSize("width");
            lvlHeight = xml.getMapSize("height");
        }
        tileMap = new TileMap[lvlWidth][lvlHeight];

        System.out.println(layerGrContext);
        for (int h = lvlHeight-1, id = 0; 0 <= h; h--) {
            for (int w = 0; w < lvlWidth; w++) {
                // TODO Replace it like: (Now for debug)
                // sprites.add(new Sprite(resourceManager.getTexture(Integer.parseInt(id) + LVL_CONST),
                // WIDTH_CONST, HEIGHT_CONST));
                int idInSprSh = Integer.parseInt(layerGrContext.get(id)) - 1;
                int idInGl = resourceManager.getTexture(idInSprSh + 5000);
                Sprite sprite = new Sprite(idInGl, sprWidth, sprHeight);
                System.out.println("spr=" + idInSprSh + "   id=" + idInGl + "   h=" + h + " w=" + w);

//                sprites.add(sprite);
                tileMap[w][h] = new TileMap();
                tileMap[w][h].getTileObjects().add(new TileObject(sprite, w * sprWidth, h * sprHeight,
                        lvlTilesProperties.get(idInSprSh)));
//                tileObjects.add(new TileObject(sprite, w * sprWidth, h * sprHeight, lvlTilesProperties.get(idInSprSh)));
                id++;
                if (id == 1580)
                    System.out.print(" ");
            }
        }
//        for (int h = 0, id = 0; h < lvlHeight * sprHeight; h += sprHeight) {
//            for (int w = 0; w < lvlWidth * sprWidth; w += sprWidth) {
//                // TODO Replace it like: (Now for debug)
//                // sprites.add(new Sprite(resourceManager.getTexture(Integer.parseInt(id) + LVL_CONST),
//                // WIDTH_CONST, HEIGHT_CONST));
//                int idInSprSh = Integer.parseInt(layerGrContext.get(id)) + 4999;
//                int idInGl = resourceManager.getTexture(idInSprSh);
//                Sprite sprite = new Sprite(idInGl, sprWidth, sprHeight);
//                System.out.println("spr=" + idInSprSh + "   id=" + idInGl + "   h=" + h + " w=" + w);
//
//                sprites.add(sprite);
//                tileObjects.add(new TileObject(sprite, w, h));
//                id++;
//                if (id == 1580)
//                    System.out.print(" ");
//            }
//        }
    }

    /**
     *
     * Return Tile position like (x, y)
     * where x is column in map and y is row
     *
     * @param x float coords of player,
     * @param y
     * @return
     */
    public static Point getTilePointByCoordinates(float x, float y) {

        Point result = new Point();

        // now result in java coordinates
        result.x = (int) (x / Constants.MAP_TILE_SIZE);
        result.y = (int) (y / Constants.MAP_TILE_SIZE);

        // y coordinate should be inversed
        result.y = Constants.MAP_HEIGHT - result.y;

        return result;

    }

    public void render() {
//        for (TileObject object : tileObjects) {
//
//            object.render();
//
//        }
        for (int y = 0; y < lvlHeight; ++y) {
            for (int x = 0; x < lvlWidth; ++x) {
                for (TileObject object : tileMap[x][y].getTileObjects()) {
                    object.render();
                }
            }
        }
    }
}
