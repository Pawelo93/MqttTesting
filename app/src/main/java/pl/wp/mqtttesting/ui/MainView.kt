package pl.wp.mqtttesting.ui

interface MainView {
    fun updateList(stations: List<Station>)

    fun updateStation(station: Station)
}