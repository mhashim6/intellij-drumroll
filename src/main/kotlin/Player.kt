import jaco.mp3.player.MP3Player

/**
 *@author mhashim6 on 20/03/19
 */

private val player = MP3Player()
private var currentlyPlaying: String? = null

fun play(what: String, extension: String = "mp3") {
    if (what == currentlyPlaying) return
    if (!player.isStopped)
        player.stop()
    player.playList.clear()
    player.addToPlayList(Drumroll::class.java.getResource("$what.$extension"))
    player.play()
    currentlyPlaying = what
}

fun stop() {
    player.stop()
}