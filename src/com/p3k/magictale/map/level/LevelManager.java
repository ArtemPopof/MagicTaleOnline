package com.p3k.magictale.map.level;

import com.p3k.magictale.engine.graphics.ResourceManager;
import com.p3k.magictale.engine.graphics.Sprite;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.util.ArrayList;

// TODO Static constant

public class LevelManager implements Level {
    private static LevelManager instance = null;
    private static DocumentBuilderFactory dbf = null;
    private static final String LEVEL_DIR = "res/map/levels/";
    String mapName = "forest";
    String pathName = "res/map/levels/lvl_forest.tmx";
    private ArrayList<Sprite> sprites = null;


    private LevelManager() throws Exception {
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
    }

    public void render() {
        for (Sprite sprite:
             sprites) {
            sprite.render();
        }
    }
}
