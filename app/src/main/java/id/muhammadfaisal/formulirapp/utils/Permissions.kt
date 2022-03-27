package id.muhammadfaisal.formulirapp.utils

import android.Manifest

class Permissions {
    companion object {
        val REQUIRED_PERMISSION = arrayOf(android.Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }
}