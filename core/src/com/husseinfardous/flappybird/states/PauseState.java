package com.husseinfardous.flappybird.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.husseinfardous.flappybird.FlappyBird;

public class PauseState extends State {

    private Texture background, bird;
    private BitmapFont font;
    private PlayState savedPlayState;
    private int score;

    public PauseState(GameStateManager gsm, int score, PlayState savedPlayState) {
        super(gsm);
        this.score = score;
        this.savedPlayState = savedPlayState;
        cam.setToOrtho(false, FlappyBird.WIDTH / 2, FlappyBird.HEIGHT / 2);

        background = new Texture("bg.png");
        bird = new Texture("bird.png");
        font = new BitmapFont();
    }

    @Override
    public void handleInput() {
        float birdXPos = cam.position.x - (bird.getWidth() / 2);
        float birdYPos = cam.position.y + (cam.position.y / 6);
        Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        cam.unproject(touchPos);

        if (Gdx.input.justTouched()) {
            if ((touchPos.x > birdXPos && touchPos.x < birdXPos + bird.getWidth()) && (touchPos.y > birdYPos && touchPos.y < birdYPos + bird.getHeight())) {
                PlayState.isPaused = true;
                gsm.set(savedPlayState);
            }
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
        sb.draw(bird, cam.position.x - (bird.getWidth() / 2), cam.position.y + (cam.position.y / 6));

        font.setColor(Color.RED);
        font.getData().setScale(1);

        GlyphLayout scoreLayout = new GlyphLayout(font, "Score: " + score);
        font.draw(sb, scoreLayout, cam.position.x - (scoreLayout.width / 2), cam.position.y);

        GlyphLayout resumeLayout = new GlyphLayout(font, "Touch the Bird to Resume");
        font.draw(sb, resumeLayout, cam.position.x - (resumeLayout.width / 2), cam.position.y - (cam.position.y / 5));

        sb.end();
    }

    @Override
    public void dispose() {
        background.dispose();
        bird.dispose();
        font.dispose();
        System.out.println("Pause State Disposed.");
    }
}
