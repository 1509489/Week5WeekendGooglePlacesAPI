package com.pixelart.week4weekendgoogleplacesapi.model

import com.google.gson.annotations.SerializedName

data class APIResponse(
    @SerializedName("html_attributions")
    val htmlAttributions: List<Any>,
    val results: List<Result>,
    val status: String
)