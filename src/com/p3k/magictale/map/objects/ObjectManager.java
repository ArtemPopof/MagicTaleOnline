package com.p3k.magictale.map.objects;

import com.p3k.magictale.engine.graphics.Map.Tile;
import com.p3k.magictale.engine.graphics.Map.TileProperties;
import com.p3k.magictale.engine.graphics.Objects.GroupObject;
import com.p3k.magictale.engine.graphics.Objects.ObjTile;
import com.p3k.magictale.engine.graphics.ResourceManager;
import com.p3k.magictale.engine.graphics.Sprite;
import com.p3k.magictale.map.XmlParser;
import com.p3k.magictale.map.level.LevelManager;
import com.sun.org.apache.bcel.internal.generic.ISHL;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
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
        // TODO Add constant
        int firstId = 7000;
        loadObjTexturePack(LEVEL_DIR + "pack_forest_summer.png", resourceManager, firstId);
        loadTileSheet(xml, resourceManager, firstId);
        loadTemplateGroupObjects(xml);

        // Parse and add to map TileObjects and GroupObjects
        try {
            xml = new XmlParser(dbf, LevelManager.getLevelDir() + "lvl_" + mapName + ".tmx");
        } catch (Exception e) {
            e.printStackTrace();
        }
        loadLvlGroupObjects(xml);

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
        for (int h = 0, id = 0, idInSprSh = 0, idInTileProp = 0; h < tilesetHeight; ++h) {
            for (int w = 0; w < tilesetWidth; w++) {
                if (Integer.parseInt(layerTemplateContext.get(id)) == 0) {
                    ++id;
                    ++idInSprSh;
                    continue;
                }
                int idInGl = resourceManager.getTexture(idInSprSh + firstId);
                Sprite sprite = new Sprite(idInGl, sprWidth, sprHeight);
                tileSheet[w][h] = new Tile(sprite, w * sprWidth, h * sprHeight,
                        tilesProperties.get(idInTileProp));
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
        int lvlHeight = 0;
//        int lvlWidth = 0;
        try {
            lvlHeight = LevelManager.getInstance().getLvlHeight();
//            lvlWidth = LevelManager.getInstance().getLvlWidth();
        } catch (Exception e) {
            e.printStackTrace();
        }
        --lvlHeight;
        ObjTile insObjTile;
        insObjTile = new ObjTile(tileSheet[xTileSheet][yTileSheet].getSprite(), (x) * 32, (lvlHeight - y) * 32);
        for (int h = 0; h < heightNum; ++h) {
            for (int w = 0; w < widthNum; ++w) {
                Tile tile = tileSheet[xTileSheet + w][yTileSheet + h];
                insObjTile = new ObjTile(tile.getSprite(), (x + w) * 32, (lvlHeight - y - h) * 32);
//                System.out.println("x=" + (x+w) + "   y=" + (y+h) + "   lvl-y-h=" + (lvlHeight - y - h)
//                + "     tsX=" + (xTileSheet + w) + "    tsY=" + (yTileSheet + h));
                insObjTile.setType(type);
                insObjTile.setName(name);
                insObjTile.setIdInTypeName(id);
//                objTile[x + w][lvlHeight - y - h][tile.getTileProperties().getLayer()] = insObjTile;
                try {
                    objTile.setObjTileByXYZ(x + w, lvlHeight - y - h, tile.getTileProperties().getLayer(), insObjTile);
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
            for (int x = 0; x < LevelManager.getInstance().getLvlWidth(); ++x) {
                for (int y = 0; y < LevelManager.getInstance().getLvlHeight(); ++y) {
                    ObjTile waitedObjTile = objTile.getObjTileByXYZ(x, y, layer);
                    if (waitedObjTile != null) {
                        waitedObjTile.render();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
