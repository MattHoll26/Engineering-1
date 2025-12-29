package io.github.some_example_name;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * <code> Extra_Time </code> is an interactable game object, that acts as the
 * positive game event by allowing the player to collide with an NPC,
 * giving a permanent time increase.
 */
public class Extra_Time {
    private Texture texture;
    private Vector2 position;
    private Rectangle bounds;
    private boolean searched = false;
    private boolean showMessage = false;
    private boolean gainedTime = false;
    private float messageTimer = 0f;
    private final float messageDuration = 5f;
    private final float timeIncreaseAmount = 30; // +30 seconds
    private BitmapFont font;

    /**
     * Constructor for <code> Extra_Time </code>, with a set of coordinates.
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
     * Draw the NPC and message.
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

    public void dispose() {
        texture.dispose();
        font.dispose();
    }

    /**
     * Return if the extra time NPC has been hit for updating the counter
     */
    public boolean gainedTime(){
        return gainedTime;
    }
}

