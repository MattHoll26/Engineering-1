package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * EXTENDED JAVADOC
 * <code>TutorialScreen</code> implements the tutorial screen shown
 * before the main game begins. It displays a full-screen tutorial image and
 * lets the player continue to the name entry screen.
 *
 * <p>Controls:</p>
 * <ul>
 * <li>SPACE --> Continue to {@link NameScreen}</li>
 * <li>ESC --> Exit the game</li>
 * </ul>
 *
 * @see com.badlogic.gdx.Screen Screen
 */
public class TutorialScreen implements Screen {
    private final MyGame game;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Texture tutorialImage;
    private FitViewport viewport;

    private final int MENU_WIDTH = 790;
    private final int MENU_HEIGHT = 480;

    /**
     * EXTENDED JAVADOC
     * Constructor for <code>TutorialScreen</code>, creating the rendering
     * objects and loading the tutorial screen image.
     * @param game Game controller used to swap between screens.
     */
    public TutorialScreen(MyGame game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, MENU_WIDTH, MENU_HEIGHT);
        batch = new SpriteBatch();

        //load the tutorial screen image
        tutorialImage = new Texture("How to Play Screen.png");

        viewport = new FitViewport(MENU_WIDTH, MENU_HEIGHT, camera);
    }

    /**
     * EXTENDED
     * Render the tutorial screen and process key input for moving to the next screen.
     * @param delta Time in seconds since the last frame finished rendering.
     * @see com.badlogic.gdx.Screen#render Screen.render().
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        //draw the image to fill the screen
        batch.draw(tutorialImage, 0, 0, MENU_WIDTH, MENU_HEIGHT);
        batch.end();

        //once space bar is pressed load the maze game
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            // MODIFIED: Changed transition to NameScreen instead of GameScreen
            game.setScreen(new NameScreen(game));
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit(); //user can go back to the start menu if they choose
        }
    }

    /**
     * EXTENDED JAVADOC
     * Resize the viewport when the window size changes.
     * @param width Current width of the window.
     * @param height Current height of the window.
     * @see com.badlogic.gdx.Screen#resize Screen.resize().
     */
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    /**
     * EXTENDED JAVADOC
     * Dispose of assets used by this screen when it is no longer needed.
     * @see com.badlogic.gdx.Screen#dispose Screen.dispose().
     */
    @Override
    public void dispose() {
        batch.dispose();
        tutorialImage.dispose();
    }

    /** Unimplemented */
    @Override
    public void show(){}

    /** Unimplemented */
    @Override
    public void pause(){}

    /** Unimplemented */
    @Override
    public void resume(){}

    /** Unimplemented */
    @Override
    public void hide(){}





}
