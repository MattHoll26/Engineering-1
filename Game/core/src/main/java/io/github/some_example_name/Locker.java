package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * <code> Locker </code> is an interactable game object, that acts as the 
 * positive game event by allowing the player to find a "sweet treat" 
 * giving a temporary speed boost.
 */

public class Locker {
	private Texture texture;
	private Vector2 position;
	private Rectangle bounds;
	private boolean searched = false;
	private boolean showMessage = false;
	private float messageTimer = 0f;
	private final float messageDuration = 5f; 
	private final float speedBoostAmount = 200f;
	private final float speedBoostDuration = 10f; 
	private float speedBoostTimer = 0f;
	private BitmapFont font;

	/**
	 * Constructor for <code> Locker </code>, with a set of coordinates. 
	 * @param x Horizontal position for locker to spawn in.
	 * @param y Vertical position for locker to spawn in.
	 */
	public Locker(float x, float y) {
		texture = new Texture("locker.png"); 
		position = new Vector2(x, y);
		bounds = new Rectangle(x,y,texture.getWidth(), texture.getHeight());
		font = new BitmapFont();
	}

	/** 
	 * Update attributes of locker, and decrement timer on speed boost and 
	 * label timer, showing label if the label timer is still active.
	 * @param player Player character.
	 * @param delta Time elapsed since last frame.
	 */ 
	public void update(Player player, float delta) {
		if (!searched && Gdx.input.isKeyJustPressed(Input.Keys.E)){
		    if (player.getPosition().dst(position) < 50f) {
			searched = true;
			showMessage = true;
			messageTimer = 0f;
			speedBoostTimer = speedBoostDuration;
		    }
		}

		if (speedBoostTimer > 0){
		    speedBoostTimer -= delta;
		}

		if (showMessage) {
		    messageTimer += delta;
		    if (messageTimer > messageDuration){
			showMessage = false;
		    }
		}
	}

	/**
	 * Convenience method to be called by the game screen's <code> render() 
	 * </code> method, to draw the locker and it's label using a 
	 * SpriteBatch at it's coordinates. 
	 * @param batch SpriteBatch used by application to render all sprites.  
	 * @see com.badlogic.gdx.graphics.g2d.SpriteBatch SpriteBatch
	 * @see com.badlogic.gdx.Screen#render Screen.render().
	 */
	public void render(SpriteBatch batch){
		batch.draw(texture, position.x, position.y);
		if (showMessage) {
		    font.draw(
			batch, 
			"You found a sweet treat,\n enjoy the sugar induced speed boost!", 
			position.x - 100, position.y + texture.getHeight() + 40	
			); 
		}
	}
	
	/**
	 * Convenience method to be called by application to dispose of texture 
	 * and font's locker's sprites when the application's dispose method is called. 
	 * @see com.badlogic.gdx.Screen#dispose Screen.dispose().
	 */
	public void dispose(){
		texture.dispose();
		font.dispose();
	}

	/**
	 * Return if speed booster is still active.
	 * @return True/False value.
	 */
	public boolean isBoostActive() {
		return speedBoostTimer > 0;
	}
}
