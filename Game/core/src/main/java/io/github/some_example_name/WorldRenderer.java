package io.github.some_example_name;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class WorldRenderer extends ApplicationAdapter {
    /*
     * <code> WorldRenderer </code> handles drawing all the graphics for the game, like a tile map for the actual world, 
     * and a set of given sprites characters and objects, to a fit-viewport.
     */
    TiledMap tiledMap;
    OrthogonalTiledMapRenderer mapRenderer;
    OrthographicCamera camera;
    FitViewport viewport;

    private SpriteBatch batch;
    private Player player;

    private int MAP_HEIGHT = 640;
    private int MAP_WIDTH = 640;

    @Override
    public void create() 
    {
        /* 
         * Instantiate all the objects needed for rendering the TileMap.
         * No parameters or returns. 
         */

        camera = new OrthographicCamera();
        camera.setToOrtho(false, MAP_WIDTH, MAP_HEIGHT);
        camera.update();

        tiledMap = new TmxMapLoader().load("Tile Maps/Game Map.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        viewport = new FitViewport(MAP_WIDTH, MAP_HEIGHT, camera);

        batch = new SpriteBatch();
        player = new Player(320,320);
    }

    private void handleInput() 
    {
        float moveSpeed = 150f; // this is in pixels per sec

        float newX = player.getPosition().x;
        float newY = player.getPosition().y;

        /*
         * WASD movement yippeeee
         */

        if (Gdx.input.isKeyPressed(Input.Keys.W))
        {
            newY += moveSpeed * Gdx.graphics.getDeltaTime();
            player.setDirection(Player.Direction.UP);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A)) 
        {
            newX -= moveSpeed * Gdx.graphics.getDeltaTime();
            player.setDirection(Player.Direction.LEFT);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.S)) 
        {
            newY -= moveSpeed * Gdx.graphics.getDeltaTime();
            player.setDirection(Player.Direction.DOWN);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D)) 
        {
            newX += moveSpeed * Gdx.graphics.getDeltaTime();
            player.setDirection(Player.Direction.RIGHT);
        }

        if (!isCellBlocked(newX, newY))
        {
            player.getPosition().set(newX, newY);
        }
    }

    private boolean isCellBlocked(float x, float y)
    {
        for (int i = 0; i < tiledMap.getLayers().getCount(); i++) {
            if (tiledMap.getLayers().get(i) instanceof TiledMapTileLayer) {
                TiledMapTileLayer layer = (TiledMapTileLayer) tiledMap.getLayers().get(i);
                
                // Convert pixel coordinates to tile coordinates for the player's center
                int tileX = (int) ((x + 8) / layer.getTileWidth());
                int tileY = (int) ((y + 8) / layer.getTileHeight());

                TiledMapTileLayer.Cell cell = layer.getCell(tileX, tileY);

                // If the cell is empty, it can't be blocked, so we skip to the next layer.
                if (cell == null || cell.getTile() == null) {
                    continue;
                }

                // Collidable property checker for a specific tile (in the future maybe)
                if (cell.getTile().getProperties().containsKey("collidable")) {
                    return true; 
                }

                // Collidable property checker for a specific layer
                if (layer.getProperties().containsKey("collidable")) {
                    return true; 
                }
            }
        }
        return false; 
    }

    @Override
    public void render() 
    {
        /*
         * Clear screen and re-render all sprites + graphics.
         */

         handleInput();

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        mapRenderer.setView(camera);
        mapRenderer.render();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        player.render(batch);
        batch.end();
    } 

    @Override
    public void dispose()
    {
        tiledMap.dispose();
        mapRenderer.dispose();
        batch.dispose();
        player.dispose();
    }
}
