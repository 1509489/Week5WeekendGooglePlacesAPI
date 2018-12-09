package com.pixelart.week5weekendgoogleplacesapi.di

import com.pixelart.week5weekendgoogleplacesapi.ui.detailscreen.DetailScreenActivity
import com.pixelart.week5weekendgoogleplacesapi.ui.mainscreen.MainScreenActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {

    fun injectMainScreen(mainScreen: MainScreenActivity)
    fun injectDetailScreen(detailScreen: DetailScreenActivity)
}