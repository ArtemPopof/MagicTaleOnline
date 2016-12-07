package com.p3k.magictale.map.level;

import com.p3k.magictale.engine.graphics.GameObject;
import com.p3k.magictale.engine.graphics.ResourceManager;
import com.p3k.magictale.engine.graphics.Sprite;
import com.p3k.magictale.engine.graphics.TileObject;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

// TODO Static constant

public class LevelManager implements Level {
    private static LevelManager instance = null;
    private static DocumentBuilderFactory dbf = null;
    private static final String LEVEL_DIR = "res/map/levels/";
    String mapName = "forest";
    String pathName = "res/map/levels/lvl_forest.tmx";
    private ArrayList<Sprite> sprites = null;
    private ArrayList<TileObject> tileObjects = null;


    private LevelManager() throws Exception {
        sprites = new ArrayList<>();
        tileObjects = new ArrayList<>();

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
        int lvlWidth = 0;
        int lvlHeight = 0;
        int sprWidth = 32;
        int sprHeight = 32;
        if (xml != null) {
            layerGrContext = xml.getLayerTextContextByName("gr");
            lvlWidth = xml.getMapSize("width");
            lvlHeight = xml.getMapSize("height");
        }
        System.out.println(layerGrContext);
//        for (int w = 0, id = 0; w < lvlWidth; w++) {
//            for (int h = 0; h < lvlHeight; h++) {
//                // TODO Replace it like: (Now for debug)
//                // sprites.add(new Sprite(resourceManager.getTexture(Integer.parseInt(id) + LVL_CONST),
//                // WIDTH_CONST, HEIGHT_CONST));
//                int idInSprSh = Integer.parseInt(layerGrContext.get(id)) + 5000;
//                int idInGl = resourceManager.getTexture(idInSprSh);
//                Sprite sprite = new Sprite(idInGl, sprWidth, sprHeight);
//
//                sprites.add(sprite);
//                tileObjects.add(new TileObject(sprite, w * sprWidth, h * sprHeight));
//                id++;
//                if (id == 1580)
//                    System.out.print(" ");
//            }
//        }
        for (int w = 0, id = 0; w < lvlWidth * sprWidth; w += sprWidth) {
            for (int h = 0; h < lvlHeight * sprHeight; h += sprHeight) {
                // TODO Replace it like: (Now for debug)
                // sprites.add(new Sprite(resourceManager.getTexture(Integer.parseInt(id) + LVL_CONST),
                // WIDTH_CONST, HEIGHT_CONST));
                int idInSprSh = Integer.parseInt(layerGrContext.get(id)) + 5000;
                int idInGl = resourceManager.getTexture(idInSprSh);
                Sprite sprite = new Sprite(idInGl, sprWidth, sprHeight);

                sprites.add(sprite);
                tileObjects.add(new TileObject(sprite, w, h));
                id++;
                if (id == 1580)
                    System.out.print(" ");
            }
        }
    }

    public void render() {
        for (TileObject object : tileObjects) {
            object.render();
        }
    }
}
