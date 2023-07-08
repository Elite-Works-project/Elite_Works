package com.example.eliteworks


import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage

class FirestoreClass
{
    private val mFireStore = FirebaseFirestore.getInstance()

    fun registerUser(activity: SignUpActivity, userInfo:User)
    {
        // "Users" is the collection, if it is already created then it will not create the same one again
        mFireStore.collection(Constants.USERS)
//          Document ID is for user fields, here Document is User ID
            .document(userInfo.id)
//          userInfo is Field and SetOptions is set to merge. It is for if we wants to merge later on instead of replacing the fields.
            .set(userInfo, SetOptions.merge())
            .addOnCompleteListener{
                activity.userRegisteredSuccessfully()
            }
            .addOnFailureListener {e->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName, "Error while registering the user.",e)
            }
    }

    fun getCurrentUserID():String
    {
        //Instance of current user using FirebaseAuth
        val currentUser = FirebaseAuth.getInstance().currentUser

        // Assigns the currentUserID if it's not null or else keep it blank
        var currentUserID = ""
        if(currentUserID!=null)
        {
            currentUserID = currentUser!!.uid
        }
        return currentUserID
    }

    fun getUserDetails(activity: Activity)
    {
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .get()
            .addOnCompleteListener{ document ->
                Log.i(activity.javaClass.simpleName, document.toString())

                // here we receive the document snapshot and we convert it into data model object
                val user = document.result.toObject(User::class.java)!!

                /* here MODE_PRIVATE is for default mode means where the created file can only be accessed
                    by the calling application means to all the applicants who shares the same User ID
                */
                val sharedPreferences = activity.getSharedPreferences(
                    Constants.RB_PREFERENCES,Context.MODE_PRIVATE
                )

                // for editing shared preferences
                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                /*
                    Key : logged_in_username
                    Value : Tushar Bhut (as example)
                */

                editor.putString(
                    Constants.LOGGED_IN_USERNAME,
                    user.name
                )
                editor.apply()
                when(activity)
                {
                    is LoginActivity -> {
                        activity.userLoggedInSuccess(user)
                    }
                    is ProfilePreviewActivity ->{
                        activity.userDetailsSuccess(user)
                    }
                    is UserDashboardActivity ->{
                        activity.userDetailsSuccess(user)
                    }
                }
            }
            .addOnFailureListener { e->
                // hide progress dialog is there is any error. And print the error in log
                when(activity)
                {
                    is LoginActivity -> {
                        activity.hideProgressDialog()
                    }
                }
                Log.e(activity.javaClass.simpleName, "Error while getting user details",e )
            }
    }

    fun updateUserDetails(activity: Activity,userHashMap: HashMap<String,Any>)
    {
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .update(userHashMap)
            .addOnSuccessListener {
                when(activity)
                {
                    is UpdateProfileActivity ->{
                        activity.userProfileUpdateSuccess()
                    }
                }
            }
            .addOnFailureListener { e->
                when(activity)
                {
                    is UpdateProfileActivity ->{
                        activity.hideProgressDialog()
                    }
                }
                Log.e(activity.javaClass.simpleName, "Error while updating user details.", e)
            }
    }

    fun uploadImageToCloudStorage(activity: Activity,imageUri:Uri?, imageType:String)
    {
        val storageReference = FirebaseStorage.getInstance().reference
            .child(imageType + System.currentTimeMillis() + "."
                    +Constants.getFileExtension(activity,imageUri))

        storageReference.putFile(imageUri!!).addOnSuccessListener { taskSnapshot ->

            // Image upload is success
//            Log.e("Firebase Image URL",
//            taskSnapshot.metadata!!.reference!!.downloadUrl.toString())

            // get the downloadable url from the task snapshot
            taskSnapshot.metadata!!.reference!!.downloadUrl
                .addOnSuccessListener { uri ->
//                    Log.e("Downloadable Image URL ",uri.toString() )
                    when(activity)
                    {
                        is UpdateProfileActivity -> {
                            activity.imageUploadSuccess(uri.toString())
                        }
                    }
                }

        }
            .addOnFailureListener {exception ->
                when(activity)
                {
                    is UpdateProfileActivity -> {
                        activity.hideProgressDialog()
                    }
                }
                Log.e(activity.javaClass.simpleName, exception.message,exception )
            }
    }
}