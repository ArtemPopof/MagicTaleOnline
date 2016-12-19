package com.p3k.magictale.map.objects;

import com.p3k.magictale.engine.graphics.Map.Tile;
import com.p3k.magictale.engine.graphics.Map.TileProperties;
import com.p3k.magictale.engine.graphics.Objects.GroupObject;
import com.p3k.magictale.engine.graphics.Objects.ObjTile;
import com.p3k.magictale.engine.graphics.ResourceManager;
import com.p3k.magictale.engine.graphics.Sprite;
import com.p3k.magictale.map.XmlParser;
import com.p3k.magictale.map.level.LevelManager;
import org.xml.sax.SAXException;
import sun.awt.image.ImageWatched;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.TreeMap;

/**
 * Created by COMar-PC on 19.12.2016.
 */
public class ObjectManager implements ObjectInterface {
    private static final String LEVEL_DIR = "res/map/objects/";
    private int lvlLayer = 4;

    private static ObjectManager instance = null;
    private static DocumentBuilderFactory dbf = null;
    private ObjTile[][][] objTile = null;
    private Tile[][] tileSheet = null;
    private TreeMap<String, TreeMap<String, ArrayList<GroupObject>>> groupObjects = null;

    private ObjectManager() throws Exception {
        groupObjects = new TreeMap<>();

        try {
            dbf = DocumentBuilderFactory.newInstance();
        } catch (Exception e) {
            System.err.println("Can't create document builder factory: " + e);
        }
    }

    public static ObjectManager getInstance() throws Exception {
        if (instance == null) {
            instance = new ObjectManager();
        }

        return instance;
    }

