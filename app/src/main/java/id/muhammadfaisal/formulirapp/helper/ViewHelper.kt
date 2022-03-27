package id.muhammadfaisal.formulirapp.helper

import android.content.Context
import android.view.View

class ViewHelper {
    companion object {
        fun makeClickable(listener: View.OnClickListener, vararg views: View) {
            for (view in views) {
                view.setOnClickListener(listener)
            }
        }

        fun enabled(vararg  views: View){
            for (view in views) {
                view.isEnabled = true
            }
        }

        fun disabled(vararg  views: View){
            for (view in views) {
                view.isEnabled = false
            }
        }

        fun gone(vararg  views: View){
            for (view in views) {
                view.visibility = View.GONE
            }
        }

        fun invisible(vararg  views: View){
            for (view in views) {
                view.visibility = View.INVISIBLE
            }
        }

        fun visible(vararg  views: View){
            for (view in views) {
                view.visibility = View.VISIBLE
            }
        }
    }
}