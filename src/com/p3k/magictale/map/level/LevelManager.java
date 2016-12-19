package com.p3k.magictale.map.level;

import com.p3k.magictale.engine.Constants;
import com.p3k.magictale.engine.graphics.*;
import com.p3k.magictale.engine.graphics.Map.TileMap;
import com.p3k.magictale.engine.graphics.Map.Tile;
import com.p3k.magictale.engine.graphics.Map.TileProperties;
import com.p3k.magictale.engine.graphics.Objects.GroupObject;
import com.p3k.magictale.map.XmlParser;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;

// TODO Static constant

public class LevelManager implements Level {
    private static LevelManager instance = null;
    private static DocumentBuilderFactory dbf = null;
    private static final String LEVEL_DIR = "res/map/levels/";
    String mapName = "forest";
    String pathName = "res/map/levels/lvl_forest.tmx";
    //    private ArrayList<Sprite> sprites = null;
//    private ArrayList<Tile> tileObjects = null;
    private int lvlHeight = 0;
    private int lvlWidth = 0;
    private TileMap[][] tileMap = null;
    private TreeMap<String, TreeMap<String, ArrayList<GroupObject>>> groupObjects = null;
//    private LinkedList<Tile> tileMap[][] = null;


    private LevelManager() throws Exception {
//        sprites = new ArrayList<>();
//        tileObjects = new ArrayList<>();
//        LinkedList<Tile>[][] tileMap = new LinkedList[48][32];

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
        groupObjects = new TreeMap<>();
        TreeMap<String, ArrayList<GroupObject>> insertedGroupObject = new TreeMap<>();
        ArrayList<GroupObject> listOfGrObj = new ArrayList<>();


//        TreeMap<String, GroupObject> insertedGroupObject1 = new TreeMap<>();
//        insertedGroupObject1.put("tented", insGrObj1);
//        groupObjects.put("structure", insertedGroupObject1);

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

        //System.out.println(layerGrContext);
        --lvlHeight;
        for (int h = lvlHeight, id = 0; 0 <= h; h--) {
//        for (int h = 0, id = 0; 0 <= lvlHeight - 1; h++) {
            for (int w = 0; w < lvlWidth; w++) {
                // TODO Replace it like: (Now for debug)
                // sprites.add(new Sprite(resourceManager.getTexture(Integer.parseInt(id) + LVL_CONST),
                // WIDTH_CONST, HEIGHT_CONST));
                int idInSprSh = Integer.parseInt(layerGrContext.get(id)) - 1;
                int idInGl = resourceManager.getTexture(idInSprSh + 5000);
                Sprite sprite = new Sprite(idInGl, sprWidth, sprHeight);
                //System.out.println("spr=" + idInSprSh + "   id=" + idInGl + "   h=" + h + " w=" + w);

//                sprites.add(sprite);
                tileMap[w][lvlHeight - h] = new TileMap();
                tileMap[w][lvlHeight - h].getTiles().add(new Tile(sprite, w * sprWidth, h * sprHeight,
                        lvlTilesProperties.get(idInSprSh)));
//                tileObjects.add(new Tile(sprite, w * sprWidth, h * sprHeight, lvlTilesProperties.get(idInSprSh)));
                id++;

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
//                tileObjects.add(new Tile(sprite, w, h));
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

    /**
     * return tile coordinates.
     *
     * Upper left corner will be returned.
     *
     * @return
     */
    public static Point getCoordinatesByTile(Point tile) {

        int tileX = tile.x * Constants.MAP_TILE_SIZE;
        int tileY = tile.y * Constants.MAP_TILE_SIZE;

        return new Point(tileX, tileY);
    }

    public void render() {
//        for (Tile object : tileObjects) {
//
//            object.render();
//
//        }
        for (int y = 0; y < lvlHeight; ++y) {
            for (int x = 0; x < lvlWidth; ++x) {
                for (Tile object : tileMap[x][y].getTiles()) {
                    object.render();
                }
            }
        }
    }

    /**
     * Return tileMap
     */
    public TileMap[][] getTileMap() {
        return tileMap;
    }
}
