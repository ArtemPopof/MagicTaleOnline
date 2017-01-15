package com.p3k.magictale.map;

import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import com.p3k.magictale.engine.graphics.ITileProperties;
import com.p3k.magictale.engine.graphics.Map.TileProperties;
import com.p3k.magictale.engine.graphics.Objects.GroupObject;
import com.p3k.magictale.engine.graphics.Objects.GroupObjectProperties;
import com.p3k.magictale.engine.graphics.Objects.ObjTileProperties;
import com.p3k.magictale.engine.graphics.Objects.ObjectSheetResource;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

/**
 * Created by COMar-PC on 04.12.2016.
 */
public class XmlParser {
    private Document doc = null;

    public XmlParser() throws IOException, SAXException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        // f.setValidating(false); // не делать проверку валидации
        DocumentBuilder db = null;
        try {
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        if (doc != null) {
            doc = db.parse(new File("res/map/level/lvl_forest.tmx"));
        }
    }

    public XmlParser(DocumentBuilderFactory dbf, String pathName) throws IOException, SAXException {
        // f.setValidating(false); // не делать проверку валидации
        DocumentBuilder db = null;
        try {
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        if (db != null) {
            doc = db.parse(new File(pathName));
        }
        System.out.println(doc.getStrictErrorChecking());
    }

    public ArrayList<String> getSpriteSheetPaths() {
        ArrayList<String> spriteSheetPaths = new ArrayList<>();

        return spriteSheetPaths;
    }

    public ArrayList<String> getLayerTextContextByName(String layerName) {
        ArrayList<String> layerTextContextByName = new ArrayList<>();
        try {
//            System.out.println(doc.getElementsByTagName("layer"));
            NodeList layers = doc.getElementsByTagName("layer");
            for (int i = 0; i < layers.getLength(); ++i) {
//                System.out.println(layers.item(i).getAttributes());
                NamedNodeMap attrs = layers.item(i).getAttributes();
                Node name = attrs.getNamedItem("name");
                //System.out.println(name.getNodeValue());
                String is_gr = name.getNodeValue();
                if (is_gr.equals(layerName)) {
//                System.out.println(layers.item(i).getTextContent());
                    String strContext = layers.item(i).getTextContent();
                    layerTextContextByName.addAll(Arrays.asList(strContext.split("\\D")));
                    for (int j = 0, size = layerTextContextByName.size(); j < size; ++j) {
                        if (layerTextContextByName.get(j).equals("")) {
                            layerTextContextByName.remove(j);
                            --j;
                            --size;
                        }
                    }
                    return layerTextContextByName;
                }
            }
        } catch (Exception e) {
            System.out.println("Parsing: getLayerTextContextByName() " + e);
        }
        return layerTextContextByName;
    }

    public ArrayList<ITileProperties> getTilesPropertiesByTilesetName(String tilesetName, String ITilePropertyClassName) {
        ArrayList<ITileProperties> tilesProperties = new ArrayList<>();
        try {
//            System.out.println(doc.getElementsByTagName("tileset"));
            NodeList tilesets = doc.getElementsByTagName("tileset");
            for (int i = 0; i < tilesets.getLength(); ++i) {
                NamedNodeMap tilesetAttrs = tilesets.item(i).getAttributes();
                if (getValueOfNamedItem(tilesetAttrs, "name").equals(tilesetName)) {
                    for (int j = 0; j < tilesets.item(i).getChildNodes().getLength(); ++j) {
                        if (tilesets.item(i).getChildNodes().item(j).getNodeName().equals("tile")) {
                            NodeList properties = tilesets.item(i).getChildNodes().item(j).getChildNodes()
                                    .item(1).getChildNodes(); // NodeList of property at .tmx
                            ITileProperties prop = null;
                            switch (ITilePropertyClassName) {
                                case "tile":
                                    prop = getTileProperties(properties);
                                    break;
                                case "objTile":
                                    prop = getObjTileProperties(properties);
                                    break;
                                default:
                                    System.out.println("Check ITileProperties properties.");
                                    break;
                            }
                            tilesProperties.add(prop);
                        }
                    }
                    return tilesProperties;
                }
            }
        } catch (Exception e) {
            System.out.println("Parsing: getLayerTextContextByName() " + e);
        }
        return tilesProperties;
    }

    private TileProperties getTileProperties(NodeList properties) {
        TileProperties prop = new TileProperties();
        for (int k = 0; k < properties.getLength(); ++k) {
            if (properties.item(k).getNodeName().equals("property")) {
                NamedNodeMap propAttrs = properties.item(k).getAttributes();
                switch (propAttrs.getNamedItem("name").getNodeValue()) {
                    case "is_pass":
                        String isPass = getValueOfNamedItem(propAttrs, "value");
                        if (isPass.equals("true")) {
                            prop.setPass(true);
                        }
                        break;
                    case "layout":
                        String layer = getValueOfNamedItem(propAttrs, "value");
                        prop.setLayer(Integer.parseInt(layer));
                        break;
                    case "isFly":
                        String isFly = getValueOfNamedItem(propAttrs, "value");
                        if (isFly.equals("true")) {
                            prop.setFly(true);
                        }
                        break;
                    default:
                        System.out.println("Check TileProperty attributes. attr = "
                                + propAttrs.getNamedItem("name").getNodeValue());
                        break;
                }
            }
        }
        return prop;
    }

    private ObjTileProperties getObjTileProperties(NodeList properties) {
        ObjTileProperties prop = new ObjTileProperties();
        for (int k = 0; k < properties.getLength(); ++k) {
            if (properties.item(k).getNodeName().equals("property")) {
                NamedNodeMap propAttrs = properties.item(k).getAttributes();
                switch (getValueOfNamedItem(propAttrs, "name")) {
                    case "is_pass":
                        String isPass = getValueOfNamedItem(propAttrs, "value");
                        if (isPass.equals("true")) {
                            prop.setPass(true);
                        }
                        break;
                    case "isFly":
                        String isFly = getValueOfNamedItem(propAttrs, "value");
                        if (isFly.equals("true")) {
                            prop.setFly(true);
                        }
                        break;
                    default:
                        System.out.println("Check ObjTileProperty attributes. attr = "
                                + propAttrs.getNamedItem("name").getNodeValue());
                        break;
                }
            }
        }
        return prop;
    }

    public ArrayDeque<GroupObject> getGroupObjectsByGroupName(String gameObjects, boolean byTemplate, int firstId) {
        ArrayDeque<GroupObject> waitedGroupObjects = new ArrayDeque<>();
        try {
            System.out.println(doc.getElementsByTagName("objectgroup"));
            NodeList checkingObjectGroups = doc.getElementsByTagName("objectgroup");
            for (int i = 0; i < checkingObjectGroups.getLength(); ++i) {
                NamedNodeMap objGrAttrs = checkingObjectGroups.item(i).getAttributes();
                if (getValueOfNamedItem(objGrAttrs, "name").equals(gameObjects)) {
                    waitedGroupObjects = getGroupObjects(checkingObjectGroups.item(i).getChildNodes(),
                            "object", byTemplate, firstId).clone();
                }
            }
            return waitedGroupObjects;
        } catch (Exception e) {
            System.out.println("Parsing: getGroupObjectsByGroupName() " + e);
        }
        return waitedGroupObjects;
    }

    private ArrayDeque<GroupObject> getGroupObjects(NodeList nodeListOfObjects, String name, boolean byTemplate,
                                                    int firstId) {
        ArrayDeque<GroupObject> itemGroupObjects = new ArrayDeque<>();
        for (int i = 0; i < nodeListOfObjects.getLength(); ++i) {
            if (nodeListOfObjects.item(i).getNodeName().equals(name)) {
                NamedNodeMap objGrAttrs = nodeListOfObjects.item(i).getAttributes();
                // TODO Add constant sprWidth, sprHeight
                GroupObject insGrObj;
                if (byTemplate) {
                    insGrObj = new GroupObject(
                            Integer.parseInt(getValueOfNamedItem(objGrAttrs, "x")) / 32,
                            Integer.parseInt(getValueOfNamedItem(objGrAttrs, "y")) / 32,
                            getValueOfNamedItem(objGrAttrs, "type"),
                            getValueOfNamedItem(objGrAttrs, "name"),
                            new GroupObjectProperties());
                } else {
                    insGrObj = new GroupObject(
                            Integer.parseInt(getValueOfNamedItem(objGrAttrs, "x")) / 32,
                            Integer.parseInt(getValueOfNamedItem(objGrAttrs, "y")) / 32,
                            Integer.parseInt(getValueOfNamedItem(objGrAttrs, "width")) / 32,
                            Integer.parseInt(getValueOfNamedItem(objGrAttrs, "height")) / 32,
                            getValueOfNamedItem(objGrAttrs, "type"),
                            getValueOfNamedItem(objGrAttrs, "name"),
                            firstId + Integer.parseInt(getValueOfNamedItem(objGrAttrs, "id")));
                }
                itemGroupObjects.addLast(insGrObj);
            }
        }
        return itemGroupObjects;
    }

    private String getValueOfNamedItem(NamedNodeMap objGrAttrs, String namedItem) {
        return objGrAttrs.getNamedItem(namedItem).getNodeValue();
    }

    public int getMapSize(String param) {
        int retValue = Integer.MIN_VALUE;
        try {
            NodeList maps = doc.getElementsByTagName("map");
            if (maps.getLength() != 1) {
                System.err.println("Only one map for .tmx file!");
            }
            retValue = Integer.parseInt(maps.item(0).getAttributes().getNamedItem(param).getNodeValue());
        } catch (Exception e) {
            System.out.println("Parsing: getMapSize() " + e);
        }
        return retValue;
    }

    public ArrayList<ObjectSheetResource> getObjectsSheet(String gameObjects) {
        ArrayList<ObjectSheetResource> objectsSheet = new ArrayList<>();
        try {
            System.out.println(doc.getElementsByTagName("objectgroup"));
            NodeList checkingObjectGroups = doc.getElementsByTagName("objectgroup");
            for (int i = 0; i < checkingObjectGroups.getLength(); ++i) {
                NamedNodeMap objGrAttrs = checkingObjectGroups.item(i).getAttributes();
                if (getValueOfNamedItem(objGrAttrs, "name").equals(gameObjects)) {
                    NodeList nodeListOfObjects = checkingObjectGroups.item(i).getChildNodes();
                    for (int j = 0; j < nodeListOfObjects.getLength(); ++j) {
                        if (nodeListOfObjects.item(j).getNodeName().equals("object")) {
                            NamedNodeMap objAttrs = nodeListOfObjects.item(j).getAttributes();
                            // TODO Add constant sprWidth, sprHeight
                            ObjectSheetResource objSheetRes = new ObjectSheetResource(
                                    Integer.parseInt(getValueOfNamedItem(objAttrs, "id")),
                                    Integer.parseInt(getValueOfNamedItem(objAttrs, "x")),
                                    Integer.parseInt(getValueOfNamedItem(objAttrs, "y")),
                                    Integer.parseInt(getValueOfNamedItem(objAttrs, "width")),
                                    Integer.parseInt(getValueOfNamedItem(objAttrs, "height")));
                            objectsSheet.add(objSheetRes);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Parsing: getGroupObjectsByGroupName() " + e);
        }
        return objectsSheet;
    }
}
