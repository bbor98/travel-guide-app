package com.borabor.travelguideapp.presentation.bindingadapter

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.borabor.travelguideapp.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

@BindingAdapter("isVisible")
fun View.setVisibility(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}

@BindingAdapter("loadImage")
fun ImageView.loadImage(imageUrl: String?) {
    imageUrl?.let {
        Glide.with(context)
            .load(it)
            .transition(DrawableTransitionOptions.withCrossFade())
            .error(R.drawable.ic_broken_image)
            .into(this)
    }
}

@BindingAdapter("tint")
fun ImageView.setImageTint(color: Int) {
    setColorFilter(color)
}