package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class NPC {
    private Texture texture;
    private Vector2 position;
    private Rectangle bounds;
    private BitmapFont font;
    private boolean showMessage = false;
 
    public NPC(float x, float y) {
        texture = new Texture("NPC.png"); //set a new texture equal to the NPC's png
        position = new Vector2(x, y);
        bounds = new Rectangle(x,y,texture.getWidth(), texture.getHeight());
        font = new BitmapFont();
    }

    public void update(Player player){
        //check if the player is close enough to interact with the NPC -> less than 50 pixels
        if (player.getPosition().dst(position) < 50f && Gdx.input.isKeyJustPressed(Input.Keys.E)){
            //the player has pressed e to interact with the NPC
            showMessage = true; 
        }

        //hide the message after a certain amount of time or if the player moves away
        if(showMessage && player.getPosition().dst(position) > 60f){
            showMessage = false;
        }
    }

    public void render(SpriteBatch batch){
        //draw the NPC
        batch.draw(texture, position.x, position.y);

        //if the interaction happened then draw the message
        if (showMessage){
            font.draw(batch, "Hey friend! \nDon't forget your bus ticket...\nyou always drop them by your room", position.x - 100, position.y + texture.getHeight() + 40); //prints the message above the NPC
        }
    }

    //helper methods
    public Vector2 getPosition() {
        return position;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void dispose(){
        texture.dispose();
        font.dispose();
    }
}