package ro.experimentation.dev

import com.badlogic.gdx.maps.tiled.TiledMap

sealed class GameEvent

data class MapChangedEvent(val tiledMap: TiledMap) : GameEvent()

interface GameEventListener {
    fun onEvent(event: GameEvent)
}

object GameEventDispatcher {
    private val listeners = mutableListOf<GameEventListener>()

    fun registerListener(listener: GameEventListener) {
        listeners += listener
    }

    fun unregisterListener(listener: GameEventListener) {
        listeners -= listener
    }

    fun dispatchEvent(event: GameEvent) {
        listeners.forEach { it.onEvent(event) }
    }
}
