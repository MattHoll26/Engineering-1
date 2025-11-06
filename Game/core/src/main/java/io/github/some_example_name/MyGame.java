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
	@Override
	public void create() {
		setScreen(new MenuScreen(this));
	}
}
