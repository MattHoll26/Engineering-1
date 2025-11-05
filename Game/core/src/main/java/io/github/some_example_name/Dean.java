package io.github.some_example_name;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Rectangle;

public class Dean {
    private Vector2 position;
    private Texture texture;
    private Player player;
    private GameScreen gameScreen;
    private float speed = 0.7f; //slightly slower than the player to give a fair chance

    public Dean(float x, float y, Player player, GameScreen gameScreen){
        this.position = new Vector2(x, y);
        this.texture = new Texture("Dean-front.png");
        this.player = player;
        this.gameScreen = gameScreen;
    }

    public void update(float delta) {
        //simple direct movement where the dean goes towards the player's position
        Vector2 direction = new Vector2(player.getPosition()).sub(position); //creates a new copy of the player's position vector with subtracting the Dean's position from the player's
        direction.nor();

        //Dean try to move towards the player diagonally -> use deltra for smooth movement
        float newX = position.x + direction.x * speed; //horizontal
        float newY = position.y + direction.y * speed; //vertical

        //use GameScreen's collision detection
        if(!gameScreen.isCellBlocked(newX, newY)) {
            position.set(newX, newY);
        }

        //if moving diagonally is blocked then try horizontally, then vertically
        tryAlternativeMovements(delta, direction);
    }

    private void tryAlternativeMovements(float delta, Vector2 direction){
        //try horizontal movement 
        float newX = position.x + direction.x * speed;
        if(!gameScreen.isCellBlocked(newX, position.y)) {
            position.x = newX;
            return;
        }

        //try vertical movement
        float newY = position.y + direction.y * speed;
         if(!gameScreen.isCellBlocked(position.x, newY)) {
            position.y = newY;
            return;
        }
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x, position.y, 16, 16); //this means show the dean at his location and make sure he is 16x16 pixels
    } 

    /**
     * creates a collision rectangle for the dean
     */
    public Vector2 getPosition() {
        return position;
    }

    public Rectangle getBounds() {
        return new Rectangle(position.x, position.y, 16,16);
    }

    public void dispose() {
        texture.dispose();
    }
}