    public void load(String mapName, ResourceManager resourceManager) {

        XmlParser xml = null;
        try {
            xml = new XmlParser(dbf, LEVEL_DIR + "obj_" + mapName + ".tmx");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

        // TODO Add when would be added more lvl
//        ArrayList<String> spriteSheetPaths = null;
//        if (xml != null) {
//            spriteSheetPaths = xml.getSpriteSheetPaths();
//        }
//        for (String spriteSheetPath:
//                spriteSheetPaths) {
//            loadObjTexturePack(spriteSheetPath, resourceManager);
//        }
        System.out.println("HERE ObjMan load");
        // TODO Add constant
        int firstId = 7000;
        loadObjTexturePack(LEVEL_DIR + "pack_forest_summer.png", resourceManager, firstId);
        loadTileSheet(xml, resourceManager, firstId);
        loadTemplateGroupObjects(xml);


//        ArrayList<String> layerGrContext = null;
//        ArrayList<TileProperties> lvlTilesProperties = null;
//        int sprWidth = 32;
//        int sprHeight = 32;
//        if (xml != null) {
//            layerGrContext = xml.getLayerTextContextByName("gr");
//            lvlTilesProperties = xml.getTilesPropertiesByTilesetName("tiles_lvl_forest");
//            tilesetWidth = xml.getMapSize("width");
//            tilesetHeight = xml.getMapSize("height");
//        }
//        tileMap = new TileMap[tilesetWidth][tilesetHeight];
//
//        //System.out.println(layerGrContext);
//        --tilesetHeight;
//        for (int h = tilesetHeight, id = 0; 0 <= h; h--) {
////        for (int h = 0, id = 0; 0 <= tilesetHeight - 1; h++) {
//            for (int w = 0; w < tilesetWidth; w++) {
//                // TODO Replace it like: (Now for debug)
//                // sprites.add(new Sprite(resourceManager.getTexture(Integer.parseInt(id) + LVL_CONST),
//                // WIDTH_CONST, HEIGHT_CONST));
//                int idInSprSh = Integer.parseInt(layerGrContext.get(id)) - 1;
//                int idInGl = resourceManager.getTexture(idInSprSh + 5000);
//                Sprite sprite = new Sprite(idInGl, sprWidth, sprHeight);
//                //System.out.println("spr=" + idInSprSh + "   id=" + idInGl + "   h=" + h + " w=" + w);
//
////                sprites.add(sprite);
//                tileMap[w][tilesetHeight - h] = new TileMap();
//                tileMap[w][tilesetHeight - h].getTiles().add(new Tile(sprite, w * sprWidth, h * sprHeight,
//                        lvlTilesProperties.get(idInSprSh)));
////                tileObjects.add(new Tile(sprite, w * sprWidth, h * sprHeight, lvlTilesProperties.get(idInSprSh)));
//                id++;
//
//            }
//        }
//        for (int h = 0, id = 0; h < tilesetHeight * sprHeight; h += sprHeight) {
//            for (int w = 0; w < tilesetWidth * sprWidth; w += sprWidth) {
//                // TODO Replace it like: (Now for debug)
//                // sprites.add(new Sprite(resourceManager.getTexture(Integer.parseInt(id) + LVL_CONST),
//                // WIDTH_CONST, HEIGHT_CONST));
//                int idInSprSh = Integer.parseInt(layerGrContext.get(id)) + 4999;
//                int idInGl = resourceManager.getTexture(idInSprSh);
//                Sprite sprite = new Sprite(idInGl, sprWidth, sprHeight);
//                System.out.println("spr=" + idInSprSh + "   id=" + idInGl + "   h=" + h + " w=" + w);
//
//                sprites.add(sprite);
//                tileObjects.add(new Tile(sprite, w, h));
//                id++;
//                if (id == 1580)
//                    System.out.print(" ");
//            }
//        }
    }

    public void loadObjTexturePack(String packName, ResourceManager resourceManager, int firstId) {
        try {
            resourceManager.loadMapTextures(packName, firstId);
        } catch (Exception e) {
            System.out.println("Error loadObjTexturePack: " + e);
        }
    }

    private void loadTileSheet(XmlParser xml, ResourceManager resourceManager, int firstId) {
        int sprWidth = 32;
        int sprHeight = 32;
        ArrayList<String> layerTemplateContext = null;
        ArrayList<TileProperties> tilesProperties = null;
        int tilesetWidth = 0;
        int tilesetHeight = 0;
        if (xml != null) {
            tilesetWidth = xml.getMapSize("width");
            tilesetHeight = xml.getMapSize("height");
            layerTemplateContext = xml.getLayerTextContextByName("Test");
            tilesProperties = xml.getTilesPropertiesByTilesetName("pack_forest");
        }
        tileSheet = new Tile[tilesetWidth][tilesetHeight];
        --tilesetHeight;
        for (int h = tilesetHeight, id = 0, idInSprSh = 0; 0 <= h; h--) {
            for (int w = 0; w < tilesetWidth; w++) {
                if (Integer.parseInt(layerTemplateContext.get(id)) == 0) {
                    ++id;
                    continue;
                }
                int idInGl = resourceManager.getTexture(idInSprSh + firstId);
                Sprite sprite = new Sprite(idInGl, sprWidth, sprHeight);
                tileSheet[w][tilesetHeight - h] = new Tile(sprite, w * sprWidth, h * sprHeight,
                        tilesProperties.get(idInSprSh));
                ++id;
                ++idInSprSh;
            }
        }
        System.out.print("HERE SpriteSheet loaded");
    }

    private void loadTemplateGroupObjects(XmlParser xml) {
        try {
            objTile = new ObjTile[LevelManager.getInstance().getLvlWidth()]
                    [LevelManager.getInstance().getLvlWidth()][lvlLayer];
        } catch (Exception e) {
            e.printStackTrace();
        }
        ArrayDeque<GroupObject> waitedGroupObjects = xml.getGroupObjectsByGroupName("GameObjects");

//
//        TreeMap<String, ArrayList<GroupObject>> insertedGroupObject = new TreeMap<>();
//        ArrayList<GroupObject> listOfGrObj = new ArrayList<>();
//
////        Test1
//        GroupObject insGrObj = new GroupObject(1, 0);
//        listOfGrObj.add(insGrObj);
//        insertedGroupObject.put("tent", listOfGrObj);
//        groupObjects.put("structure", insertedGroupObject);
//
////        Test2
//        GroupObject insGrObj1 = new GroupObject(1, 1);
//        insertedGroupObject = groupObjects.get("structured");
////        if (groupObjects.containsKey("structure")) {
//        if (insertedGroupObject != null) {
//            listOfGrObj = insertedGroupObject.get("tent");
//            if (listOfGrObj != null) {
//                listOfGrObj.add(insGrObj1);
//            }
//        } else {
//            listOfGrObj = new ArrayList<>();
//            insertedGroupObject = new TreeMap<>();
//            listOfGrObj.add(insGrObj1);
//            insertedGroupObject.put("tent", listOfGrObj);
//            groupObjects.put("structured", insertedGroupObject);
//        }

    }

    public void render() {
        try {
            for (int y = 0; y < LevelManager.getInstance().getLvlHeight(); ++y) {
                for (int x = 0; x < LevelManager.getInstance().getLvlWidth(); ++x) {
                    for (int z = 0; z < lvlLayer; ++z) {
    //                    objTile[x][y][z].render();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
