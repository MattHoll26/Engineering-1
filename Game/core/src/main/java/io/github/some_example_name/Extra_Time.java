package io.github.some_example_name;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * NEW
 *
 * <code>Extra_Time</code> is an interactable game object that represents
 * a positive event. When the player collides with this NPC, additional
 * time is permanently added to the game timer.
 */
public class Extra_Time {


    private Texture texture; // The texture used to render the NPC
    private Vector2 position; // The map position of the NPC

    private Rectangle bounds; // The collision bounds
    /** The tracking of NPC interaction */
    private boolean searched = false;
    /** Determines whether the message should currently be shown */
    private boolean showMessage = false;
    /** Tracks whether the time bonus has been granted */
    private boolean gainedTime = false;
    /** The timer which is used to control message display duration */
    private float messageTimer = 0f;
    /** The duration (in seconds) that the message remains visible */
    private final float messageDuration = 5f;
    /** The amount of time (in seconds) that is added to the game timer */
    private final float timeIncreaseAmount = 30; // +30 seconds
    private BitmapFont font; // The Font used to render on-screen messages

    /**
     * Constructor for <code>Extra_Time</code>, spawning the NPC at the given
     * world coordinates.
     *
     * @param x Horizontal position to spawn the NPC
     * @param y Vertical position to spawn the NPC
     */
    public Extra_Time(float x, float y) {
        texture = new Texture("NPC.png");

        // Move it slightly right and up from the base coordinates
        position = new Vector2(x + 20, y + 10);

        bounds = new Rectangle(
            position.x,
            position.y,
            texture.getWidth(),
            texture.getHeight()
        );

        font = new BitmapFont();
    }

    /**
     * Update attributes of NPC, and increment timer on time increase and
     * label timer, showing label if the label timer is still active.
     *
     * @param player The player character interacting with the NPC
     * @param timer GameTimer is used to add additional time
     * @param delta Represents the time elapsed since the last frame
     */
    public void update(Player player, GameTimer timer, float delta) {
        if (!searched) {
            Rectangle playerRect = new Rectangle(
                player.getPosition().x,
                player.getPosition().y,
                player.currentFrame.getRegionWidth(),
                player.currentFrame.getRegionHeight()
            );

            if (bounds.overlaps(playerRect)) {
                searched = true;
                showMessage = true;
                gainedTime = true;
                messageTimer = 0f;
                timer.addTime(timeIncreaseAmount);
            }
        }

        if (showMessage) {
            messageTimer += delta;
            if (messageTimer > messageDuration) {
                showMessage = false;
            }
        }
    }

    /**
     * Render the NPC and any associated on-screen message.
     *
     * @param batch SpriteBatch used to render the NPC and text
     */
    public void render(SpriteBatch batch) {
        if (!searched) {
            batch.draw(texture, position.x, position.y);
        }

        if (showMessage) {
            font.draw(
                batch,
                "You found extra time!\n+30 seconds",
                position.x - 40, position.y + texture.getHeight() + 20
            );
        }
    }

    /**
     * Dispose of textures and fonts associated with this NPC.
     */
    public void dispose() {
        texture.dispose();
        font.dispose();
    }

    /**
     * Return if the extra time NPC has been hit for updating the counter
     *
     * @return True if extra time was awarded, false otherwise
     */
    public boolean gainedTime(){
        return gainedTime;
    }
}

