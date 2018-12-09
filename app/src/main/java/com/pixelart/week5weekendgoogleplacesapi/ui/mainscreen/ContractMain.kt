package com.pixelart.week5weekendgoogleplacesapi.ui.mainscreen

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.pixelart.week4weekendgoogleplacesapi.model.APIResponse
import com.pixelart.week5weekendgoogleplacesapi.base.BasePresenter
import com.pixelart.week5weekendgoogleplacesapi.base.BaseView

interface ContractMain {
    interface View: BaseView{
        fun showPlaces(apiResponse: APIResponse)
    }

    interface Presenter: BasePresenter{
        fun getPlaces(location: String, type: String)
        fun markerOptions(latLng: LatLng, title: String): MarkerOptions
    }
}