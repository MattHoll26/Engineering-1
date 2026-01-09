package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;

/**
 * Base test helper class that sets up LibGDX headless environment.
 * test classes extend this to get proper LibGDX context like Gdx.files and Gdx.graphics
 * LibGDX headless backend for testing
 */
public abstract class TestHelper {

    @BeforeEach
    public void setup() {
        // Mock GL20 for basic rendering calls
        Gdx.gl = Mockito.mock(GL20.class);
        Gdx.gl20 = Mockito.mock(GL20.class);

        // Initialize full headless application
        // This provides Gdx.files, Gdx.graphics, etc.
        HeadlessLauncher.main(new String[0]);
    }
}
