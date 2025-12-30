package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class LeaderboardScreen implements Screen {
    private final MyGame game;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private BitmapFont font;
    private Save_Leaderboard leaderboard;
    private final int MENU_WIDTH = 640;
    private final int MENU_HEIGHT = 480;

    public LeaderboardScreen(MyGame game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, MENU_WIDTH, MENU_HEIGHT);
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.getData().setScale(1f);
        leaderboard = new Save_Leaderboard();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        font.draw(batch, "LEADERBOARD --> TOP 10", 150, 450);
        font.draw(batch, "SPACE or ENTER --> Back to Menu | ESC --> Quit", 100, 420);

        Array<Save_Leaderboard.ScoreEntry> highScores = leaderboard.getHighScores();
        for (int i = 0; i < highScores.size && i < 10; i++) {
            Save_Leaderboard.ScoreEntry individualScore = highScores.get(i);
            font.draw(batch, (i+1) + ". " + individualScore.fullName + " - " + individualScore.score, 100, 380 - i * 30);
        }

        batch.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            game.setScreen(new MenuScreen(game));
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
    }

    @Override public void dispose() {
        batch.dispose();
        font.dispose();
    }

    // Other Screen methods empty
    @Override public void show() {

    }
    @Override public void resize(int width, int height) {

    }
    @Override public void pause() {

    }
    @Override public void resume() {

    }
    @Override public void hide() {

    }
}
