package com.pixelart.week5weekendgoogleplacesapi.base

import android.widget.ProgressBar

interface BaseView {

    fun showMessage(message: String)
    fun showError(error: String)
    fun showLoadingIndicator(loadingIndicator: ProgressBar)
    fun hideLoadingIndicator(loadingIndicator: ProgressBar)
}