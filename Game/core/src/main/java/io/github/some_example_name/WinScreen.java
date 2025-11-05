package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * <code> WinScreen </code> implements a static screen with a congratulation message, and 
 * allows player to then quit or replay game.
 * @see {@link com.badlogic.gdx.Screen} Screen.
 */
public class WinScreen implements Screen {
	private final MyGame game;
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private BitmapFont font;

	/**
	 * Constructor for <code> WinScreen </code>, using the game creator in
	 * <code> MyGame </code> to create menu screen.
	 * @param game Game creator.
	 */
	public WinScreen(MyGame game) {
		this.game = game;

		camera = new OrthographicCamera();
		camera.setToOrtho(false, 640, 480);

		batch = new SpriteBatch();
		font = new BitmapFont();
		font.getData().setScale(2.5f);
	}

	/**
	 * Process input then render new frame, including dialog, for the win menu. 
	 * @param delta Time in seconds since last frame finished rendering.
	 * @see {@link com.badlogic.gdx.Screen#render} Screen.render().
	 */
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.1f, 0.3f, 0.1f, 1); // A nice green color
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		batch.setProjectionMatrix(camera.combined);

		batch.begin();
		font.draw(batch, "You Win!", 250, 350);
		font.draw(batch, "Press SPACE to return to menu", 100, 250);
		batch.end();

		if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
		    game.setScreen(new MenuScreen(game));
		}
	}

   	/**
	 * Dispose win menu assets when menu is exited or program is quit. 
	 * @see {@link com.badlogic.gdx.Screen#dispose} Screen.dispose().
	 */ 
	@Override
	public void dispose() {
		batch.dispose();
		font.dispose();
	}
	
	/** Unimplemented */
	@Override
	public void show() {}

	/** Unimplemented */
	@Override

	/** Unimplemented */
	public void resize(int width, int height) {}

	/** Unimplemented */
	@Override

	/** Unimplemented */
	public void pause() {}

	/** Unimplemented */
	@Override

	/** Unimplemented */
	public void resume() {}

	/** Unimplemented */
	@Override
	public void hide() {}
}
