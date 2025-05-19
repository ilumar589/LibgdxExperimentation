package ro.experimentation.dev

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import ktx.assets.disposeSafely

enum class MapAsset(val path: String) {
    TEST_MAP("maps/test_map.tmx")
}

class Assets {
    private val assetManager = AssetManager().apply {
        setLoader(TiledMap::class.java, TmxMapLoader())
    }

    operator fun get(mapAsset: MapAsset): TiledMap {
        assetManager.load(mapAsset.path, TiledMap::class.java)
        assetManager.finishLoading()
        return assetManager.get(mapAsset.path)
    }

    fun disposeSafely() = assetManager.disposeSafely()
}
