package io.github.some_example_name;

import com.badlogic.gdx.Game;

/**
 * <code> MyGame </code> handles creating and presenting the game from
 * the different screens.
 * @see com.badlogic.gdx.Game Game
 * @see com.badlogic.gdx.Screen Screen
 */
public class MyGame extends Game {
    /** Create game, starting at the menu score. */
    private String PlayerFirstName = "";
    private String PlayerLastName = "";

    @Override
    public void create() {
        setScreen(new MenuScreen(this));
    }

    public void setPlayerName(String firstName, String lastName) {
        this.PlayerFirstName = firstName != null ? firstName.trim() : "";
        this.PlayerLastName = lastName != null ? lastName.trim() : "";

    }

    public String getPlayerFirstName() {
        return PlayerFirstName;
    }

    public String getPlayerLastName() {
        return PlayerLastName;
    }

    public String getPlayerFullName() {
        if (PlayerLastName.isEmpty()) {
            return PlayerFirstName;
        }
        else{
            return PlayerFirstName + " " + PlayerLastName;
        }
    }
}
