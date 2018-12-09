package com.pixelart.week5weekendgoogleplacesapi.ui.detailscreen

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.pixelart.week5weekendgoogleplacesapi.R
import com.pixelart.week5weekendgoogleplacesapi.base.BaseActivity
import com.pixelart.week5weekendgoogleplacesapi.common.GlideApp
import com.pixelart.week5weekendgoogleplacesapi.databinding.ActivityDetailScreenBinding
import com.pixelart.week5weekendgoogleplacesapi.di.ApplicationModule
import com.pixelart.week5weekendgoogleplacesapi.di.DaggerApplicationComponent
import com.pixelart.week5weekendgoogleplacesapi.di.NetworkModule
import javax.inject.Inject

class DetailScreenActivity : BaseActivity<ContractDetail.Presenter>(), ContractDetail.View {

    @Inject
    lateinit var presenter: ContractDetail.Presenter
    @Inject
    lateinit var binding: ActivityDetailScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        displayPlaceDetails()
    }

    override fun getPresenterView(): ContractDetail.Presenter = presenter

    override fun init() {
        DaggerApplicationComponent.builder()
            .applicationModule(ApplicationModule(this))
            .networkModule(NetworkModule())
            .build().injectDetailScreen(this)
    }

    @SuppressLint("SetTextI18n")
    override fun displayPlaceDetails() {
        val place = presenter.getPlaceDetails(intent)

        binding.tvName.text = place.placeName
        binding.tvAddress.text = place.vicinity
        binding.tvRatings.text = place.rating.toString()
        binding.ratingBar.rating = place.rating.toFloat()

        if (place.openNow)
            binding.tvOpenHours.text = resources.getString(R.string.open)
        else
            binding.tvOpenHours.text = resources.getString(R.string.close)

        GlideApp.with(this)
            .load(place.icon)
            .placeholder(R.drawable.placeholder)
            .override(385,250)
            .fitCenter()
            .into(binding.ivPhoto)

        binding.btnDirection.setOnClickListener {
            val destination = ("${place.placeName}${place.vicinity}").replace(" ", "+")
            val mapIntentUri = Uri.parse("https://www.google.com/maps/dir/?api=1&destination=$destination")
            val mapIntent = Intent(Intent.ACTION_VIEW, mapIntentUri)
            mapIntent.`package` = "com.google.android.apps.maps"
            startActivity(mapIntent)
        }

        when(place.priceLevel){
            0 ->{
                binding.tvPriceLevel.text = "Price Level: Not available"
            }
            1 ->{
                binding.tvPriceLevel.text = "Price Level: £"
            }
            2 ->{
                binding.tvPriceLevel.text = "Price Level: ££"
            }
            3 ->{
                binding.tvPriceLevel.text = "Price Level: £££"
            }
            4 ->{
                binding.tvPriceLevel.text = "Price Level: ££££"
            }
            5 ->{
                binding.tvPriceLevel.text = "Price Level: £££££"
            }
        }
    }

}
