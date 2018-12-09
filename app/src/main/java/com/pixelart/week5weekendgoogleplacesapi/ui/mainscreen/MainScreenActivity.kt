package com.pixelart.week5weekendgoogleplacesapi.ui.mainscreen

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.pixelart.week4weekendgoogleplacesapi.model.APIResponse
import com.pixelart.week4weekendgoogleplacesapi.model.PlaceData
import com.pixelart.week5weekendgoogleplacesapi.R
import com.pixelart.week5weekendgoogleplacesapi.adapters.PlacesAdapter
import com.pixelart.week5weekendgoogleplacesapi.base.BaseActivity
import com.pixelart.week5weekendgoogleplacesapi.databinding.ActivityMainScreenBinding
import com.pixelart.week5weekendgoogleplacesapi.di.ApplicationModule
import com.pixelart.week5weekendgoogleplacesapi.di.DaggerApplicationComponent
import com.pixelart.week5weekendgoogleplacesapi.di.NetworkModule
import com.pixelart.week5weekendgoogleplacesapi.ui.detailscreen.DetailScreenActivity
import kotlinx.android.synthetic.main.place_type_menu.view.*
import javax.inject.Inject

class MainScreenActivity : BaseActivity<ContractMain.Presenter>(), ContractMain.View,
    OnMapReadyCallback, PlacesAdapter.OnItemClickedListener, GoogleApiClient.ConnectionCallbacks,
