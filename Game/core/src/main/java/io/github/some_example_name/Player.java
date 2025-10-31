package io.github.some_example_name;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Player 
{

    public enum Direction 
    {
        UP, DOWN, LEFT, RIGHT
    }
    private Vector2 position;

    private Texture frontTexture;
    private Texture backTexture;
    private Texture sideTexture;

    private TextureRegion frontFrame;
    private TextureRegion backFrame;
    private TextureRegion sideFrame;

    private TextureRegion currentFrame;

    public Player(float x, float y) 
    {
        position = new Vector2(x, y);

        // loading sprite under diff positions
        frontTexture = new Texture("Player.png"); 
        backTexture = new Texture("Player-back.png");
        sideTexture= new Texture ("Player-side.png");

        frontFrame = new TextureRegion(frontTexture);
        backFrame = new TextureRegion(backTexture);
        sideFrame = new TextureRegion(sideTexture);

        // front facing frame as default
        currentFrame = frontFrame;
    }

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

    public void render(SpriteBatch batch) 
    {
        batch.draw(currentFrame, position.x, position.y);
    }

    public Vector2 getPosition() 
    {
        return position;
    }

    public void dispose() 
    {
        frontTexture.dispose();
        backTexture.dispose();
        sideTexture.dispose();
    }
    
}
