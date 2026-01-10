package io.github.some_example_name;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * NEW
 *
 * <code>Patrol_Dean</code> represents a Dean enemy that patrols vertically
 * between two Y axis bounds. The dean reverses direction when reaching
 * its limits or when colliding with a blocked tile.
 */
public class Patrol_Dean {

    private final Vector2 position;
    private final Texture texture;
    private final GameScreen gameScreen;

    private float speed = 3f;

    // limits
    private final float minY;
    private final float maxY;

    // +1 = moving up, -1 = moving down
    private int direction = 1;

    /**
     * Create a dean at a fixed X, starting Y, that moves between minY and maxY.
     *
     * @param startX Initial horizontal position
     * @param startY Initial vertical position
     * @param minY Minimum Y boundary for patrol
     * @param maxY Maximum Y boundary for patrol
     * @param gameScreen Game screen used for collision checks
     *
     */
    public Patrol_Dean(float startX, float startY, float minY, float maxY, GameScreen gameScreen) {
        this.position = new Vector2(startX, startY);
        this.minY = minY;
        this.maxY = maxY;
        this.gameScreen = gameScreen;
        this.texture = new Texture("Dean-front.png"); // reuse same sprite
    }

    /**
     * Set the movement speed of the patrol dean.
     * @param newSpeed New speed value
     */
    public void setSpeed(float newSpeed) {
        this.speed = newSpeed;
    }

    /**
     * Return the current movement speed of the patrol dean.
     * @return Current speed value
     */
    public float getSpeed() {
        return speed;
    }

    /**
     * Move up/down each frame, bouncing at minY/maxY.
     * @param delta Time elapsed since the last frame
     */
    public void update(float delta) {
        float newY = position.y + (direction * speed);

        // bounce off limits
        if (newY > maxY) {
            newY = maxY;
            direction = -1;
        } else if (newY < minY) {
            newY = minY;
            direction = 1;
        }

        // collision check (only vertical movement)
        if (!gameScreen.isCellBlocked(position.x, newY)) {
            position.y = newY;
        } else {
            // if blocked, flip direction so it doesn't get stuck forever
            direction *= -1;
        }
    }

    /**
     * Render the patrol dean sprite.
     * @param batch SpriteBatch used to draw the sprite
     */
    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x, position.y, 16, 16);
    }

    /**
     * Return the current position of the patrol dean.
     * @return 2D vector representing the dean's position
     */
    public Vector2 getPosition() { return position; }

    /**
     * Return the collision bounds of the patrol dean.
     * @return Rectangle representing the collision area
     */
    public Rectangle getBounds() { return new Rectangle(position.x, position.y, 16, 16); }

    /**
     * Dispose of the patrol dean's texture when no longer needed.
     */
    public void dispose() {
        texture.dispose();
    }
}