GoogleApiClient.OnConnectionFailedListener, LocationListener {


    private val TAG = "MainScreenActivity"
    private val USER_REQUEST_CODE = 99

    @Inject
    lateinit var presenter: ContractMain.Presenter
    @Inject
    lateinit var binding: ActivityMainScreenBinding

    //Map variables
    private lateinit var mMap: GoogleMap
    private lateinit var locationRequest: LocationRequest
    private lateinit var googleApiClient : GoogleApiClient
    private lateinit var lastLocation: Location
    private var mapConnected = false

    private var longitude: Double = 0.0
    private var latitude: Double = 0.0

    //RecyclerView setup variables
    private lateinit var placeList: ArrayList<PlaceData>
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var adapter: PlacesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        buildGoogleApiClient()
        hideLoadingIndicator(binding.rvProgressBar)
        binding.tvInfo.text = resources.getString(R.string.select_place_type)

        placeList = ArrayList()
        layoutManager = LinearLayoutManager(this)
        //presenter.getPlaces("51.7541278,-0.2273111", "restaurant")

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission()
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.mapType = GoogleMap.MAP_TYPE_HYBRID

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)  {
            buildGoogleApiClient()
            mMap.isMyLocationEnabled = true
        }
        if (mMap.isMyLocationEnabled) {
            binding.include.ibRestaurant.setOnClickListener {
                presenter.getPlaces("${lastLocation.latitude},${lastLocation.longitude}", "restaurant")
                showLoadingIndicator(binding.rvProgressBar)
                binding.tvInfo.visibility = View.INVISIBLE
            }
            binding.include.ibHotel.setOnClickListener {
                presenter.getPlaces("${lastLocation.latitude},${lastLocation.longitude}", "lodging")
                showLoadingIndicator(binding.rvProgressBar)
                binding.tvInfo.visibility = View.GONE
            }
            binding.include.ibBank.setOnClickListener {
                presenter.getPlaces("${lastLocation.latitude},${lastLocation.longitude}", "bank")
                showLoadingIndicator(binding.rvProgressBar)
                binding.tvInfo.visibility = View.GONE
            }
            binding.include.ibHospital.setOnClickListener {
                presenter.getPlaces("${lastLocation.latitude},${lastLocation.longitude}", "hospital")
                showLoadingIndicator(binding.rvProgressBar)
                binding.tvInfo.visibility = View.GONE
            }
            binding.include.ibSuperMarket.setOnClickListener {
                presenter.getPlaces("${lastLocation.latitude},${lastLocation.longitude}", "supermarket")
                showLoadingIndicator(binding.rvProgressBar)
                binding.tvInfo.visibility = View.GONE
            }
        }
    }

    override fun getPresenterView(): ContractMain.Presenter = presenter

    override fun init() {
        DaggerApplicationComponent.builder()
            .applicationModule(ApplicationModule(this))
            .networkModule(NetworkModule())
            .build().injectMainScreen(this)
    }

    fun checkPermission(): Boolean{

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED )
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), USER_REQUEST_CODE)
            }
            else{
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), USER_REQUEST_CODE)
            }
            return false
        }
        else{
            return true
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode)
        {
            USER_REQUEST_CODE ->{
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                    {
                        if (googleApiClient == null)
                        {
                            buildGoogleApiClient()
                        }
                        mMap.isMyLocationEnabled = true
                    }
                }
                else{
                    Toast.makeText(this, "Permission Require", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    @Synchronized
    protected fun buildGoogleApiClient() {
        googleApiClient = GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()
        googleApiClient.connect()
    }

    override fun showPlaces(apiResponse: APIResponse) {
        placeList.clear()
        mMap.clear()

        for (i in 0 until apiResponse.results.size) {
            val lat = apiResponse.results[i].geometry.location.lat
            val lng = apiResponse.results[i].geometry.location.lng

            val placeName = apiResponse.results[i].name
            val vicinity = apiResponse.results[i].vicinity
            val rating = apiResponse.results[i].rating
            val priceLevel = apiResponse.results[i].priceLevel
            val icon = apiResponse.results[i].icon
            //val openNow = apiResponse.results[i].openingHours.openNow
            var openNow = false
            if (!apiResponse.results[i].openingHours.openNow == null) {
                openNow = true
            }

            val latLng = LatLng(lat, lng)

            mMap.addMarker(presenter.markerOptions(latLng, "$placeName: $vicinity"))
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
            mMap.animateCamera(CameraUpdateFactory.zoomBy(0F))

            placeList.add(PlaceData(placeName, vicinity, rating, priceLevel, icon,true))
            Log.d(TAG, "Open Now: $openNow")
        }

        adapter = PlacesAdapter(placeList, this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter

        val status = apiResponse.status
        if (status == "OK") {
            hideLoadingIndicator(binding.rvProgressBar)
            initItemTouchHelper()
            binding.tvInfo.visibility = View.INVISIBLE
            adapter.notifyDataSetChanged()
        }
        else if (status == "OVER_QUERY_LIMIT") {
            adapter.notifyDataSetChanged()
            hideLoadingIndicator(binding.rvProgressBar)
            binding.tvInfo.visibility = View.VISIBLE
            binding.tvInfo.text = resources.getString(R.string.over_query_limit)
        }
    }

    override fun onConnected(bundle: Bundle?) {
        showMessage("Connected")
        locationRequest = LocationRequest()
        locationRequest.interval = 1000
        locationRequest.fastestInterval = 1000
        locationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (googleApiClient.isConnected)
            {
                mapConnected = true
                lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient)
                LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this)
            }
        }
    }

    override fun onConnectionSuspended(i: Int) {
        showMessage("Connection Suspended")
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        showMessage("Connection Failed")
    }

    override fun onLocationChanged(location: Location) {
        showMessage("Location Changed")

        lastLocation = location
        longitude = location.longitude
        latitude = location.latitude

        val latLng = LatLng(location.latitude, location.longitude)
        val markerOptions = MarkerOptions()
        markerOptions.position(latLng)
        markerOptions.title("Current Location")
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))

        mMap.addMarker(markerOptions)?.remove()

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        mMap.animateCamera(CameraUpdateFactory.zoomBy(15F))

        if (googleApiClient != null)
        {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this)
        }
    }



    override fun onItemClicked(position: Int) {
        showMessage("Item at Position: $position clicked")

        val place = PlaceData(
            placeList[position].placeName,
            placeList[position].vicinity,
            placeList[position].rating,
            placeList[position].priceLevel,
            placeList[position].icon,
            placeList[position].openNow
        )

        startActivity(Intent(this, DetailScreenActivity::class.java).also {
            it.putExtra("place", place)
        })
    }

    private fun initItemTouchHelper(){
        val itemTouchCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
            override fun onMove(p0: RecyclerView, p1: RecyclerView.ViewHolder, p2: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(holder: RecyclerView.ViewHolder, direction: Int) {
                val position = holder.adapterPosition

                val places = PlaceData(
                    placeList[position].placeName,
                    placeList[position].vicinity,
                    placeList[position].rating,
                    placeList[position].priceLevel,
                    placeList[position].icon,
                    placeList[position].openNow
                )

                val destination = ("${places.placeName}${places.vicinity}").replace(" ", "+")
                val mapIntentUri = Uri.parse("https://www.google.com/maps/dir/?api=1&destination=$destination")
                val mapIntent = Intent(Intent.ACTION_VIEW, mapIntentUri)
                mapIntent.`package` = "com.google.android.apps.maps"

                if (direction == ItemTouchHelper.LEFT)
                {
                    startActivity(mapIntent)
                    adapter.notifyDataSetChanged()
                }
                else if (direction == ItemTouchHelper.RIGHT)
                {
                    startActivity(mapIntent)
                    adapter.notifyDataSetChanged()
                }
            }

        }

        val itemTouchHelper = ItemTouchHelper(itemTouchCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
    }
}

