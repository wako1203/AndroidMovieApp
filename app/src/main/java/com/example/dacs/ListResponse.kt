package com.example.dacs

import android.graphics.Movie
import com.google.gson.annotations.SerializedName

data class ListResponse(
    @SerializedName("results")
    val movies: List<DataModel>
)