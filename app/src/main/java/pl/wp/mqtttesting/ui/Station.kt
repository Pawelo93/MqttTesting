package pl.wp.mqtttesting.ui

data class Station(
    val id: Int,
    val name: String,
    val tracks: List<Track>
)

data class Track(
    val title: String,
    val begin: Double,
    val end: Double
)