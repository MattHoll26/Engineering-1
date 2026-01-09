package io.github.some_example_name;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;

/**
 * HeadlessLauncher provides a headless application context for unit testing.
 * Assets are loaded from src/test/resources/ directory.
 */
public class HeadlessLauncher {

    private static Application application;

    public static void main(String[] args) {
        if (application == null) {
            createApplication();
        }
    }

    private static Application createApplication() {
        application = new HeadlessApplication(new MyGame(), getDefaultConfiguration());
        return application;
    }

    private static HeadlessApplicationConfiguration getDefaultConfiguration() {
        HeadlessApplicationConfiguration configuration = new HeadlessApplicationConfiguration();
        configuration.updatesPerSecond = -1;
        return configuration;
    }
}
