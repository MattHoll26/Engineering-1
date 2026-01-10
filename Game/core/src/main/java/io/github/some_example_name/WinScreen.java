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
 * EXTENDED
 *
 * <code> WinScreen </code> implements a static screen with a congratulation message, and
 * allows player to then quit or replay game.
 * @see com.badlogic.gdx.Screen Screen.
 */

public class WinScreen implements Screen {
	private final MyGame game;
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private BitmapFont font;
    private Array<Achievement> achievements;
    private int achievementYPos = 210;

	//scoring system variables
	private int finalScore;
	private int timeRemaining;
	private int totalPenalty;

    /**
     * EXTENDED
     *
     * Constructor for <code> WinScreen </code>, using the game creator in
     * <code> MyGame </code> to create menu screen.
     * @param game Game creator.
     * @param finalScore The calculated final score.
     * @param timeRemaining Time remaining in seconds.
     * @param totalPenalty The total score deduction from penalties.
     * @param achievements The list of achievements unlocked by the player.
     */
	public WinScreen(MyGame game, int finalScore, int timeRemaining, int totalPenalty, Array<Achievement> achievements) {
		this.game = game;
		this.finalScore = finalScore;
		this.timeRemaining = timeRemaining;
        this.totalPenalty = totalPenalty;
        this.achievements = achievements;

		camera = new OrthographicCamera();
		camera.setToOrtho(false, 640, 480);

		batch = new SpriteBatch();
		font = new BitmapFont();
		font.getData().setScale(1.5f);
	}

	/**
     * EXTENDED
     *
	 * Process the input and then render the new frame for the win menu.
	 * @param delta Time in seconds since last frame finished rendering.
	 * @see com.badlogic.gdx.Screen#render Screen.render().
	 */
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.1f, 0.3f, 0.1f, 1); // A nice green color
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		batch.setProjectionMatrix(camera.combined);

		batch.begin();
		font.draw(batch, "You Win!", 100, 470);
		font.draw(batch, "Final Score = " + finalScore, 100, 430);
		font.draw(batch, "Penalties = " + totalPenalty, 100, 390);
        font.draw(batch, "--- ACHIEVEMENTS UNLOCKED ---", 100, 350);

        achievementYPos = 310;
        if (achievements.size == 0) {
            font.draw(batch, "No achievements earned.", 100, 330); // If the list is empty, they unlocked nothing.
        } else for (Achievement achievement : achievements) {
            // Draw the achievement
            String achievementText = achievement.name + ": " + achievement.description + " (" + achievement.bonusScore + ")";
            font.draw(batch, achievementText, 100, achievementYPos);
            achievementYPos -= 20; // Move down
            }


		font.draw(batch, "Press SPACE to see the global leaderboard", 100, 150);
        font.draw(batch, "Press Enter to go back to the main menu", 100, 110);
        font.draw(batch, "Press Esc to QUIT", 100, 70);
		batch.end();

		if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
		    game.setScreen(new LeaderboardScreen(game));
		}
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            game.setScreen(new MenuScreen(game));
        }
        else if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
	}

   	/**
     * UNCHANGED
	 * Dispose win menu assets when menu is exited or program is quit.
	 * @see com.badlogic.gdx.Screen#dispose Screen.dispose().
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
	public void resize(int width, int height) {}

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
