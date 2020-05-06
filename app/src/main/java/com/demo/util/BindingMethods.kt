package com.demo.util

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import com.demo.R

/** Use to set server image or local image with glide
 *  @param imageView ImageView : image view to set the image
 *  @param url String : url in base64 and converted into drawable
 *  @param placeholder : Drawable : set drawable image till server url image not load
 */
class BindingMethods {
    companion object {
        @BindingAdapter(value = ["imagePath","isCircle", "placeholder"], requireAll = false)
        @JvmStatic
        fun setCircleImage(imageView: ImageView, url: String?,isCircle:Boolean, placeholder: Drawable) {
            if (!url.isNullOrEmpty()) {
                val requestObject: RequestOptions = if (isCircle)
                    RequestOptions.circleCropTransform().error(placeholder).placeholder(placeholder)
                else {
                    RequestOptions().error(placeholder).placeholder(placeholder)
                }
                when {
                    url.contains("http") -> {
                        Glide.with(imageView.context).load(url).apply(
                            requestObject
                        ).into(imageView)
                    }
                    else -> {
                        Glide.with(imageView.context).load(url).apply(requestObject
                        ).into(imageView)
                    }
                }
            } else {
                imageView.setImageDrawable(placeholder)
            }
        }
    }
}