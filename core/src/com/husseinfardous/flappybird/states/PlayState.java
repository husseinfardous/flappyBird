package com.husseinfardous.flappybird.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Array;
import com.husseinfardous.flappybird.FlappyBird;
import com.husseinfardous.flappybird.sprites.Bird;
import com.husseinfardous.flappybird.sprites.Tube;

public class PlayState extends State {

    private static final int TUBE_SPACING = 125; // space between two consecutive set of tubes
    private static final int TUBE_COUNT = 4; // amount of tube sets
    private static final int GROUND_Y_OFFSET = -50; // y position of ground
    public static boolean isPaused = false;
    private Bird bird;
    private Texture bg, ground;
    private Array<Tube> tubes;
    private Vector2 groundPos1, groundPos2;
    private int score;
    private BitmapFont font;
    private TextButton pauseButton;
    private TextButton.TextButtonStyle pauseButtonStyle;
    private TextureAtlas pauseButtonAtlas;
    private Skin pauseButtonSkin;
    private Stage stage;

    public PlayState(GameStateManager gsm) {
        super(gsm);
        score = 0;
        font = new BitmapFont();
        bird = new Bird(50, 300);
        cam.setToOrtho(false, FlappyBird.WIDTH / 2, FlappyBird.HEIGHT / 2);

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        pauseButtonSkin = new Skin();
        pauseButtonAtlas = new TextureAtlas(Gdx.files.internal("uiskin.atlas"));
        pauseButtonSkin.addRegions(pauseButtonAtlas);
        pauseButtonStyle = new TextButton.TextButtonStyle();
        pauseButtonStyle.font = font;
        pauseButtonStyle.fontColor = Color.RED;
        pauseButtonStyle.up = pauseButtonSkin.getDrawable("default-round");
        pauseButtonStyle.down = pauseButtonSkin.getDrawable("default-round-down");
        pauseButton = new TextButton("Pause", pauseButtonStyle);
        pauseButton.setSize(Gdx.graphics.getHeight() / 10, Gdx.graphics.getHeight() / 10);
        pauseButton.getLabel().setFontScale(3);
        pauseButton.setPosition((Gdx.graphics.getWidth() / 2) - (pauseButton.getWidth() / 2), 0);
        stage.addActor(pauseButton);

        bg = new Texture("bg.png");
        ground = new Texture("ground.png");
        groundPos1 = new Vector2(cam.position.x - (cam.viewportWidth / 2), GROUND_Y_OFFSET);
        groundPos2 = new Vector2((cam.position.x - (cam.viewportWidth / 2)) + ground.getWidth(), GROUND_Y_OFFSET);
        tubes = new Array<Tube>();
        for (int i = 1; i <= TUBE_COUNT; i++) {
            tubes.add(new Tube(i * (TUBE_SPACING + Tube.TUBE_WIDTH)));
        }
    }

    @Override
    public void handleInput() {
        if (pauseButton.isPressed()) {
            gsm.push(new PauseState(gsm, score, this));
        }

        if(Gdx.input.justTouched()) {
            bird.jump();
        }
    }

    @Override
    public void update(float dt) {
        if (!isPaused) {
            handleInput();
            updateGround();
            bird.update(dt);
            cam.position.x = bird.getPosition().x + 80;

            for (int i = 0; i < tubes.size; i++) {
                Tube tube = tubes.get(i);

                if (cam.position.x - (cam.viewportWidth / 2) > tube.getPosTopTube().x + tube.getTopTube().getWidth()) {
                    tube.reposition(tube.getPosTopTube().x + ((Tube.TUBE_WIDTH + TUBE_SPACING) * TUBE_COUNT));
                }

                if (tube.collides(bird.getBounds())) {
                    gsm.set(new GameOverState(gsm, score));
                }

                if (tube.scored(bird.getBounds())) {
                    tube.getTubeGap().setPosition(0, 0);
                    score++;
                }
            }

            if (bird.getPosition().y <= ground.getHeight() + GROUND_Y_OFFSET) {
                gsm.set(new GameOverState(gsm, score));
            }
            cam.update();
        }

        if (Gdx.input.justTouched()) {
            isPaused = false;
        }
    }

    private void updateGround() {
        if (cam.position.x - (cam.viewportWidth / 2) > groundPos1.x + ground.getWidth()) {
            groundPos1.add(ground.getWidth() * 2, 0);
        }

        if (cam.position.x - (cam.viewportWidth / 2) > groundPos2.x + ground.getWidth()) {
            groundPos2.add(ground.getWidth() * 2, 0);
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        sb.begin();

        sb.draw(bg, cam.position.x - (cam.viewportWidth / 2), 0);
        sb.draw(bird.getTexture(), bird.getPosition().x, bird.getPosition().y);

        for (Tube tube : tubes) {
            sb.draw(tube.getTopTube(), tube.getPosTopTube().x, tube.getPosTopTube().y);
            sb.draw(tube.getBottomTube(), tube.getPosBotTube().x, tube.getPosBotTube().y);
        }

        sb.draw(ground, groundPos1.x, groundPos1.y);
        sb.draw(ground, groundPos2.x, groundPos2.y);

        font.setColor(Color.WHITE);
        font.getData().setScale(1);

        GlyphLayout scoreLayout = new GlyphLayout(font, Integer.toString(score));
        font.draw(sb, scoreLayout, cam.position.x - (scoreLayout.width / 2), cam.position.y + 170);

        if (isPaused) {
            font.setColor(Color.RED);
            GlyphLayout resumeLayout = new GlyphLayout(font, "Touch the Screen");
            font.draw(sb, resumeLayout, cam.position.x - (resumeLayout.width / 2), cam.position.y + 130);
        }

        sb.end();
        stage.draw();
    }

    @Override
    public void dispose() {
        bg.dispose();
        bird.dispose();
        ground.dispose();
        font.dispose();
        pauseButtonAtlas.dispose();
        pauseButtonSkin.dispose();
        stage.dispose();

        for (Tube tube: tubes) {
            tube.dispose();
        }
        System.out.println("Play State Disposed.");
    }
}
