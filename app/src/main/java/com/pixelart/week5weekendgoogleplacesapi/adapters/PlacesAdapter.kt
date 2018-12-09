package com.pixelart.week5weekendgoogleplacesapi.adapters

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.pixelart.week4weekendgoogleplacesapi.model.PlaceData
import com.pixelart.week5weekendgoogleplacesapi.R
import com.pixelart.week5weekendgoogleplacesapi.common.GlideApp
import com.pixelart.week5weekendgoogleplacesapi.databinding.RecyclerviewLayoutBinding
import kotlinx.android.synthetic.main.recyclerview_layout.view.*

class PlacesAdapter(private val placeList: List<PlaceData>, private val listener: OnItemClickedListener):
    RecyclerView.Adapter<PlacesAdapter.ViewHolder>() {
    private lateinit var context: Context
    private val lastPosition = -1

    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): ViewHolder {
        context = viewGroup.context
        val binding: RecyclerviewLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.recyclerview_layout, viewGroup, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return placeList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (position > lastPosition) {
            val animation = AnimationUtils.loadAnimation(context, android.R.anim.fade_in)
            animation.setInterpolator(context, android.R.interpolator.bounce)
            holder.itemView.animation = animation
        }

        val place = placeList[position]

        holder.itemView.apply {
            tvName.text = place.placeName
            tvRating.text = place.rating.toString()
            ratingBar.rating = place.rating.toFloat()

            if (place.openNow)
                tvOpenNow.text = resources.getString(R.string.open)
            else
                tvOpenNow.text = resources.getString( R.string.close)

            GlideApp.with(context)
                .load(place.icon)
                .placeholder(R.drawable.placeholder)
                .override(100,100)
                .into(ivIcon)

            when(place.priceLevel){
                0 ->{
                    tvPriceLevel.text = ""
                }
                1 ->{
                    tvPriceLevel.text = "£"
                }
                2 ->{
                    tvPriceLevel.text = "££"
                }
                3 ->{
                    tvPriceLevel.text = "£££"
                }
                4 ->{
                    tvPriceLevel.text = "££££"
                }
                5 ->{
                    tvPriceLevel.text = "£££££"
                }
            }
        }


        holder.itemView.setOnClickListener {
            listener.onItemClicked(position)
        }
    }

    class ViewHolder(binding: RecyclerviewLayoutBinding): RecyclerView.ViewHolder(binding.root)

    interface OnItemClickedListener{
        fun onItemClicked(position: Int)
    }
}