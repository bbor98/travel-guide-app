package com.borabor.travelguideapp.domain.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Travel(
    @PrimaryKey
    val id: String,
    val category: String,
    val city: String,
    val country: String,
    val dateCreated: Long? = null,
    val description: String,
    val images: List<Image>,
    val isBookmark: Boolean,
    val schedule: String? = null,
    val title: String
) : Parcelable