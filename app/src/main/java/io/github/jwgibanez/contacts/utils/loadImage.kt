package io.github.jwgibanez.contacts.utils

import android.view.View
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.lang.Exception

fun loadImage(
    url: String?,
    imageView: CircleImageView,
    progressBar: ProgressBar?
) = url?.let {
    Picasso.get().load(url).into(
        imageView, object : Callback {
            override fun onSuccess() {
                progressBar?.visibility = View.GONE
            }

            override fun onError(e: Exception?) {
                progressBar?.visibility = View.GONE
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        imageView.context,
                        android.R.drawable.stat_notify_error
                    )
                )
            }
        })
}