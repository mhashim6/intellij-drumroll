import jaco.mp3.player.MP3Player

/**
 *@author mhashim6 on 20/03/19
 */

private val player  by lazy { MP3Player() }

fun play(what: String, extension: String = "mp3") {
    if (!player.isStopped)
        player.stop()
    player.playList.clear()
    player.addToPlayList(Drumroll::class.java.getResource("$what.$extension"))
    player.play()
}

fun isPlaying() = !player.run { isStopped || isPaused || !isEnabled }

fun stop() {
    player.stop()
}