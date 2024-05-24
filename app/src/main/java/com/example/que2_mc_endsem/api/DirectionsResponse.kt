package com.example.que2_mc_endsem.api

import okhttp3.Route

class DirectionsResponse {

    var routes: List<Route>? = null

    class Route {
        var legs: List<Location>? = null
    }

    class Location {
        val steps: Any
            get() {
                TODO()
            }
        var lat: Double? = null
        var lng: Double? = null
    }

}
