package io.github.some_example_name;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/** 
 * <code> Player </code> is the main character of the game, handling rendering and direction of the character sprite. 
 */
public class Player 
{
	/**
	 * Enumeration of all the possible directions the player sprite can face.
	 */
	public enum Direction 
	{
		/** Face North */
		UP, 

		/** Face South */
		DOWN, 
		/** Face West */
		LEFT, 
		/** Face East */
		RIGHT
	}

	private Vector2 position;
	private Texture frontTexture;
	private Texture backTexture;
	private Texture sideTexture;
	private TextureRegion frontFrame;
	private TextureRegion backFrame;
	private TextureRegion sideFrame;
	private TextureRegion currentFrame;

	/**
	 * Constructor for <code> Player </code>, with a set of coordinates. 
	 * @param x Horizontal position for player to spawn in.
	 * @param y Vertical position for player to spawn in.
	 */
	public Player(float x, float y) 
	{
		position = new Vector2(x, y);

		frontTexture = new Texture("Player-front.png"); 
		backTexture = new Texture("Player-back.png");
		sideTexture= new Texture ("Player-side.png");

		frontFrame = new TextureRegion(frontTexture);
		backFrame = new TextureRegion(backTexture);
		sideFrame = new TextureRegion(sideTexture);

		currentFrame = frontFrame;
	}

	/** 
	 * Called to set the direction of the Player sprite.
	 * @param newDirection the direction the player should face. 
	 * @see Direction Direction.
	 */
	public void setDirection(Direction newDirection)
	{
		switch (newDirection)
		{
			case UP:
				currentFrame = backFrame;
				break;
			case DOWN:
				currentFrame = frontFrame;
				break;
			case LEFT:
				currentFrame = sideFrame;
				if (currentFrame.isFlipX()) 
				{
					currentFrame.flip(true, false);
				}
				break;
			case RIGHT:
				currentFrame = sideFrame;
				if (!currentFrame.isFlipX())
				{
					currentFrame.flip(true, false);
				}
				break;
		}
	}

	/**
	 * Convenience method to be called by the game screen's <code> render() 
	 * </code> method, to draw the player using a SpriteBatch at the current 
	 * player coordinates. 
	 * @param batch SpriteBatch used by application to render all sprites.  
	 * @see com.badlogic.gdx.graphics.g2d.SpriteBatch SpriteBatch
	 * @see com.badlogic.gdx.Screen#render Screen.render().
	 */
	public void render(SpriteBatch batch) 
	{
		batch.draw(currentFrame, position.x, position.y);
	}

	/**
	 * Get the player's position in world. 
	 * @return The players x-by-y coordinates as a 2D vector.
	 */
	public Vector2 getPosition() 
	{
		return position;
	}

	/**
	 * Convenience method to be called by application to dispose of textures 
	 * of player's sprites when the application's dispose method is called. 
	 * @see com.badlogic.gdx.Screen#dispose Screen.dispose().
	 */
	public void dispose() 
	{
		frontTexture.dispose();
		backTexture.dispose();
		sideTexture.dispose();
	}
}
