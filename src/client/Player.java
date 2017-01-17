package client;

import com.p3k.magictale.engine.graphics.Sprite;

/**
 * Created by jorgen on 16.01.17.
 */
public class Player {
    private int currentHealth;
    private int maxHealth;
    private float speed;
    private int attack;
    private boolean isDead;
    private int currentLevel;
    private int xpForNextLevel;
    private int xpForPrevLevel;
    private int xp;
    private long timestamp = 0;
    private Sprite sprite = new Sprite(0, 0, 0, 10, 10);

    public int getCurrentHealth() {
        return currentHealth;
    }

    public void setCurrentHealth(int currentHealth) {
        this.currentHealth = currentHealth;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }

    public void setDead(byte dead) {
        isDead = dead != 0;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public int getXpForNextLevel() {
        return xpForNextLevel;
    }

    public void setXpForNextLevel(int xpForNextLevel) {
        this.xpForNextLevel = xpForNextLevel;
    }

    public int getXpForPrevLevel() { return (currentLevel - 1)*10; }

    public void setXpForPrevLevel(int xpForPrevLevel) {
        this.xpForPrevLevel = xpForPrevLevel;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        if (sprite != null) {
            this.sprite = sprite;
//            System.out.println("Sprite updated: " + sprite.getTextureId());
        }
    }
}
