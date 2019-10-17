package pl.wp.mqtttesting.ui

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.joda.time.DateTime
import pl.wp.mqtttesting.api.ApiService
import pl.wp.mqtttesting.api.PlaylistResponse
import pl.wp.mqtttesting.base.BasePresenter
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class ApiCallPresenter(
    private val apiService: ApiService,
    private val stationIds: List<Int>
) : BasePresenter<MainView>() {

    private var view: MainView? = null

    override fun attach(view: MainView) {
        this.view = view
    }

    override fun detach() {
        this.view = null
    }

    fun observe() {
//        val randomTime = (Random.nextInt(60) + 15).toLong()
        val randomTime = 10L

        Observable.fromIterable(stationIds)
            .flatMap { stationId ->
                Observable.interval(0,randomTime, TimeUnit.SECONDS)
                    .flatMapSingle { loadStation(stationId) }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view?.updateStation(it)
            }, {
                println()
            })
            .remember()

//        stationSubject
//            .flatMap { station ->
//                val time =(station.tracks.last().end.toLong() * 1000) - DateTime.now().millis
//                Observable.timer(time, TimeUnit.MILLISECONDS)
//                    .doOnNext {
//                        println("load next ${station.name}")
//                    }
//                    .flatMapSingle { loadStation(station.id) }
//                    .doOnNext { stationSubject.onNext(it) }
//            }
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe({
//                view?.updateStation(it)
//            }, {
//                println()
//            })
//            .remember()
    }

//    private fun observeStation(id: Int, end: Long): Observable<Station> {
//        var time = DateTime.now().millis - end
//        if (time <= 1)
//            time = 1
//
//        println("observe station $id time $time")
//
//        return Observable.timer(time, TimeUnit.MILLISECONDS)
//            .doOnNext {
//                println("Next $id $end")
//            }
//            .flatMapSingle { loadStation(id) }
//    }

    private fun loadStation(id: Int): Single<Station> {
        println("load Stations $id")
        return apiService.loadPlaylist(id)
            .map(::mapToStations)
            .map { it.first() }
    }

    private fun mapToStations(playlistResponses: List<PlaylistResponse>): List<Station> {
        return playlistResponses.map { playlistResponse ->
            val tracks = playlistResponse.tracks.map {
                Track(
                    "${it.song.artist} - ${it.song.title}",
                    it.begin,
                    it.end
                )
            }
            Station(playlistResponse.streamId, "Station ${playlistResponse.streamId}", tracks)
        }
    }
}