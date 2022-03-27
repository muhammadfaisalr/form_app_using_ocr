package id.muhammadfaisal.formulirapp.listener

import android.net.Uri

interface CameraListener {
    fun onImageCaptured(uri: Uri, type: Int)
}