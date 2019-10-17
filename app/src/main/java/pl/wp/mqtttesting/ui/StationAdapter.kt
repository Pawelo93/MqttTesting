package pl.wp.mqtttesting.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.station_item.view.*
import org.joda.time.DateTime
import pl.wp.mqtttesting.R

class StationAdapter : RecyclerView.Adapter<StationViewHolder>() {

    var stations: List<Station> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    fun updateStation(station: Station) {
        val mutableList = stations.toMutableList()
        val index = stations.indexOfFirst { it.name == station.name }
        if (index >= 0) {
            mutableList[index] = station
        } else
            mutableList.add(station)

        stations = mutableList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StationViewHolder {
        return StationViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.station_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount() = stations.size

    override fun onBindViewHolder(holder: StationViewHolder, position: Int) {
        holder.bind(stations[position])
    }
}

class StationViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bind(station: Station) = with(itemView) {
        stationName.text = station.name
        station.tracks.forEachIndexed { index, track ->
            when (index) {
                0 -> {
                    track1Name.text = track.title
                    val time = DateTime((track.end * 1000).toLong())
                    track1Time.text = "${time.hourOfDay().get()}:${time.minuteOfHour().get()}"
                }
                1 -> {
                    track2Name.text = track.title
                    val time = DateTime((track.end * 1000).toLong())
                    track2Time.text = "${time.hourOfDay().get()}:${time.minuteOfHour().get()}"
                }
                2 -> {
                    track3Name.text = track.title
                    val time = DateTime((track.end * 1000).toLong())
                    track3Time.text = "${time.hourOfDay().get()}:${time.minuteOfHour().get()}"
                }
            }
        }


    }
}