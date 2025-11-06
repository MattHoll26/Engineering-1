package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * <code> MenuScreen </code> implements a main menu screen, to let player pause, resume
 * and quit game. 
 * @see com.badlogic.gdx.Screen Screen.
 */

public class MenuScreen implements Screen {
	private final MyGame game;
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private BitmapFont font;
	private FitViewport viewport;

	private final int MENU_WIDTH = 640;
	private final int MENU_HEIGHT = 480;


	/**
	 * Constructor for <code> MenuScreen </code>, using the game creator in
	 * <code> MyGame </code> to create menu screen.
	 * @param game Game creator.
	 */
	public MenuScreen(MyGame game) {
		this.game = game;

		camera = new OrthographicCamera();
		camera.setToOrtho(false, MENU_WIDTH, MENU_HEIGHT);

		batch = new SpriteBatch();
		font = new BitmapFont(); //uses default font of Arial
		font.getData().setScale(2f); //this makes the text bigger

		viewport = new FitViewport(MENU_WIDTH, MENU_HEIGHT, camera);
	}

	/**
	 * Show main menu screen.
	 */ 
	@Override
	public void show() {}

	/**
	 * Process input then render new frame for the main menu. 
	 * @param delta Time in seconds since last frame finished rendering.
	 * @see com.badlogic.gdx.Screen#render Screen.render().
	 */
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		batch.setProjectionMatrix(camera.combined);

		batch.begin();
		font.draw(batch, "Game Title", 200, 350);
		font.draw(batch, "Press SPACE to Start", 150, 250);
		font.draw(batch, "Press ESC to Exit", 170, 200);
		batch.end();

		if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
		    game.setScreen(new GameScreen(game));
		} else if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
		    Gdx.app.exit();
		}
	}

	/**
	 * Resize UI Viewport when the window size is changed.
	 * @param width Current width of window. 
	 * @param height Current height of window. 
	 * @see com.badlogic.gdx.Screen#resize Screen.resize().
	 */
	@Override
	public void resize(int width, int height) { viewport.update(width,height); }

	/**
	 * Dispose menu assets when menu is exited or program is quit. 
	 * @see com.badlogic.gdx.Screen#dispose Screen.dispose().
	 */
	@Override
	public void dispose() {
		batch.dispose();
		font.dispose();
	}

	/** Unimplemented */
	@Override
	public void pause() {}

	/** Unimplemented */
	@Override
	public void resume() {}

	/** Unimplemented */
	@Override
	public void hide() {}
}
