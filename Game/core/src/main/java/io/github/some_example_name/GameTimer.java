package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.audio.Sound;

/**
 * <code>GameTimer</code> implements a timer that ticks down during gameplay, and emits
 * a sound when it reaches 0.  
 * It contains a sprite that can be rendered to display to the user graphically
 * how much time is left.
 *
 * @since 2025-11-04 19:28:26
 */
public class GameTimer {
	
	private Float timeLeft;
	private Label timerLabel;
	private Sound timerDingSFX;

	/**
	 * Constructor for <code>GameTimer</code>, defaulting to 300 seconds. 
	 * @param skin Skin containing style for the timer label. 
	 * @param table Table displaying timer widget. 
	 */ 
	public GameTimer(Skin skin, Table table) {
		this.timeLeft = 300f;	
		this.timerDingSFX = Gdx.audio.newSound(
			Gdx.files.internal("ding.wav")
		);
		instantiateLabel(skin,table);
	}

	/**
	 * Constructor for <code>GameTimer</code> for a given amount of seconds. 
	 * @param skin Skin containing style for the timer label. 
	 * @param table Table displaying timer widget. 
	 * @param seconds Time in seconds to be counted by timer. 
	 * @see {@link com.badlogic.gdx.scenes.scene2d.ui.Skin} Skin 
	 */
	public GameTimer(Skin skin, Table table, float seconds) {
		this.timeLeft = seconds;	

		this.timerDingSFX = Gdx.audio.newSound(
			Gdx.files.internal("ding.wav")
		);
		instantiateLabel(skin,table);
	} 

	
	/**
 	 * Return the time in formatted in mm:ss for time left. 
 	 * @return String formatted in mm:ss of time left.
	 */
	@Override 
	public String toString() { 	
		long minutes = (long) Math.floor(this.timeLeft / 60) ;
		long seconds = (long) Math.floor(this.timeLeft) - (minutes * 60);
		return String.format("%02d : %02d", minutes, seconds);
	}
	
	/**
	 * Decrements timer by given value, or to 0 if the new value will be a 
	 * negative number. 
	 * @param decrementaion Value to reduce the timer by, in seconds, ideally sourced from {@see Gdx.graphics.getRawDeltaTime()}.
	 */
	public void decrementTimer(float decrementaion) {
		this.timeLeft = Math.max(0, timeLeft - decrementaion);	
		this.timerLabel.setText(this.toString());
	}

	/**
	 * Called when the time reaches 0, to play finishing sound and 
	 * change to game over screen. 	
	 * @param 
	 */
	public void onTimeUp() { 
		this.timerDingSFX.play(3f); 
	}

	/**
	 * Get time left in seconds in timer. 
	 * @return Time left in seconds.
	 */
	public float getTimeLeft() { 
		return this.timeLeft;
	}  

	/**
 	 * Get the grpahical label that displays the amount of time left. 
	 * @return Label component. 
	 */
	public Label getTimerLabel() { 
		return this.timerLabel;
	}

	/**
 	 * Helper method to create and add the label to given table. 
 	 * @param skin Skin containing style for the timer label. 
	 * @param table Table displaying timer widget. 
	 */
	private void instantiateLabel(Skin skin, Table table) { 
		this.timerLabel = new Label("", skin);
		table.add(this.timerLabel).row();
	}
}
