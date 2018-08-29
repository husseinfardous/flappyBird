package com.husseinfardous.flappybird.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import java.util.Random;

public class Tube {

    public static final int TUBE_WIDTH = 52; // width of a tube
    private static final int FLUCTUATION = 130; // so a number between 0 and 130 is generated randomly
    private static final int TUBE_GAP = 100; // space between top and bottom tubes
    private static final int LOWEST_OPENING = 120; // minimum height that top tube must be above
    private Texture topTube, bottomTube;
    private Vector2 posTopTube, posBotTube;
    private Random rand;
    private Rectangle boundsTop, boundsBot;
    private Rectangle tubeGap;

    public Tube(float x) {
        topTube = new Texture("toptube.png");
        bottomTube = new Texture("bottomtube.png");
        rand = new Random();
        posTopTube = new Vector2(x, rand.nextInt(FLUCTUATION) + TUBE_GAP + LOWEST_OPENING);
        posBotTube = new Vector2(x, posTopTube.y - TUBE_GAP - bottomTube.getHeight());
        boundsTop = new Rectangle(posTopTube.x, posTopTube.y, topTube.getWidth(), topTube.getHeight());
        boundsBot = new Rectangle(posBotTube.x, posBotTube.y, bottomTube.getWidth(), bottomTube.getHeight());
        tubeGap = new Rectangle(posTopTube.x + topTube.getWidth(), posTopTube.y - 100, 1, TUBE_GAP);
    }

    public Texture getTopTube() {
        return topTube;
    }

    public Texture getBottomTube() {
        return bottomTube;
    }

    public Vector2 getPosTopTube() {
        return posTopTube;
    }

    public Vector2 getPosBotTube() {
        return posBotTube;
    }

    public void reposition(float x) {
        posTopTube.set(x, rand.nextInt(FLUCTUATION) + TUBE_GAP + LOWEST_OPENING);
        posBotTube.set(x, posTopTube.y - TUBE_GAP - bottomTube.getHeight());
        boundsTop.setPosition(posTopTube.x, posTopTube.y);
        boundsBot.setPosition(posBotTube.x, posBotTube.y);
        tubeGap.setPosition(posTopTube.x + topTube.getWidth(), posTopTube.y - 100);
    }

    public boolean collides(Rectangle player) {
        return (player.overlaps(boundsTop) || player.overlaps(boundsBot));
    }

    public boolean scored(Rectangle player) {
        return player.overlaps(tubeGap);
    }

    public Rectangle getTubeGap() {
        return tubeGap;
    }

    public void dispose() {
        topTube.dispose();
        bottomTube.dispose();
    }
}
