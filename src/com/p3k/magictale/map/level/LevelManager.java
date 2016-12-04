package com.p3k.magictale.map.level;

import com.p3k.magictale.engine.graphics.Sprite;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;

public class LevelManager implements Level {
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

    public Sprite[][] getSprites() {
        Sprite[][] arraySprite = new Sprite[16][9];
        try {
            new LevelParser(dbf, "res/map/level/lvl_forest.tmx", "data");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

        return arraySprite;
    }
}
