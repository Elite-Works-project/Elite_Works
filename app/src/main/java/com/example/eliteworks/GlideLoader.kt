package com.example.eliteworks

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import java.io.IOException

class GlideLoader(val context: Context)
{
    fun loadUserPicture(imageUri: Any,imageView: ImageView)
    {
        try {
            Glide
                .with(context)
                .load(imageUri)
                .centerCrop()
                .placeholder(R.drawable.man)
                .into(imageView)
        }
        catch (e:IOException)
        {
            e.printStackTrace()
        }
    }
}