package com.example.eliteworks

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView


open class BaseFragment : Fragment() {
    private lateinit var mProgressDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_base, container, false)
    }

    fun showProgressDialog(text:String)
    {
        mProgressDialog = Dialog(requireActivity())

        /*
            Set progress dialog
         */
        mProgressDialog.setContentView(R.layout.dialog_progress)

        val progressText = mProgressDialog.findViewById<TextView>(R.id.tv_progress_text)
        progressText.text = text

        mProgressDialog.setCancelable(false)
        mProgressDialog.setCanceledOnTouchOutside(false)

        // start showing progress dialog
        mProgressDialog.show()
    }

    fun hideProgressDialog()
    {
        mProgressDialog.dismiss()
    }
}