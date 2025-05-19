package ro.experimentation.dev

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.assets.disposeSafely
import ro.experimentation.dev.screens.GameScreen

/** [com.badlogic.gdx.ApplicationListener] implementation shared by all platforms. */
class LibgdxExperiments : KtxGame<KtxScreen>() {

    // share a batch instance with other screens?
    private val batch by lazy { SpriteBatch() }
    private val assets by lazy { Assets() }

    override fun create() {
        super.create()
        addScreen(GameScreen(batch, assets))
        setScreen<GameScreen>()
    }

    override fun dispose() {
        super.dispose()
        batch.disposeSafely()
        assets.disposeSafely()
    }

    companion object {
        const val UNIT_SCALE = 1 / 16f // 16 pixels is one in game world unit == 1 meter in Box2D world
    }
}
