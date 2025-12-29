package io.github.some_example_name;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Enemy that patrols up and down between two Y bounds.
 *
 */
public class Patrol_Dean {

    private final Vector2 position;
    private final Texture texture;
    private final GameScreen gameScreen;

    private float speed = 0.7f;

    // limits
    private final float minY;
    private final float maxY;

    // +1 = moving up, -1 = moving down
    private int direction = 1;

    /**
     * Create a dean at a fixed X, starting Y, that moves between minY and maxY.
     */
    public Patrol_Dean(float startX, float startY, float minY, float maxY, GameScreen gameScreen) {
        this.position = new Vector2(startX, startY);
        this.minY = minY;
        this.maxY = maxY;
        this.gameScreen = gameScreen;
        this.texture = new Texture("Dean-front.png"); // reuse same sprite
    }

    /**
     * Move up/down each frame, bouncing at minY/maxY.
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

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x, position.y, 16, 16);
    }

    public Vector2 getPosition() { return position; }

    public Rectangle getBounds() { return new Rectangle(position.x, position.y, 16, 16); }

    public void dispose() {
        texture.dispose();
    }
}
