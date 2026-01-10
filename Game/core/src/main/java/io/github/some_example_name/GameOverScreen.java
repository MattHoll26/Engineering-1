package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * UNCHANGED
 *
 * <code> GameOverScreen </code> implements a screen that appears when the timer runs out
 * @see com.badlogic.gdx.Screen Screen
 */

public class GameOverScreen implements Screen {
    private final MyGame game;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private BitmapFont font;

    /**
     * EXTENDED JAVADOC
     * Constructor for <code>GameOverScreen</code>, using the game creator
     * in <code>MyGame</code> to create the game over screen.
     * @param game Game creator.
     */
    public GameOverScreen(MyGame game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 640, 480);

        batch = new SpriteBatch();
        font = new BitmapFont();
        font.getData().setScale(2.5f);
    }

    /**
     * EXTENDed JAVADOC
     * Process input then render thew new frame for the game over screen
     * @param delta Time in seconds since the last frame finished rendering
     * @see com.badlogic.gdx.Screen#render Screen.render().
     */

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.3f, 0.1f, 0.1f, 1); //a red colour for the background as it is game over
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        font.draw(batch, "You lost!", 255, 350);
        font.draw(batch, "Press SPACE to continue", 135, 250);
        batch.end();

        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            game.setScreen(new MenuScreen(game));
        }
    }

    /**
     * EXTENDED JAVADOC
     * Dispose game over screen assists when the screen is exited
     * @see com.badlogic.gdx.Screen#dispose Screen.dispose()
     */
    @Override
    public void dispose(){
        batch.dispose();;
        font.dispose();
    }

    /**
     * Unimplemented
     */
    @Override
    public void show() {}

     /**
     * Unimplemented
     */
    @Override
    public void resize(int width, int height){}

     /**
     * Unimplemented
     */
    @Override
    public void pause() {}

     /**
     * Unimplemented
     */
    @Override
    public void resume() {}

     /**
     * Unimplemented
     */
    @Override
    public void hide() {}
}

