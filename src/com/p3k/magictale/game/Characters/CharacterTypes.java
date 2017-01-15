package com.p3k.magictale.game.Characters;

import java.io.Serializable;

/**
 * All character types in game
 * if Mage character has Swordman type
 * then all animations and other stuff will be
 * for swordman, so be careful...
 *
 * Created by artem96 on 06.12.16.
 */
public enum CharacterTypes implements Serializable {

    ABSTRACT_CHARACTER,
    ABSTRACT_PLAYER,
    ABSTRACT_BOT,
    BAT_BOT,
}
