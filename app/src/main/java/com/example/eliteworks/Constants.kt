package com.example.eliteworks

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap


object Constants
{
    const val READ_STORAGE_PERMISSION_CODE = 2
    const val PICK_IMAGE_REQUEST_CODE = 1
    fun showImageChooser(activity: Activity)
    {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        activity.startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
    }

    fun getFileExtension(activity: Activity,uri: Uri?): String?
    {
        /*
        MimeTypeMap:Two-way map that maps MIME-types to file extensions and vice versa.

        getSingleton():Get the singleton instance of MimeTypeMap.

        getExtensionFromMimeType:Return the registered extension for the given MIME type.

        contentResolver.getType:Return the MIME type of the given content URL.
        */

        //c:/Tushar Bhut/download/user.jpg  --> Uri
        // this will return the .jpg
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
    }
}