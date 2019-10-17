package pl.wp.mqtttesting.ui

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import pl.wp.mqtttesting.mqtt.MqttRxHelper
import pl.wp.mqtttesting.base.BasePresenter

class MqttPresenter(
    private val mqttRxHelper: MqttRxHelper,
    private val stationIds: List<Int>
) : BasePresenter<MainView>() {

    private var view: MainView? = null

    override fun attach(view: MainView) {
        this.view = view
    }

    override fun detach() {
        view = null
    }

    fun observe() {
        mqttRxHelper.setup()
        mqttRxHelper.connect()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                println("Connected! $it")
                subscribeToStations()
            }, {
                println("Error $it")
            })
            .remember()
    }

    private fun subscribeToStations() {
        Observable.fromIterable(stationIds)
            .flatMap {
                mqttRxHelper.subscribe(it)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view?.updateStation(it)
            }, {
                println("Error $it")
            })
            .remember()
    }
}