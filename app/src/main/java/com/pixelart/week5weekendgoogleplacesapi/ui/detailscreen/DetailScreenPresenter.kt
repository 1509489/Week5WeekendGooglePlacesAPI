package com.pixelart.week5weekendgoogleplacesapi.ui.detailscreen

import android.content.Intent
import com.pixelart.week4weekendgoogleplacesapi.model.PlaceData
import javax.inject.Inject

class DetailScreenPresenter @Inject constructor(private val view: ContractDetail.View): ContractDetail.Presenter {

    override fun getPlaceDetails(intent: Intent):PlaceData {
        return  intent.getParcelableExtra("place")
    }

    override fun onCreate() {

    }

    override fun onResume() {

    }

    override fun onDestroy() {

    }
}