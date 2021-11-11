package ru.netology.papillon.extensions

import android.widget.ImageView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import ru.netology.papillon.R

//fun ImageView.loadAvatars(url: String, vararg transforms: BitmapTransformation = emptyArray()) =
//    Glide.with(this)
//        .load(url)
//        .timeout(10_000)
//        .placeholder(R.drawable.ic_no_avatar_user)
//        .error(R.drawable.ic_avatar_error_foreground)
//        .transform(*transforms)
//        .into(this)
//
//fun ImageView.loadImage(url: String, vararg transforms: BitmapTransformation = emptyArray()) =
//    Glide.with(this)
//        .load(url)
//        .timeout(10_000)
//        .placeholder(CircularProgressDrawable(this.context).apply {
//            strokeWidth = 5f
//            centerRadius = 30f
//            start()
//        })
//        .error(R.drawable.ic_broken_image_24)
//        .transform(*transforms)
//        .into(this)
//
//fun ImageView.loadCircleCrop(url: String, vararg transforms: BitmapTransformation = emptyArray()) =
//    loadAvatars(url, CircleCrop(), *transforms)

fun ImageView.load(url: String, vararg transforms: BitmapTransformation = emptyArray()) =
    Glide.with(this)
        .load(url)
        .timeout(10_000)
        .placeholder(R.drawable.ic_no_avatar_user)
        .error(R.drawable.ic_avatar_error_foreground)
        .transform(*transforms)
        .into(this)

fun ImageView.loadCircleCrop(url: String, vararg transforms: BitmapTransformation = emptyArray()) =
    load(url, CircleCrop(), *transforms)