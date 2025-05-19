package ro.experimentation.dev.system

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.maps.MapLayer
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile
import com.badlogic.gdx.utils.viewport.Viewport
import com.github.quillraven.fleks.IntervalSystem
import com.github.quillraven.fleks.World.Companion.inject
import ktx.assets.disposeSafely
import ktx.graphics.use
import ro.experimentation.dev.GameEvent
import ro.experimentation.dev.GameEventListener
import ro.experimentation.dev.LibgdxExperiments.Companion.UNIT_SCALE
import ro.experimentation.dev.MapChangedEvent

class RenderSystem(
    private val batch: Batch = inject(),
    private val gameViewport: Viewport = inject("gameViewport"),
    private val gameCamera: OrthographicCamera = inject()
) : IntervalSystem(), GameEventListener {

    private val mapRenderer = OrthogonalTiledMapRenderer(null, UNIT_SCALE, batch)
    private val backgroundLayers = mutableListOf<TiledMapTileLayer>()
    private val foregroundLayers = mutableListOf<TiledMapTileLayer>()


    override fun onTick() {
        gameViewport.apply()

        mapRenderer.use(gameCamera) { batch ->
            backgroundLayers.forEach { mapRenderer.renderTileLayer(it) }
            // render entities
            foregroundLayers.forEach { mapRenderer.renderTileLayer(it) }
        }
    }

    override fun onDispose() {
        super.onDispose()
        mapRenderer.disposeSafely()
    }

    override fun onEvent(event: GameEvent) {
        when (event) {
            is MapChangedEvent -> {
                mapRenderer.map = event.tiledMap
                parseMapLayers(event.tiledMap)
            }
        }
    }

    private fun OrthogonalTiledMapRenderer.use(camera: OrthographicCamera, actions: (Batch) -> Unit) {
        this.setView(camera)
        AnimatedTiledMapTile.updateAnimationBaseTime()
        this.batch.use {
            actions(this.batch)
        }
    }

    private fun parseMapLayers(tiledMap: TiledMap) {
        backgroundLayers.clear()
        foregroundLayers.clear()

        var currentLayers: MutableList<TiledMapTileLayer> = backgroundLayers

        // this is stupid, we are rendering in the order of the layer created in tilemap
        // so for example when we encounter the objects layer we switch to foreground layers. I'll leave it for now to
        // follow the tutorial

        tiledMap.layers
            .forEach { layer ->
                when (layer) {
                    is TiledMapTileLayer -> currentLayers += layer
                    is MapLayer -> {
                        // we have an object layer -> switch from background to foreground
                        currentLayers = foregroundLayers
                    }

                    else -> {}
                }
            }
    }
}
