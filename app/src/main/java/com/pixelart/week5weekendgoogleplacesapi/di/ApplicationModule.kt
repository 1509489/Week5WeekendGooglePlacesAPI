package com.pixelart.week5weekendgoogleplacesapi.di

import android.app.Activity
import android.databinding.DataBindingUtil
import com.pixelart.week5weekendgoogleplacesapi.R
import com.pixelart.week5weekendgoogleplacesapi.base.BaseActivity
import com.pixelart.week5weekendgoogleplacesapi.databinding.ActivityDetailScreenBinding
import com.pixelart.week5weekendgoogleplacesapi.databinding.ActivityMainScreenBinding
import com.pixelart.week5weekendgoogleplacesapi.remote.APIService
import com.pixelart.week5weekendgoogleplacesapi.ui.detailscreen.ContractDetail
import com.pixelart.week5weekendgoogleplacesapi.ui.detailscreen.DetailScreenActivity
import com.pixelart.week5weekendgoogleplacesapi.ui.detailscreen.DetailScreenPresenter
import com.pixelart.week5weekendgoogleplacesapi.ui.mainscreen.ContractMain
import com.pixelart.week5weekendgoogleplacesapi.ui.mainscreen.MainScreenActivity
import com.pixelart.week5weekendgoogleplacesapi.ui.mainscreen.MainScreenPresenter
import dagger.Module
import dagger.Provides

@Module(includes = [NetworkModule::class])
class ApplicationModule(private val baseActivity: BaseActivity<*>) {

    @Provides
    fun providesMainContractPresenter(apiService: APIService):ContractMain.Presenter = MainScreenPresenter(baseActivity as MainScreenActivity, apiService)

    @Provides
    fun funProvidesMainScreenBinding():ActivityMainScreenBinding = DataBindingUtil.setContentView(baseActivity as Activity, R.layout.activity_main_screen)

    @Provides
    fun providesDetailContractPresenter():ContractDetail.Presenter = DetailScreenPresenter(baseActivity as DetailScreenActivity)

    @Provides
    fun providesDetailScreenBinding():ActivityDetailScreenBinding = DataBindingUtil.setContentView(baseActivity as Activity, R.layout.activity_detail_screen)
}