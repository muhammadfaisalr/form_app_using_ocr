package id.muhammadfaisal.formulirapp

import android.app.Application
import android.util.Log

class FormulirApplication : Application() {

    companion object {
        val TAG = FormulirApplication::class.java.simpleName
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate();")


    }
}