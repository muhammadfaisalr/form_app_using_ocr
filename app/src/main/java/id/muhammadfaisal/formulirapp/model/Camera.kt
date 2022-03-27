package id.muhammadfaisal.formulirapp.model

import android.content.Context
import id.muhammadfaisal.formulirapp.listener.CameraListener
import java.io.Serializable

data class Camera(
    var cameraListener: CameraListener
) : Serializable