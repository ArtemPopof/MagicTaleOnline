package com.p3k.magictale.engine.gui.fonts;

import com.p3k.magictale.engine.graphics.SpriteSheet;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

class FontInfo {
    public int size;
    public int space;
    public int charWidth;
    public int charHeight;
}

/**
 * Created by jorgen on 24.12.16.
 */
public class FontManager {

    private static final String FONT_DIR = "res/gui/fonts/";

    private static FontManager instance = null;

    private Map<String, Font> fonts;

    private FontManager() {
        fonts = new HashMap<>();
    }

    public void registerFont(String filename, String name)
            throws FileNotFoundException {

        FontInfo info = loadFontInfo(filename);

        SpriteSheet sheet = new SpriteSheet(FONT_DIR + filename,
                info.charWidth, info.charHeight, false, true);

        if ( sheet == null ) {
            throw new FileNotFoundException("Can't find file with name: "
                    + name + ".png");
        }

        fonts.put(name, new Font(name, info, sheet));
    }

    public Font getFont(String name) {
        if ( fonts.get(name) == null ) {
            throw new NoSuchElementException("No font with name '" + name + "' was loaded!");
        }

        return new Font(fonts.get(name));
    }

    private FontInfo loadFontInfo(String font) {
        FontInfo info = new FontInfo();

        font = font.split("\\.")[0] + ".font";

        try(
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                        new FileInputStream(FONT_DIR + font)))
        ) {

            HashMap<String, String> values = new HashMap<>();

            String str;
            String[] parts;
            while ( (str = in.readLine()) != null ) {
                parts = str.split("=");
                values.put(parts[0].trim(), parts[1].trim());
            }

            info.size = Integer.parseInt(values.get("size"));
            info.space = Integer.parseInt(values.get("space"));
            info.charHeight = Integer.parseInt(values.get("charHeight"));
            info.charWidth = Integer.parseInt(values.get("charWidth"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return info;
    }

    static public FontManager getInstance() {
        if ( instance == null ) {
            instance = new FontManager();
        }

        return instance;
    }
}
