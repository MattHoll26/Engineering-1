package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/** 
 * <code> NPC </code> represents NPCs that can provide items or dialog to 
 * the player when interacted with. 
 */
public class NPC {
	private Texture texture;
	private Vector2 position;
	private Rectangle bounds;
	private BitmapFont font;
	private boolean showMessage = false;

	/**
	 * Constructor for <code> NPC </code>, with a set of coordinates. 
	 * @param x Horizontal position for NPC to spawn in.
	 * @param y Vertical position for NPC to spawn in.
	 */
	public NPC(float x, float y) {
		texture = new Texture("NPC.png"); 
		position = new Vector2(x, y);
		bounds = new Rectangle(x,y,texture.getWidth(), texture.getHeight());
		font = new BitmapFont();
	}

	/**
	 * Update if the dialog is shown to player depending on player position 
	 * and if the E key has been pressed recently.
	 * @param player Player object.
	 */
	public void update(Player player){
		if (
			player.getPosition().dst(position) < 50f && 
			Gdx.input.isKeyJustPressed(Input.Keys.E)
		)
		{
		    showMessage = true; 
		}

		if(showMessage && player.getPosition().dst(position) > 60f){
		    showMessage = false;
		}
	}

	/**
	 * Convenience method to be called by the game screen's <code> render() 
	 * </code> method, to draw the NPC's sprite and it's dialog using a 
	 * SpriteBatch at the current player coordinates. 
	 * @param batch SpriteBatch used by application to render all sprites.  
	 * @see com.badlogic.gdx.graphics.g2d.SpriteBatch SpriteBatch
	 * @see com.badlogic.gdx.Screen#render Screen.render().
	 */
	public void render(SpriteBatch batch){
		batch.draw(texture, position.x, position.y);
		if (showMessage){
		    font.draw(
			batch, 
			"Hey friend!\nDon't forget your bus ticket...\nyou always drop them by your room", 
			position.x - 100, position.y + texture.getHeight() + 40);  
		}
	}

	/**
	 * Convenience method to be called by application to dispose of textures 
	 * of NPC's sprites and dialog when the application's dispose method is called. 
	 * @see com.badlogic.gdx.Screen#dispose Screen.dispose().
	 */
	public void dispose(){
		texture.dispose();
		font.dispose();
	}
	
	/**
	 * Get the NPC's position in world. 
	 * @return The players x-by-y coordinates as a 2D vector.
	 */
	public Vector2 getPosition() {
		return position;
	}

	/**
	 * Get NPC's collision box.
	 * @return Rectangle bounds of NPC.
	 */ 
	public Rectangle getBounds() {
		return bounds;
	}
}
