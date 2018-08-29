package com.husseinfardous.flappybird.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.husseinfardous.flappybird.FlappyBird;

public class GameOverState extends State {

    private Texture background, gameover;
    private int score, highscore;
    private BitmapFont font;
    private TextButton tryAgainButton, menuButton;
    private TextButton.TextButtonStyle buttonStyle;
    private TextureAtlas buttonAtlas;
    private Skin buttonSkin;
    private Stage stage;

    public GameOverState(GameStateManager gsm, int score) {
        super(gsm);
        this.score = score;
        cam.setToOrtho(false, FlappyBird.WIDTH / 2, FlappyBird.HEIGHT / 2);

        background = new Texture("bg.png");
        gameover = new Texture("gameover.png");
        font = new BitmapFont();

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        buttonSkin = new Skin();
        buttonAtlas = new TextureAtlas(Gdx.files.internal("uiskin.atlas"));
        buttonSkin.addRegions(buttonAtlas);
        buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;
        buttonStyle.fontColor = Color.RED;
        buttonStyle.up = buttonSkin.getDrawable("default-round");
        buttonStyle.down = buttonSkin.getDrawable("default-round-down");

        tryAgainButton = new TextButton("Try Again", buttonStyle);
        tryAgainButton.setSize(Gdx.graphics.getHeight() / 10, Gdx.graphics.getHeight() / 10);
        tryAgainButton.getLabel().setFontScale(3);
        tryAgainButton.setPosition((Gdx.graphics.getWidth() / 2) - (tryAgainButton.getWidth() / 2), (Gdx.graphics.getHeight() / 3));
        stage.addActor(tryAgainButton);

        menuButton = new TextButton("Menu", buttonStyle);
        menuButton.setSize(Gdx.graphics.getHeight() / 10, Gdx.graphics.getHeight() / 10);
        menuButton.getLabel().setFontScale(3);
        menuButton.setPosition((Gdx.graphics.getWidth() / 2) - (menuButton.getWidth() / 2), (Gdx.graphics.getHeight() / 6));
        stage.addActor(menuButton);

        Preferences prefs = Gdx.app.getPreferences("savedFlappyBirdData");
        this.highscore = prefs.getInteger("highscore", 0);

        if (score > highscore) {
            prefs.putInteger("highscore", score);
            prefs.flush();
        }
    }

    @Override
    public void handleInput() {
       if (tryAgainButton.isPressed()) {
           gsm.set(new PlayState(gsm));
       }

       if (menuButton.isPressed()) {
           gsm.set(new MenuState(gsm));
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
        sb.draw(gameover, cam.position.x - (gameover.getWidth() / 2), cam.position.y + (cam.position.y / 2));

        font.setColor(Color.RED);
        font.getData().setScale(1);

        GlyphLayout highscoreLayout = new GlyphLayout(font, "HighScore: " + highscore);
        GlyphLayout scoreLayout = new GlyphLayout(font, "Score: " + score);
        font.draw(sb, highscoreLayout, cam.position.x - (highscoreLayout.width / 2), cam.position.y + (cam.position.y / 3));
        font.draw(sb, scoreLayout, cam.position.x - (scoreLayout.width / 2), cam.position.y + (cam.position.y / 6));

        sb.end();
        stage.draw();
    }

    @Override
    public void dispose() {
        background.dispose();
        gameover.dispose();
        font.dispose();
        buttonAtlas.dispose();
        buttonSkin.dispose();
        stage.dispose();
        System.out.println("GameOver State Disposed.");
    }
}
