package com.example.ctvms

import android.content.Context
import android.net.ConnectivityManager
import androidx.appcompat.app.AlertDialog
import customAlert

object NetworkUtils {

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    private fun showCustomAlert(context: Context) {
        val myCustomAlert = customAlert(context)
        myCustomAlert.setData("No Internet Connection", R.drawable.errorinfo, "Please check your internet connection.")
        val customAlertView = myCustomAlert.getView()
        val alert = AlertDialog.Builder(context).apply {
            setView(customAlertView)
            setCancelable(false)
        }.create()


        val okBtn = myCustomAlert.getOkButton()
        alert.show()
        okBtn.setOnClickListener {
            alert.dismiss()
        }
    }

    fun showNetworkAlert(context: Context) {
        showCustomAlert(context)
    }
}
