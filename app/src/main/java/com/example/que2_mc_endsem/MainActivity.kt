package com.example.que2_mc_endsem

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.example.que2_mc_endsem.api.DirectionsResponse
import com.example.que2_mc_endsem.api.RouteApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var errorTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        errorTextView = findViewById(R.id.error_text_view)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        getDirections("IIITD Campus, Delhi", "Lotus Temple, Delhi")
    }

    private fun getDirections(origin: String, destination: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/maps/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(RouteApiService::class.java)
        val call = service.getDirections(origin, destination, "YOUR_API_KEY")

        call.enqueue(object : Callback<DirectionsResponse> {
            override fun onResponse(call: Call<DirectionsResponse>, response: Response<DirectionsResponse>) {
                if (response.isSuccessful) {
                    response.body()?.routes?.forEach { route ->
                        route.legs?.forEach { leg ->
                            // Create a polyline for the map
                            val polylineOptions = PolylineOptions()
                            leg.steps?.forEach { step ->
                                polylineOptions.add(LatLng(step.startLocation.lat!!, step.startLocation.lng!!))
                            }

                            map.addPolyline(polylineOptions)

                        }
                    }
                } else {
                    runOnUiThread {
                        errorTextView.text = "Server responded but failed to get directions: ${response.code()}"
                    }
                }
            }

            override fun onFailure(call: Call<DirectionsResponse>, t: Throwable) {
                runOnUiThread {
                    errorTextView.text = "Failed to retrieve directions: ${t.message}"
                }
            }
        })
    }
}