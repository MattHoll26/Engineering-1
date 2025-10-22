package io.github.some_example_name;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.Gdx;

public class WorldRenderer extends ApplicationAdapter {
    /*
     * <code> WorldRenderer </code> handles drawing all the graphics for the game, like a tile map for the actual world, 
     * and a set of given sprites characters and objects, to a fit-viewport.
     */
    TiledMap tiledMap;
    OrthogonalTiledMapRenderer mapRenderer;
    OrthographicCamera camera;
    FitViewport viewport;

    private int MAP_HEIGHT = 640;
    private int MAP_WIDTH = 640;

    @Override
    public void create() {
        /* 
         * Instantiate all the objects needed for rendering the TileMap.
         * No parameters or returns. 
         */

        camera = new OrthographicCamera();
        camera.setToOrtho(false, MAP_WIDTH, MAP_HEIGHT);
        camera.update();

        tiledMap = new TmxMapLoader().load("Game Map.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        viewport = new FitViewport(MAP_WIDTH, MAP_HEIGHT, camera);
    }

    @Override
    public void render() {
        /*
         * Clear screen and re-render all sprites + graphics.
         */
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        mapRenderer.setView(camera);
        mapRenderer.render();
    } 
}
