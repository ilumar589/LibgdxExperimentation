package ro.experimentation.dev.screens

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.utils.viewport.FitViewport
import com.github.quillraven.fleks.configureWorld
import ktx.app.KtxScreen
import ro.experimentation.dev.*
import ro.experimentation.dev.system.RenderSystem

class GameScreen(
    private val batch: Batch,
    private val assets: Assets
) : KtxScreen {

    private val camera = OrthographicCamera()
    private val gameViewport = FitViewport(10f, 7f, camera)

    private val world = configureWorld {
        injectables {
            add(camera)
            add("gameViewport", gameViewport)
            add(assets)
            add(batch)
        }

        systems {
            add(RenderSystem())
        }
    }

    override fun show() {
        super.show()

        world
            .systems
            .filterIsInstance<GameEventListener>()
            .forEach { GameEventDispatcher.registerListener(it) }

        val map = assets[MapAsset.TEST_MAP]
        GameEventDispatcher.dispatchEvent(MapChangedEvent(map))
    }

    override fun resize(width: Int, height: Int) {
        super.resize(width, height)
        gameViewport.update(width, height, true)
    }

    override fun render(delta: Float) {
        super.render(delta)
        world.update(delta)
    }

    override fun dispose() {
        super.dispose()
        world.dispose()
    }
}
