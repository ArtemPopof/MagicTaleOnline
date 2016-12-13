package com.p3k.magictale.map.level;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringJoiner;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import com.p3k.magictale.engine.graphics.Sprite;
import com.p3k.magictale.engine.graphics.TileProperties;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

/**
 * Created by COMar-PC on 04.12.2016.
 */
public class XmlParser {
    private Document doc = null;

    public XmlParser() throws IOException, SAXException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance(); //создали фабрику строителей, сложный и грамосткий процесс (по реже выполняйте это действие)
        // f.setValidating(false); // не делать проверку валидации
        DocumentBuilder db = null; // создали конкретного строителя документа
        try {
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        if (doc != null) {
            doc = db.parse(new File("res/map/level/lvl_forest.tmx"));
        }
        // стооитель построил документ
//        visit(doc, 0);
    }

    public XmlParser(DocumentBuilderFactory dbf, String pathName) throws IOException, SAXException {
        // f.setValidating(false); // не делать проверку валидации
        DocumentBuilder db = null; // создали конкретного строителя документа
        try {
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        if (db != null) {
            doc = db.parse(new File(pathName));
        }
//        if (doc != null) {
//            visit(doc, 0);
//            System.out.println("el = " + doc.getElementsByTagName("data"));
//        }
        System.out.println(doc.getStrictErrorChecking());
    }

    public ArrayList<String> getSpriteSheetPaths() {
        ArrayList<String> spriteSheetPaths = new ArrayList<>();

        return spriteSheetPaths;
    }

    public ArrayList<String> getLayerTextContextByName(String layerName) {
        ArrayList<String> layerTextContextByName = new ArrayList<>();
        try {
            System.out.println(doc.getElementsByTagName("layer"));
            NodeList layers = doc.getElementsByTagName("layer");
            for (int i = 0; i < layers.getLength(); ++i) {
                System.out.println(layers.item(i).getAttributes());
                NamedNodeMap attrs = layers.item(i).getAttributes();
                Node name = attrs.getNamedItem("name");
                System.out.println(name.getNodeValue());
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
        } catch (Exception e){
            System.out.println("Parsing: getLayerTextContextByName() " + e);
        }
        return layerTextContextByName;
    }

    public ArrayList<TileProperties> getTilesPropertiesByTilesetName(String tilesetName) {
        ArrayList<TileProperties> tilesProperties = new ArrayList<>();
        try {
            System.out.println(doc.getElementsByTagName("tileset"));
            NodeList tilesets = doc.getElementsByTagName("tileset");
            for (int i = 0; i < tilesets.getLength(); ++i) {
                NamedNodeMap tilesetAttrs = tilesets.item(i).getAttributes();
                if (tilesetAttrs.getNamedItem("name").getNodeValue().equals(tilesetName)) {
                    for (int j = 0; j < tilesets.item(i).getChildNodes().getLength(); ++j) {
                        if (tilesets.item(i).getChildNodes().item(j).getNodeName().equals("tile")) {
                            NodeList properties = tilesets.item(i).getChildNodes().item(j).getChildNodes()
                                    .item(1).getChildNodes(); // NodeList of property at .tmx
                            TileProperties prop = new TileProperties();
                            for (int k = 0; k < properties.getLength(); ++k) {
                                if (properties.item(k).getNodeName().equals("property")) {
                                    NamedNodeMap propAttrs = properties.item(k).getAttributes();
                                    switch (propAttrs.getNamedItem("name").getNodeValue()) {
                                        case "is_pass":
                                            String bool = propAttrs.getNamedItem("value").getNodeValue();
                                            if (bool.equals("true")) {
                                                prop.setPass(true);
                                            }
                                            break;
                                        case "layout":
                                            String layout = propAttrs.getNamedItem("value").getNodeValue();
                                            prop.setLayer(Integer.parseInt(layout));
                                            break;
                                        default:
                                            System.out.println("Check Property attributes. attr = "
                                                    + propAttrs.getNamedItem("name").getNodeValue());
                                            break;
                                    }
                                }

                            }
                            tilesProperties.add(prop);
                        }
                    }
                    return tilesProperties;
                }
            }
        } catch (Exception e){
            System.out.println("Parsing: getLayerTextContextByName() " + e);
        }
        return tilesProperties;
    }

    public int getMapSize(String param) {
        int retValue = Integer.MIN_VALUE;
        try {
            NodeList maps = doc.getElementsByTagName("map");
            if (maps.getLength() != 1) {
                System.err.println("Only one map for .tmx file!");
            }
            retValue = Integer.parseInt(maps.item(0).getAttributes().getNamedItem(param).getNodeValue());
        } catch (Exception e){
            System.out.println("Parsing: getWidth() " + e);
        }
        return retValue;
    }

    private static void visit(Node node, int level) {
        NodeList list = node.getChildNodes();
        for (int i = 0; i < list.getLength(); ++i) {
            Node childNode = list.item(i); // текущий нод
            process(childNode, level + 1); // обработка
            visit(childNode, level + 1); // рекурсия
        }
    }

    private static void process(Node node, int level) {
        for (int i = 0; i < level; i++) {
            System.out.print('\t');
        }
        System.out.print(node.getNodeName());
        System.out.print('\t' + node.getNodeValue());
        System.out.print(" Attrs: " + node.getTextContent());
        if (node instanceof Element){
            Element e = (Element) node;
            // работаем как с элементом (у него есть атрибуты и схема)
        }
        System.out.println();
    }
}