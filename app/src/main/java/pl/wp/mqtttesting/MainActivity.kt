package pl.wp.mqtttesting

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import net.danlew.android.joda.JodaTimeAndroid
import pl.wp.mqtttesting.api.ApiServiceProvider
import pl.wp.mqtttesting.mqtt.MqttRxHelper
import pl.wp.mqtttesting.ui.*

class MainActivity : AppCompatActivity(), MainView {
    var apiCallPresenter: ApiCallPresenter? = null

    var mqttPresenter: MqttPresenter? = null
    val adapter = StationAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        JodaTimeAndroid.init(this)

        val stations = (1..10).toList()
        apiCallPresenter = ApiCallPresenter(
            ApiServiceProvider().get(),
            stations
        )

        mqttPresenter = MqttPresenter(
            MqttRxHelper(applicationContext),
            stations
        )

        apiCallPresenter?.attach(this)
        mqttPresenter?.attach(this)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        mqttPresenter?.observe()
    }


    override fun updateList(stations: List<Station>) {
        adapter.stations = stations
    }

    override fun updateStation(station: Station) {
        adapter.updateStation(station)
    }

    override fun onDestroy() {
        apiCallPresenter?.clear()
        mqttPresenter?.clear()
        super.onDestroy()
    }
}