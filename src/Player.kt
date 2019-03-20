import jaco.mp3.player.MP3Player

/**
 *@author mhashim6 on 20/03/19
 */

private val player = MP3Player()

fun play(what: String) {
    if (!player.isStopped)
        player.stop()
    player.playList.clear()
    player.addToPlayList(Drumroll::class.java.getResource(what))
    player.play()
}

fun stop() {
    player.stop()
}