package io.github.some_example_name;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/** <code> Dean </code> is the main enemy of the game, which chases the player's
 * character to attempt to attack them, resetting them to the start of the game.
 */

public class Dean {
	private Vector2 position;
	private Vector2 startPosition;
	private Vector2 velocity;
	private Texture texture;
	private Player player;
	private GameScreen gameScreen;
	private float speed = 0.7f;

	/**
	 * Constructor for <code> Dean </code>, with a set of coordinates.
	 * @param x Horizontal position for dean to spawn in.
	 * @param y Vertical position for dean to spawn in.
	 * @param player Player class to follow.
	 * @param gameScreen Screen rendering the game.
	 */
	public Dean(float x, float y, Player player, GameScreen gameScreen){
		this.position = new Vector2(x, y);
		this.startPosition = new Vector2(x,y); //store the starting position of the dean
		this.texture = new Texture("Dean-front.png");
		this.player = player;
		this.gameScreen = gameScreen;
		this.velocity = new Vector2();
	}

    public void setSpeed(float newSpeed) {
        this.speed = newSpeed;
    }

    public float getSpeed() {
        return speed;
    }

	/**
	 * Update position of dean to get closer to player's new position.
	 * @param delta Time elapsed since last update.
	 */
	public void update(float delta) {
		Vector2 direction = new Vector2(player.getPosition()).sub(position);
		direction.nor(); //normalise

		float newX = position.x + direction.x * speed;
		float newY = position.y + direction.y * speed;

		if(!gameScreen.isCellBlockedForDean(newX, newY)) {
		    position.set(newX, newY);
		}

		tryMoveDiagonally(delta, direction);
	}

	/**
	 * Attempt to move dean diagonally towards player if moving
	 * in a straight line is not possible.
	 * @param delta Time elapsed since last update.
	 * @param direction 2D Vector for position of player.
	 */
	private void tryMoveDiagonally(float delta, Vector2 direction){
		float newX = position.x + direction.x * speed;
		if(!gameScreen.isCellBlockedForDean(newX, position.y)) {
		    position.x = newX;
		    return;
		}

		float newY = position.y + direction.y * speed;
		 if(!gameScreen.isCellBlockedForDean(position.x, newY)) {
		    position.y = newY;
		    return;
		}
	}

	/**
	 * Reset the dean to its starting position when the player is caught or to the other side of the map
	 */
	public void resetToStart(int caughtNumber) {
		if (caughtNumber % 2 == 0){
			//if the number of times caught by the dean is even send them to a new positon than their starting, otherwise send them to the start
			position.set(startPosition);
		}else{
			position.set(690, 560);
		}
	}




	/**
	 * Convenience method to be called by the game screen's <code> render()
	 * </code> method, to draw the dean using a SpriteBatch at the current
	 * dean coordinates.
	 * @param batch SpriteBatch used by application to render all sprites.
	 * @see com.badlogic.gdx.graphics.g2d.SpriteBatch SpriteBatch
	 * @see com.badlogic.gdx.Screen#render Screen.render().
	 */
	public void render(SpriteBatch batch) {
		batch.draw(texture, position.x, position.y, 16, 16);
	}

	/**
	 * Return 2D coordinates of dean.
	 * @return 2D Vector x-by-y position of dean.
	 */
	public Vector2 getPosition() {return position; }

	/**
	 * Return rectangle representing collision bounds of dean.
	 * @return Rectangle collision box.
	 */
	public Rectangle getBounds() { return new Rectangle(position.x, position.y, 16,16); }

	/**
	 * Convenience method to be called by application to dispose of textures
	 * of player's sprites when the application's dispose method is called.
	  @see com.badlogic.gdx.Screen#dispose Screen.dispose().
	 */
	public void dispose() {
		texture.dispose();
	}
}
