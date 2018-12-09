package com.pixelart.week5weekendgoogleplacesapi.ui.mainscreen

import android.util.Log
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.pixelart.week4weekendgoogleplacesapi.model.APIResponse
import com.pixelart.week5weekendgoogleplacesapi.common.*
import com.pixelart.week5weekendgoogleplacesapi.remote.APIService
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MainScreenPresenter @Inject constructor(private val view: ContractMain.View, private val apiService: APIService):
    ContractMain.Presenter{

    override fun getPlaces(location: String, type: String) {
        apiService.getPlaces(location, type, RADIUS, true, API_KEY)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                object : SingleObserver<APIResponse>{
                    override fun onSuccess(t: APIResponse) {
                        view.showPlaces(t)
                    }

                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        view.showError("Error: Failed to fetch data")
                    }
                }
            )
    }

    override fun markerOptions(latLng: LatLng, title: String):MarkerOptions{
        return MarkerOptions()
            .position(latLng)
            .title(title)
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
    }

    override fun onCreate() {

    }

    override fun onResume() {

    }

    override fun onDestroy() {

    }
}