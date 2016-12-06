package com.p3k.magictale.map.level;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import com.p3k.magictale.engine.graphics.Sprite;
import com.sun.deploy.util.ArrayUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
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
        spriteSheetPaths.add(doc.getNodeName());

        return spriteSheetPaths;
    }

    public ArrayList<Sprite> getSprites() {
        ArrayList<Sprite> sprites = new ArrayList<>();
        System.out.println(doc.getClass().getName());

        return sprites;
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