package io.github.some_example_name;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * <code> BusTicket </code> represents the ticket used to allow player to board
 * bus and win the game, containing both the actual sprite the user can interact 
 * with, and UI elements to represent if the ticket has been picked up or not.
 */ 
public class BusTicket {
	private Vector2 position;
	private Texture texture;
	private boolean isCollected;
	private boolean isDiscovered; // To control when it becomes visible

	/**
	 * Constructor for <code> BusTicket </code>, spawning the ticket sprite
	 * in the given world coordinates.Uses "bus-ticket.png" in 
	 * <code> assets/ </code> folder for sprite.
	 * @param x Horizontal position in world for ticket sprite. 
	 * @param y Vertical position in world for ticket sprite. 
	 */ 
	public BusTicket(float x, float y) {
		this.position = new Vector2(x, y);
		this.texture = new Texture("bus-ticket.png"); 
		this.isCollected = false;
		this.isDiscovered = false;
	}

	/**
	 * Render ticket sprite within given sprite batch.
	 * @param batch SpriteBatch to render world sprite in. 
	 * @see com.badlogic.gdx.graphics.g2d.SpriteBatch SpriteBatch.
	 */
	public void render(SpriteBatch batch) {
		if (isDiscovered && !isCollected) {
		    batch.draw(texture, position.x, position.y, 16, 16);
		}
	}

	/**
	 * Render ticket collected UI element, to signify the ticket is in user 
	 * inventory. 
	 * @param batch SpriteBatch to render UI sprite in. 
	 * @param camera Camera connected to batch. 
	 * @see com.badlogic.gdx.graphics.g2d.SpriteBatch SpriteBatch.
	 * @see com.badlogic.gdx.graphics.OrthographicCamera OrthographicCamera
	 */
	public void renderAsIcon(SpriteBatch batch, OrthographicCamera camera) {
		if (isCollected) {
		    float iconSize = 32f; 
		    float padding = 20f;

		    // Calculate the top-right corner of the camera's view
		    // We must account for the camera's zoom level
		    float cornerX = camera.position.x + (camera.viewportWidth / 2 * camera.zoom);
		    float cornerY = camera.position.y + (camera.viewportHeight / 2 * camera.zoom);

		    // Set the icon's position with padding
		    float iconX = cornerX - iconSize - padding;
		    float iconY = cornerY - iconSize - padding;

		    batch.draw(texture, iconX, iconY, iconSize, iconSize);
		}
	}

	/**
	 * Return ticket position as a 2D Vector. 
	 * @return 2D Vector with x/y positions of ticket.
	 */
	public Vector2 getPosition() {
		return position;
	}

	/**
	 * Return if the ticket has been collected by player or not.
	 * @return True/False value corrosponding to if ticket is collected. 
	 */
	public boolean isCollected() {
		return isCollected;
	}

	/**
	 * Set ticket collection to true.
	 */
	public void collect() {
		this.isCollected = true;
	}

	/**
	 * Set ticket being discovered by player to true.
	 */
	public void discover() {
		this.isDiscovered = true;
	}

	/**
	 * Dispose ticket sprite texture.Use as part of 
	 * Application dispose method. 
	 */
	public void dispose() {
		texture.dispose();
	}
}
