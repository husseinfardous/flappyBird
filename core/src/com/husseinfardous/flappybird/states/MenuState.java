package com.husseinfardous.flappybird.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.husseinfardous.flappybird.FlappyBird;

public class MenuState extends State {

    private Texture background, playBtn, bird;
    private BitmapFont font;
    private int highscore;

    public MenuState(GameStateManager gsm) {
        super(gsm);
        cam.setToOrtho(false, FlappyBird.WIDTH / 2, FlappyBird.HEIGHT / 2);

        background = new Texture("bg.png");
        playBtn = new Texture("playbtn.png");
        bird = new Texture("bird.png");
        font = new BitmapFont();

        Preferences prefs = Gdx.app.getPreferences("savedFlappyBirdData");
        this.highscore = prefs.getInteger("highscore", 0);
    }

    @Override
    public void handleInput() {
        if(Gdx.input.justTouched()) {
            gsm.set(new PlayState(gsm));
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        sb.begin();

        sb.draw(background, 0, 0);
        sb.draw(bird, cam.position.x - (bird.getWidth() / 2), cam.position.y + (cam.position.y / 2));
        sb.draw(playBtn, cam.position.x - (playBtn.getWidth() / 2), cam.position.y - (cam.position.y / 6));

        font.setColor(Color.ORANGE);
        font.getData().setScale(1);

        GlyphLayout highscoreLayout = new GlyphLayout(font, "Highscore: " + highscore);
        font.draw(sb, highscoreLayout, cam.position.x - (highscoreLayout.width / 2), cam.position.y + (cam.position.y / 3));

        sb.end();
    }

    @Override
    public void dispose() {
        background.dispose();
        playBtn.dispose();
        bird.dispose();
        font.dispose();
        System.out.println("Menu State Disposed.");
    }
}
