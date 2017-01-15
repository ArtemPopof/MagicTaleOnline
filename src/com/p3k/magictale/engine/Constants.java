package com.p3k.magictale.engine;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.Serializable;

/**
 * Основные константы, определяющую конфигурацию движка.
 * Изменение этих настроек в основном нежелательно на
 * поздних этапах разработки.
 *
 * Для того, чтобы получить доступ к глобальным константам,
 * просто добавь implements MagicConstants к классу.
 *
 * @author Артём Попов
 * @version 0.1
 */
public interface Constants extends Serializable {

    public static final int SCREEN_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
    public static final int SCREEN_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;
    public static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();

    public static final int WINDOW_WIDTH = 800;
    public static final int WINDOW_HEIGHT = WINDOW_WIDTH * 9 / 16;
    public static final Dimension WINDOW_SIZE = new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT);

    public static final int MAX_FPS = 60;

    // each animation frame will be showed 5 times.
    public static final int FRAME_SHOW_COUNT = 5;

    // размер тайлов в пикселях
    // помимо тайлов на экране могут быть другие
    // объекты, больше по размеру, чем тайлы.
    public static final int TILE_SIZE = 32;

    // temp value
    public static final int PLAYER_SIZE = 32;

    // standart player speed
    public static final float PLAYER_SPEED = 4f;

    //Map width in tiles
    public static final int MAP_WIDTH = 48;
    public static final int MAP_HEIGHT = 33;

    public static final int MAP_TILE_SIZE = 32;

    public static final int MOUSE_BTN_LEFT = 0;
    public static final int MOUSE_BTN_RIGHT = 1;

    public static final int CLIENT_UDP_PORT = 54321;
    public static final int SERVER_UDP_PORT = 12345;
}