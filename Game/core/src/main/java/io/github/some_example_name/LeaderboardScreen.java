package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

/**
 * NEW
 *
 * <code> LeaderboardScreen </code> implements a screen that displays the top
 * scores stored in the local leaderboard file.
 * The player can return to the main menu or quit the game from this screen.
 *
 * @see com.badlogic.gdx.Screen Screen
 */
public class LeaderboardScreen implements Screen {
    private final MyGame game;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private BitmapFont font;
    private Save_Leaderboard leaderboard;
    private final int MENU_WIDTH = 640;
    private final int MENU_HEIGHT = 480;

    /**
     * Constructor for <code> LeaderboardScreen </code>, creating the camera,
     * render assets, and loading the stored leaderboard scores.
     *
     * @param game The Game creator which is used to switch screens.
     */
    public LeaderboardScreen(MyGame game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, MENU_WIDTH, MENU_HEIGHT);
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.getData().setScale(1f);
        leaderboard = new Save_Leaderboard();
    }

    /**
     * Render the leaderboard screen and process user input.
     * Displays the top 5 scores currently stored and provides controls
     * to return to the main menu or quit.
     *
     * @param delta Time in seconds since last frame finished rendering.
     * @see com.badlogic.gdx.Screen#render(float) Screen.render()
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        font.draw(batch, "LEADERBOARD --> TOP 5", 150, 450);
        font.draw(batch, "SPACE or ENTER --> Back to Menu | ESC --> Quit", 100, 420);

        Array<Save_Leaderboard.ScoreEntry> highScores = leaderboard.getHighScores();
        for (int i = 0; i < highScores.size && i < 5; i++) {
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

    /**
     * Dispose of leaderboard screen assets when the screen is exited.
     *
     * @see com.badlogic.gdx.Screen#dispose() Screen.dispose()
     */
    @Override public void dispose() {
        batch.dispose();
        font.dispose();
    }

    /** Unimplemented. */
    @Override public void show() {}
    /** Unimplemented. */
    @Override public void resize(int width, int height) {}
    /** Unimplemented. */
    @Override public void pause() {}
    /** Unimplemented. */
    @Override public void resume() {}
    /** Unimplemented. */
    @Override public void hide() {}
}
