package io.github.some_example_name;

/**
 * Test double for NPC class enabling unit testing without LibGDX input dependencies.
 * Overrides protected methods to inject test-controlled behavior allowing NPCTest to verify interaction logic
 * without requiring Gdx.input or actual player logic
 * Test only subclass which will not include in production build
 * Usage: NPCTest creates TestableNPC instances and controls state via setters
 */
public class TestableNPC extends NPC {

    private boolean keyPressed = false;
    private boolean inRange = false;
    private boolean outOfRange = false;
    private String forcedDialog = "TEST DIALOG";

    public TestableNPC(float x, float y) {
        super(x, y);
    }

    public void setKeyPressed(boolean pressed) {
        this.keyPressed = pressed;
    }

    public void setInRange(boolean inRange) {
        this.inRange = inRange;
    }

    public void setOutOfRange(boolean outOfRange) {
        this.outOfRange = outOfRange;
    }

    public void setForcedDialog(String dialog) {
        this.forcedDialog = dialog;
    }

    @Override
    protected boolean isInteractKeyPressed() {
        return keyPressed;
    }

    @Override
    protected boolean isPlayerInRange(Player player) {
        return inRange;
    }

    @Override
    protected boolean isPlayerOutOfRange(Player player) {
        return outOfRange;
    }

    @Override
    protected String chooseDialogLine() {
        return forcedDialog;
    }
}
