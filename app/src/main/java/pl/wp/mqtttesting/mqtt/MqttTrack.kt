package pl.wp.mqtttesting.mqtt

data class MqttTrack(
    val begin: Double,
    val track_id: Int,
    val end: Double,
    val song: Song
)

data class Song(
    val title: String,
    val artist: String
)