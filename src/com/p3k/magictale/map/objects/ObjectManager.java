package com.p3k.magictale.map.objects;

import com.p3k.magictale.engine.Constants;
import com.p3k.magictale.engine.graphics.ITileProperties;
import com.p3k.magictale.engine.graphics.Map.Tile;
import com.p3k.magictale.engine.graphics.Map.TileMap;
import com.p3k.magictale.engine.graphics.Map.TileProperties;
import com.p3k.magictale.engine.graphics.Objects.GroupObject;
import com.p3k.magictale.engine.graphics.Objects.ObjTile;
import com.p3k.magictale.engine.graphics.Objects.ObjTileProperties;
import com.p3k.magictale.engine.graphics.ResourceManager;
import com.p3k.magictale.engine.graphics.Sprite;
import com.p3k.magictale.map.XmlParser;
import com.p3k.magictale.map.level.Level;
import com.p3k.magictale.map.level.LevelManager;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Created by COMar-PC on 19.12.2016.
 */
public class ObjectManager implements ObjectInterface {
    private static final String LEVEL_DIR = "res/map/objects/";
    private static ObjectManager instance = null;
    private static DocumentBuilderFactory dbf = null;
    private int lvlLayer = 3;
    //    private ObjTile[][][] objTile = null;
    private ISharedObjTile objTile = null;
    private Tile[][] tileSheet = null;
    private TreeMap<String, TreeMap<String, ArrayList<GroupObject>>> groupObjects = null;

