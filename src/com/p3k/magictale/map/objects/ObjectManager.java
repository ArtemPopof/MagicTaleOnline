package com.p3k.magictale.map.objects;

import com.p3k.magictale.engine.graphics.Objects.GroupObject;
import com.p3k.magictale.engine.graphics.Objects.ObjTile;
import com.p3k.magictale.engine.graphics.Objects.ObjTileProperties;
import com.p3k.magictale.engine.graphics.ResourceManager;
import com.p3k.magictale.map.XmlParser;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Created by COMar-PC on 19.12.2016.
 */
public class ObjectManager {
    private static final String LEVEL_DIR = "res/map/objects/";
    private int lvlHeight = 0;
    private int lvlWidth = 0;

    private static ObjectManager instance = null;
    private static DocumentBuilderFactory dbf = null;
    private ObjTile[][][] objTile = null;
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
        if ( instance == null ) {
            instance = new ObjectManager();
        }

        return instance;
    }

    public void load(String mapName, ResourceManager resourceManager) {
//        objTile = new ObjTile[][][];
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

        XmlParser xml = null;
        try {
            xml = new XmlParser(dbf, LEVEL_DIR + "obj_" + mapName + ".tmx");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        ArrayList<String> spriteSheetPaths = null;
        if (xml != null) {
            spriteSheetPaths = xml.getSpriteSheetPaths();
        }

        System.out.println("HERE Load");
        try {
            int firstId = 6000;
//            for (String spriteSheetPath:
//                 spriteSheetPaths) {
//                System.out.println("TRY ");
//                resourceManager.loadMapTextures(spriteSheetPath, firstId);
//            }
            resourceManager.loadMapTextures(LEVEL_DIR + "pack_forest_summer.png", firstId);
        } catch (Exception e) {
            System.out.println("Error level load: " + e);
        }

        int sprWidth = 32;
        int sprHeight = 32;
        ArrayList<ObjTileProperties> objTilesProperties = null;
        if (xml != null) {
            objTilesProperties = xml.getObjTilesPropertiesByTilesetName("tiles_lvl_forest");
            lvlWidth = xml.getMapSize("width");
            lvlHeight = xml.getMapSize("height");
        }



//        ArrayList<String> layerGrContext = null;
//        ArrayList<TileProperties> lvlTilesProperties = null;
//        int sprWidth = 32;
//        int sprHeight = 32;
//        if (xml != null) {
//            layerGrContext = xml.getLayerTextContextByName("gr");
//            lvlTilesProperties = xml.getTilesPropertiesByTilesetName("tiles_lvl_forest");
//            lvlWidth = xml.getMapSize("width");
//            lvlHeight = xml.getMapSize("height");
//        }
//        tileMap = new TileMap[lvlWidth][lvlHeight];
//
//        //System.out.println(layerGrContext);
//        --lvlHeight;
//        for (int h = lvlHeight, id = 0; 0 <= h; h--) {
////        for (int h = 0, id = 0; 0 <= lvlHeight - 1; h++) {
//            for (int w = 0; w < lvlWidth; w++) {
//                // TODO Replace it like: (Now for debug)
//                // sprites.add(new Sprite(resourceManager.getTexture(Integer.parseInt(id) + LVL_CONST),
//                // WIDTH_CONST, HEIGHT_CONST));
//                int idInSprSh = Integer.parseInt(layerGrContext.get(id)) - 1;
//                int idInGl = resourceManager.getTexture(idInSprSh + 5000);
//                Sprite sprite = new Sprite(idInGl, sprWidth, sprHeight);
//                //System.out.println("spr=" + idInSprSh + "   id=" + idInGl + "   h=" + h + " w=" + w);
//
////                sprites.add(sprite);
//                tileMap[w][lvlHeight - h] = new TileMap();
//                tileMap[w][lvlHeight - h].getTiles().add(new Tile(sprite, w * sprWidth, h * sprHeight,
//                        lvlTilesProperties.get(idInSprSh)));
////                tileObjects.add(new Tile(sprite, w * sprWidth, h * sprHeight, lvlTilesProperties.get(idInSprSh)));
//                id++;
//
//            }
//        }
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
//                tileObjects.add(new Tile(sprite, w, h));
//                id++;
//                if (id == 1580)
//                    System.out.print(" ");
//            }
//        }

    }
//
//    public void render() {
//        for (int y = 0; y < lvlHeight; ++y) {
//            for (int x = 0; x < lvlWidth; ++x) {
//                for (int z = 0; z < lvlLayer; ++z) {
//                    objTile[x][y][z].render();
//                }
//            }
//        }
//    }
}
