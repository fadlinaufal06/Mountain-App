package com.bangkit.mountainapp.data.local

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FavoriteData(
    var favoritePhotoFeeds: Int,
    var favoriteName: String,
    var favoriteLocation: String,
):Parcelable


