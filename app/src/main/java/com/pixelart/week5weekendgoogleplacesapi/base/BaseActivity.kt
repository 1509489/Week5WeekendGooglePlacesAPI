package com.pixelart.week5weekendgoogleplacesapi.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast

abstract class BaseActivity<T: BasePresenter>: AppCompatActivity(),BaseView{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    override fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun showError(error: String) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
    }

    override fun showLoadingIndicator(loadingIndicator: ProgressBar) {
        loadingIndicator.visibility = View.VISIBLE
    }

    override fun hideLoadingIndicator(loadingIndicator: ProgressBar) {
        loadingIndicator.visibility = View.INVISIBLE
    }

    abstract fun getPresenterView():T
    abstract fun init()
}