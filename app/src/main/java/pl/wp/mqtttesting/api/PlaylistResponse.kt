package pl.wp.mqtttesting.api

import com.squareup.moshi.Json

data class PlaylistResponse(
    @Json(name = "stream_id") val streamId: Int,
    val tracks: List<Track>
)

data class Track(
    val begin: Double,
    val end: Double,
    val song: Song,
    @Json(name = "track_id") val id: Long
)

data class Song(
    val title: String,
    val artist: String,
    val album: Album?
)

data class Album(
    val year: Int?,
    val title: String?,
    @Json(name = "cover_uri") val coverUri: String?
)