    private ObjectManager() throws Exception {
        groupObjects = new TreeMap<>();
        try {
//            objTile = new ObjTile[LevelManager.getInstance().getLvlWidth()]
//                    [LevelManager.getInstance().getLvlHeight()][lvlLayer];
            String remoteIP = System.getenv("IP");
            if (remoteIP == null) {
                objTile = new SharedObjTile(LevelManager.getInstance().getLvlWidth(),
                        LevelManager.getInstance().getLvlHeight(),
                        lvlLayer);
                Naming.bind("objTile", objTile);
                System.out.println("objTile RMI ready");
            } else {
                Registry registry = LocateRegistry.getRegistry(remoteIP);
                objTile = (ISharedObjTile) registry.lookup("objTile");
                System.out.println("remote objTile in use");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

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
        System.out.println("HERE Lvl load");

        // Parse and add to map GroupObject"sheet"
        XmlParser xmlObj = null, xmlLvl = null, xmlTileProps = null;
        try {
            xmlObj = new XmlParser(dbf, LEVEL_DIR + "obj_" + mapName + ".tmx");
            xmlLvl = new XmlParser(dbf, LevelManager.getLevelDir() + "lvl_" + mapName + ".tmx");
        } catch (IOException | SAXException e) {
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
        // TODO Add constant
        int firstId = 7000;
        loadObjTexturePack(LEVEL_DIR + "pack_forest_summer.png", resourceManager, firstId);
        loadTileSheet(xmlObj, resourceManager, firstId);
        loadTemplateGroupObjects(xmlObj);

        // Parse and add to map TileObjects and GroupObjects
        loadLvlGroupObjects(xmlLvl);

        // FOR DEBUG
//        try {
//            System.out.println("Map of isPass");
//            ObjTile chObj;
//            int height = Constants.MAP_HEIGHT - 1;
//            for (int y = 0; y <= height; ++y) {
//                System.out.print("y = " + y + "\t");
//                for (int x = 0; x < Constants.MAP_WIDTH; ++x) {
////                for (int z = 0; z < lvlLayer; ++z) {
//                    chObj = objTile.getObjTileByXYZ(x, y, 2);
//                    if (chObj != null) {
//                        if (chObj.isPass())
//                            System.out.print("1 ");
//                        else
//                            System.out.print("0 ");
//                    } else {
//                        System.out.print("- ");
//                    }
//                    //                }
////                    System.out.println(" ");
//                }
//                System.out.println();
//            }
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
        System.out.println("HERE Objects loaded");
    }

    public void loadObjTexturePack(String packName, ResourceManager resourceManager, int firstId) {
        try {
            resourceManager.loadMapTextures(packName, firstId);
        } catch (Exception e) {
            System.out.println("Error loadObjTexturePack: " + e);
        }
    }

    private void loadTileSheet(XmlParser xml, ResourceManager resourceManager, int firstId) {
        ArrayList<String> layerTemplateContext = null;
        ArrayList<ITileProperties> tilesProperties = null;
        int tilesetWidth = 0;
        int tilesetHeight = 0;
        if (xml != null) {
            tilesetWidth = xml.getMapSize("width");
            tilesetHeight = xml.getMapSize("height");
            layerTemplateContext = xml.getLayerTextContextByName("Test");
            tilesProperties = xml.getTilesPropertiesByTilesetName("pack_forest", "tile");
        }
        tileSheet = new Tile[tilesetWidth][tilesetHeight];
        for (int h = 0, id = 0, idInSprSh = 0, idInTileProp = 0; h < tilesetHeight; ++h) {
            for (int w = 0; w < tilesetWidth; w++) {
                if (Integer.parseInt(layerTemplateContext.get(id)) == 0) {
                    ++id;
                    ++idInSprSh;
                    continue;
                }
                int idInGl = resourceManager.getTexture(idInSprSh + firstId);
                Sprite sprite = new Sprite(idInGl, Constants.MAP_TILE_SIZE, Constants.MAP_TILE_SIZE);
                tileSheet[w][h] = new Tile(sprite, w * Constants.MAP_TILE_SIZE, h * Constants.MAP_TILE_SIZE,
                        (TileProperties) tilesProperties.get(idInTileProp));
                ++id;
                ++idInSprSh;
                ++idInTileProp;
            }
        }
        System.out.print("HERE SpriteSheet loaded");
    }

    private void loadTemplateGroupObjects(XmlParser xml) {
        ArrayDeque<GroupObject> waitedGroupObjects = xml.getGroupObjectsByGroupName("GameObjects",
                false).clone();
        addGroupObjects(waitedGroupObjects);
    }

    private void addGroupObjects(ArrayDeque<GroupObject> waitedGroupObjects) {
        while (true) {
            try {
                GroupObject insGrObj = waitedGroupObjects.pollFirst();
                if (insGrObj != null) {
                    addGroupObject(insGrObj);
                } else {
                    break;
                }
            } catch (Exception e) {
                System.out.println("Load template groupObjects: " + e);
            }
        }
    }

    private int addGroupObject(GroupObject waitedGroupObject) {
        TreeMap<String, ArrayList<GroupObject>> waitedTreeMap;
        ArrayList<GroupObject> waitedArrayOfGrObj = new ArrayList<>();
        try {
            waitedTreeMap = groupObjects.get(waitedGroupObject.getType());
            if (waitedTreeMap != null) {
                waitedArrayOfGrObj = waitedTreeMap.get(waitedGroupObject.getName());
                if (waitedArrayOfGrObj != null) {
                    waitedArrayOfGrObj.add(waitedGroupObject);
                } else {
                    waitedArrayOfGrObj = new ArrayList<>();
                    waitedArrayOfGrObj.add(waitedGroupObject);
                    waitedTreeMap.put(waitedGroupObject.getName(), waitedArrayOfGrObj);
                    int id = waitedArrayOfGrObj.indexOf(waitedGroupObject);
                    return id;
                }
            } else {
                waitedTreeMap = new TreeMap<>();
                waitedArrayOfGrObj = new ArrayList<>();
                waitedArrayOfGrObj.add(waitedGroupObject);
                waitedTreeMap.put(waitedGroupObject.getName(), waitedArrayOfGrObj);
                groupObjects.put(waitedGroupObject.getType(), waitedTreeMap);
                int id = waitedArrayOfGrObj.indexOf(waitedGroupObject);
                return id;
            }
        } catch (Exception e) {
            System.out.println("Load template groupObjects: " + e);
        }
        int id = waitedArrayOfGrObj.indexOf(waitedGroupObject);
        return id;
    }

    private void loadLvlGroupObjects(XmlParser xml) {
        ArrayDeque<GroupObject> insertedGroupObjects;
        insertedGroupObjects = xml.getGroupObjectsByGroupName("GameObjects", true).clone();
        addGroupObjectsByTemplate(insertedGroupObjects);
        // TODO Change addTileToObjTile(int layer) from LevelManager
        addTilesToObjTiles();
    }

    private void addTilesToObjTiles() {
        int lvlWidth = 48, lvlHeigth = 33;
        try {
            lvlWidth = LevelManager.getInstance().getLvlWidth();
            lvlHeigth = LevelManager.getInstance().getLvlHeight();
        } catch (Exception e) {
            e.printStackTrace();
        }
        --lvlHeigth;
        for (int w = 0; w < lvlWidth; ++w) {
            for (int h = 0; h <= lvlHeigth; ++h) {
                Tile insTile = null;
                try {
                    insTile = LevelManager.getInstance().getTileMap()[w][lvlHeigth - h].getTiles().get(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ObjTile insObjTile = new ObjTile(insTile.getSprite(), insTile.getX(), insTile.getY());
                ObjTileProperties objTileProperties = new ObjTileProperties();
                objTileProperties.setPass(insTile.getTileProperties().isPass());
                objTileProperties.setFly(insTile.getTileProperties().isFly());
                insObjTile.setObjTileProperties(objTileProperties);
                try {
                    objTile.setObjTileByXYZ(w, lvlHeigth - h, 0, insObjTile);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
        ++lvlHeigth;
    }

    private void addGroupObjectsByTemplate(ArrayDeque<GroupObject> waitedGroupObjects) {
        while (true) {
            try {
                GroupObject insGrObj = waitedGroupObjects.pollFirst();
                if (insGrObj != null) {
                    GroupObject templateGrObj = getTemplateGroupObject(insGrObj.getType(), insGrObj.getName());
                    if (templateGrObj != null) {
                        insGrObj.setHeightNum(templateGrObj.getHeightNum());
                        insGrObj.setWidthNum(templateGrObj.getWidthNum());
                        insGrObj.setxTileSheet(templateGrObj.getxTileSheet());
                        insGrObj.setyTileSheet(templateGrObj.getyTileSheet());
                        int id = addGroupObject(insGrObj);
                        addObjTiles(insGrObj.getX(), insGrObj.getY(),
                                insGrObj.getxTileSheet(), insGrObj.getyTileSheet(),
                                insGrObj.getWidthNum(), insGrObj.getHeightNum(),
                                insGrObj.getType(), insGrObj.getName(), id);
                    } else {
                        System.out.println("Check .xml file!");
                    }
                } else {
                    break;
                }
            } catch (Exception e) {
                System.out.println("Load template groupObjects: " + e);
            }
        }
    }

    private void addObjTiles(int x, int y, int xTileSheet, int yTileSheet, int widthNum, int heightNum,
                             String type, String name, int id) {
        int lvlHeight = Constants.MAP_HEIGHT;
        --lvlHeight;
        ObjTile insObjTile;
//        insObjTile = new ObjTile(tileSheet[xTileSheet][yTileSheet].getSprite(), (x) * 32, (lvlHeight - y) * 32);
        for (int h = 0; h < heightNum; ++h) {
            for (int w = 0; w < widthNum; ++w) {
                Tile tile = tileSheet[xTileSheet + w][yTileSheet + h];
                insObjTile = new ObjTile(tile.getSprite(), (x + w) * 32, (lvlHeight - y - h) * 32);
//                System.out.println("x=" + (x+w) + "   y=" + (y+h) + "   lvl-y-h=" + (lvlHeight - y - h)
//                + "     tsX=" + (xTileSheet + w) + "    tsY=" + (yTileSheet + h));
                insObjTile.setType(type);
                insObjTile.setName(name);
                insObjTile.setIdInTypeName(id);
                insObjTile.setObjTileProperties(tile.getTileProperties());
//                objTile[x + w][lvlHeight - y - h][tile.getTileProperties().getLayer()] = insObjTile;
                try {
                    objTile.setObjTileByXYZ(x + w, y + h, tile.getTileProperties().getLayer(), insObjTile);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
//        System.out.println("Type=" + insObjTile.getType()
//                + "   Name=" + insObjTile.getName()
//                + " id=" + insObjTile.getIdInTypeName()
//                + " spr=" + insObjTile.getSprite().getTextureId()
//                + " sheet=" + (insObjTile.getSprite().getTextureId() - 162));
        ++lvlHeight;
    }

    public GroupObject getTemplateGroupObject(String type, String name) {
        return groupObjects.get(type).get(name).get(0);
    }

    public void render() {
        try {
            for (int x = 0; x < LevelManager.getInstance().getLvlWidth(); ++x) {
                for (int y = 0; y < LevelManager.getInstance().getLvlHeight(); ++y) {
                    for (int z = 0; z < lvlLayer; ++z) {
                        ObjTile waitedObjTile = objTile.getObjTileByXYZ(x, y, z);
                        if (waitedObjTile != null) {
                            waitedObjTile.render();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void render(int layer) {
        try {
            ObjTile[][][] waitedObjTiles = objTile.getObjTile(LevelManager.getInstance().getLvlWidth(),
                    LevelManager.getInstance().getLvlHeight(),
                    layer);
            for (int x = 0; x < LevelManager.getInstance().getLvlWidth(); ++x) {
                for (int y = 0; y < LevelManager.getInstance().getLvlHeight(); ++y) {
                    ObjTile waitedObjTile = waitedObjTiles[x][y][0];
                    if (waitedObjTile != null) {
                        waitedObjTile.render();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isPass(int x, int y, int layer, boolean isFly) {
        boolean isPass = true;
        ObjTile checkObjTile = null;
        int lvlHeight = Constants.MAP_HEIGHT;
        --lvlHeight;
        try {
            checkObjTile = objTile.getObjTileByXYZ(x, y, layer);
            if (checkObjTile != null) {
                return checkObjTile.isPass();
            }
//            if (isFly) {
//
//            } else {
            for (int i = 0; i < layer; ++i) {
                checkObjTile = objTile.getObjTileByXYZ(x, y, i);
                if (checkObjTile != null) {
                    if (!checkObjTile.isPass()) {
                        return false;
                    }
                }
            }
//            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return isPass;
    }

    public static Point getTilePointByCoordinates(float x, float y) {

        Point result = new Point();

        // now result in java coordinates
        result.x = (int) (x / Constants.MAP_TILE_SIZE);
        result.y = (int) (y / Constants.MAP_TILE_SIZE);

        // y coordinate should be inversed
        result.y = Constants.MAP_HEIGHT - result.y;

        return result;

    }
}
