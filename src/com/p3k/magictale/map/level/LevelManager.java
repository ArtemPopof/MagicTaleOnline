package com.p3k.magictale.map.level;

import client.ClientObject;
import com.p3k.magictale.engine.Constants;
import com.p3k.magictale.engine.graphics.*;
import com.p3k.magictale.engine.graphics.Map.TileMap;
import com.p3k.magictale.engine.graphics.Map.Tile;
import com.p3k.magictale.engine.graphics.Map.TileProperties;
import com.p3k.magictale.map.XmlParser;
import org.xml.sax.SAXException;
import server.ServerObject;

import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

// TODO Static constant

public class LevelManager implements Level {
    private ArrayList<ClientObject> background = new ArrayList<>();

    private static LevelManager instance = null;
    private static DocumentBuilderFactory dbf = null;
    private static final String LEVEL_DIR = "res/map/levels/";
    // TODO Add to constants. Delete it
    public static String getLevelDir() {
        return LEVEL_DIR;
    }
//    private Tile[][] levelTiles = null;

    String mapName = "forest";
    String pathName = "res/map/levels/lvl_forest.tmx";
    private int lvlHeight = 0;
    private int lvlWidth = 0;
    private TileMap[][] tileMap = null;

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

    public void loadClient(String mapName, ResourceManager resourceManager) {
        System.out.println("HERE Lvl loadClient");

        XmlParser xml = null;
        try {
            xml = new XmlParser(dbf, LEVEL_DIR + "lvl_" + mapName + ".tmx");
        } catch (SAXException | IOException e) {
            e.printStackTrace();
        }

//        ArrayList<String> spriteSheetPaths = null;
//        if (xml != null) {
//            spriteSheetPaths = xml.getSpriteSheetPaths();
//        }
        int firstId = 5000;
        try {
//            for (String spriteSheetPath:
//                 spriteSheetPaths) {
//                System.out.println("TRY ");
//                resourceManager.loadMapTextures(spriteSheetPath, firstId);
//            }
            resourceManager.loadMapTextures(LEVEL_DIR + "tiles_lvl_forest.png", firstId);
        } catch (Exception e) {
            System.out.println("Error level loadClient: " + e);
        }

        ArrayList<String> layerGrContext = null;
        if (xml != null) {
            layerGrContext = xml.getLayerTextContextByName("gr");
            lvlWidth = xml.getMapSize("width");
            lvlHeight = xml.getMapSize("height");
        }

        --lvlHeight;
        for (int h = lvlHeight, id = 0; 0 <= h; h--) {
//        for (int h = 0, id = 0; 0 <= lvlHeight - 1; h++) {
            for (int w = 0; w < lvlWidth; w++) {
                int idInSprSh = Integer.parseInt(layerGrContext.get(id)) - 1;
                if (idInSprSh == -1) {
                    ++id;
                    continue;
                }
                background.add(new ClientObject(idInSprSh + firstId,
                        w * Constants.MAP_TILE_SIZE,
                        h * Constants.MAP_TILE_SIZE));
                id++;
            }
        }
        ++lvlHeight;

        System.out.println("HERE loadClient loaded");
    }

    public void loadServer(String mapName) {
        System.out.println("HERE Lvl loadServer");

        XmlParser xml = null;
        try {
            xml = new XmlParser(dbf, LEVEL_DIR + "lvl_" + mapName + ".tmx");
        } catch (SAXException | IOException e) {
            e.printStackTrace();
        }

        int firstId = 5000;
        ArrayList<String> layerGrContext = null;
        ArrayList<ITileProperties> lvlTilesProperties = null;
        if (xml != null) {
            layerGrContext = xml.getLayerTextContextByName("gr");
            lvlTilesProperties = xml.getTilesPropertiesByTilesetName("tiles_lvl_forest", "tile");
            lvlWidth = xml.getMapSize("width");
            lvlHeight = xml.getMapSize("height");
        }
        tileMap = new TileMap[lvlWidth][lvlHeight];

        //System.out.println(layerGrContext);
        --lvlHeight;
        for (int h = lvlHeight, id = 0; 0 <= h; --h) {
//        for (int h = 0, id = 0; 0 <= lvlHeight - 1; h++) {
            for (int w = 0; w < lvlWidth; w++) {
                // TODO Replace it like: (Now for debug)
                // sprites.add(new Sprite(resourceManager.getTexture(Integer.parseInt(id) + LVL_CONST),
                // WIDTH_CONST, HEIGHT_CONST));
                int idInSprSh = Integer.parseInt(layerGrContext.get(id)) - 1;
                if (idInSprSh == -1) {
                    ++id;
                    continue;
                }

                tileMap[w][lvlHeight - h] = new TileMap();
                Tile insTile = new Tile(idInSprSh + firstId,
                        w * Constants.MAP_TILE_SIZE, h * Constants.MAP_TILE_SIZE,
                        (TileProperties) lvlTilesProperties.get(idInSprSh));
                tileMap[w][lvlHeight - h].getTiles().add(insTile);
                id++;
            }
        }
        ++lvlHeight;

        System.out.println("HERE loadServer loaded");
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
        for(ClientObject obj : background) {
            obj.render();
        }
    }

    public int getLvlHeight() {
        return lvlHeight;
    }

    public int getLvlWidth() {
        return lvlWidth;
    }

    /**
     * Return tileMap
     */
    public TileMap[][] getTileMap() {
        return tileMap;
    }
}
