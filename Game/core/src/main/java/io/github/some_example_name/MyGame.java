package io.github.some_example_name;

import com.badlogic.gdx.Game;

/**
 * EXTENDED
 *
 * <code> MyGame </code> handles creating and presenting the game from
 * the different screens.
 * @see com.badlogic.gdx.Game Game
 * @see com.badlogic.gdx.Screen Screen
 */
public class MyGame extends Game {
    /**
     * EXTENDED
     *
     * Create game, starting at the menu score.
     */
    private String PlayerFirstName = "";
    private String PlayerLastName = "";

    @Override
    public void create() {
        setScreen(new MenuScreen(this));
    }

    /**
     * NEW
     *
     * Set the player's name which is used for leaderboard display.
     * The whitespace is trimmed and null values are handled.
     *
     * @param firstName The player's first name
     * @param lastName The player's last name
     */
    public void setPlayerName(String firstName, String lastName) {
        this.PlayerFirstName = firstName != null ? firstName.trim() : "";
        this.PlayerLastName = lastName != null ? lastName.trim() : "";

    }

    /**
     * NEW
     *
     * Return the player's first name.
     *
     * @return Player's first name
     */
    public String getPlayerFirstName() {
        return PlayerFirstName;
    }

    /**
     * NEW
     *
     * Return the player's last name.
     *
     * @return Player's last name
     */
    public String getPlayerLastName() {
        return PlayerLastName;
    }

    /**
     * NEW
     *
     * Return the player's full name, combining first and last name.
     * If no last name is provided, only the first name is returned.
     *
     * @return Player's full name
     */
    public String getPlayerFullName() {
        if (PlayerLastName.isEmpty()) {
            return PlayerFirstName;
        }
        else{
            return PlayerFirstName + " " + PlayerLastName;
        }
    }
}
