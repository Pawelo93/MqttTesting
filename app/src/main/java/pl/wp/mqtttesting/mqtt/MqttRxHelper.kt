package pl.wp.mqtttesting.mqtt

import android.content.Context
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types.newParameterizedType
import io.reactivex.Observable
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*
import pl.wp.mqtttesting.ui.Station
import pl.wp.mqtttesting.ui.Track

class MqttRxHelper(private val context: Context) {

    lateinit var mqttClient: MqttAndroidClient

    private val moshi = Moshi.Builder().build()
    private val jsonAdapter = moshi.adapter<List<MqttTrack>>(
        newParameterizedType(
            List::class.java,
            MqttTrack::class.java
        )
    )

    fun setup() {
        val clientId = MqttClient.generateClientId()
        mqttClient = MqttAndroidClient(context, "tcp://open.fm:1883", clientId)

        val options = MqttConnectOptions()
        options.isAutomaticReconnect = true
    }

    fun connect() = Observable.create<Boolean> { emitter ->
        mqttClient.connect().actionCallback = object : IMqttActionListener {
            override fun onSuccess(asyncActionToken: IMqttToken?) {
                emitter.onNext(true)
            }

            override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                exception?.let { emitter.onError(it) }
            }
        }
    }

    fun subscribe(id: Int) = Observable.create<MqttMessage> { emitter ->
        mqttClient.subscribe("station/$id", 0) { topic, message ->
            println(message)
            emitter.onNext(message)
        }
    }.map {
        jsonAdapter.fromJson(it.toString())
    }.map {
        val tracks = it.map { Track("${it.song.artist} - ${it.song.title}", it.begin, it.end) }.reversed()
        Station(id, "Station $id", tracks)
    }
}