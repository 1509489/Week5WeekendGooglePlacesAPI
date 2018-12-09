package com.pixelart.week5weekendgoogleplacesapi.ui.detailscreen

import android.content.Intent
import com.pixelart.week4weekendgoogleplacesapi.model.PlaceData
import com.pixelart.week5weekendgoogleplacesapi.base.BasePresenter
import com.pixelart.week5weekendgoogleplacesapi.base.BaseView

interface ContractDetail {
    interface View: BaseView {
        fun displayPlaceDetails()
    }

    interface Presenter: BasePresenter{
        fun getPlaceDetails(intent: Intent):PlaceData
    }
}