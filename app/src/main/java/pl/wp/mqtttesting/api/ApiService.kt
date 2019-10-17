package pl.wp.mqtttesting.api

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("playlists/stream_{id}.json?number=3")
    fun loadPlaylist(@Path("id") stationId: Int): Single<List<PlaylistResponse>>
}