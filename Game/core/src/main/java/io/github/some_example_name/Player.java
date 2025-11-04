package io.github.some_example_name;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/** 
 * <code> Player </code> is used to control rendering and direction of the 
 * player's character, to be called by the main renderer. 
 */
public class Player 
{
	/**
	 * Enumeration of all the possible directions the player sprite can face.
	 */
	public enum Direction 
	{
		UP, 
		DOWN, 
		LEFT, 
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
	 * Construct the player and place them in the given coordinates relative 
	 * in the world. 
	 */
  public Player(float x, float y) 
  {
			position = new Vector2(x, y);

        // loading sprite under diff positions
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
	 * @see Direction
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
	 * </code> method, to draw the player using its SpriteBatch at the 
	 * players current coordinates. 
	 * render the players sprite. 
	 * @param batch SpriteBatch used by application to render all sprites.  
	 * @see SpriteBatch
	 * @see ApplicationAdapter 
	 */
	public void render(SpriteBatch batch) 
	{
		batch.draw(currentFrame, position.x, position.y);
	}

	/**
	 * Get the player's position. 
	 * @return The players x-by-y coordinates as a 2D vector.
	 */
	public Vector2 getPosition() 
	{
		return position;
	}

	/**
	 * Convenience method to be called by application to dispose of textures 
	 * of player's sprites when the application's dispose method is called. 
	 * @see ApplicationAdapter 
	 */
	public void dispose() 
	{
			frontTexture.dispose();
			backTexture.dispose();
			sideTexture.dispose();
	}
}
