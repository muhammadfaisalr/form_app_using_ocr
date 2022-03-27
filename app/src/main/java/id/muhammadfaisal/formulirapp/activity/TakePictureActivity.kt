package id.muhammadfaisal.formulirapp.activity

import android.app.IntentService
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import id.muhammadfaisal.formulirapp.R
import id.muhammadfaisal.formulirapp.api.model.OcrResponse
import id.muhammadfaisal.formulirapp.api.model.Result
import id.muhammadfaisal.formulirapp.databinding.ActivityTakePictureBinding
import id.muhammadfaisal.formulirapp.helper.GeneralHelper
import id.muhammadfaisal.formulirapp.helper.ViewHelper
import id.muhammadfaisal.formulirapp.utils.Apis
import id.muhammadfaisal.formulirapp.utils.Constant
import id.muhammadfaisal.formulirapp.utils.Permissions
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.quality
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class TakePictureActivity : AppCompatActivity(), View.OnClickListener{

    private lateinit var binding: ActivityTakePictureBinding

    private lateinit var outputDirectory : File
    private lateinit var cameraExecutor: ExecutorService

    private var identityType: Int = 0
    private var formId: Long = 0
    private var imageCapture: ImageCapture? = null
    private val TAG = TakePictureActivity::class.java.simpleName
    private val requestCodePermission = 102

    private var result1: Result? = null

    private lateinit var progressDialog: ProgressDialog

    companion object {
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = ActivityTakePictureBinding.inflate(this.layoutInflater)
        this.setContentView(this.binding.root)

        this.extract()
        this.init()
    }

    private fun extract() {
        val bundle = this.intent.getBundleExtra(Constant.Key.BUNDLING)

        if (bundle != null) {
            this.identityType = bundle.getInt(Constant.Key.IDENTITY_TYPE, 0)
            this.formId = bundle.getLong(Constant.Key.FORM_ID, 0L)
        }
    }

    private fun init() {
        this.outputDirectory = this.getOutputDirectory()
        this.cameraExecutor = Executors.newSingleThreadExecutor()

        if (this.isPermissionGranted()) {
            this.startCamera()
        } else {
            //Request Permission
            Log.d(TAG, "Request for Permission.")
            ActivityCompat.requestPermissions(this, Permissions.REQUIRED_PERMISSION, this.requestCodePermission)
        }

        this.binding.apply {
            ViewHelper.makeClickable(this@TakePictureActivity, this.buttonTake)
        }
    }

    private fun getOutputDirectory(): File {
        val mediaDirectory = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply {
                mkdirs()
            }
        }

        return if (mediaDirectory != null && mediaDirectory.exists())
            mediaDirectory else filesDir

    }

    private fun startCamera() {
        Log.d(TAG, "Starting Camera...")
        val processCameraProvider = ProcessCameraProvider.getInstance(this)

        processCameraProvider.addListener(Runnable {
            Log.d(TAG, "Listener For Process Camera Provider")
            val cameraProvider = processCameraProvider.get()

            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(this.binding.camera.surfaceProvider)
                }

            this.imageCapture = ImageCapture.Builder().build()

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, this.imageCapture
                )
            }catch (e: Exception) {
                Log.e(TAG, e.printStackTrace().toString())
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun isPermissionGranted() = Permissions.REQUIRED_PERMISSION.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onClick(p0: View?) {
        if (p0 == this.binding.buttonTake) {
            this.takePicture()
        }
    }

    private fun takePicture() {
        Log.d(TAG, "Taking Picture...")
        progressDialog = ProgressDialog(this)
        progressDialog.setCancelable(false)
        progressDialog.setMessage("Sedang Mengambil Gambar, Pastikan Kamera Tidak Bergoyang!")
        progressDialog.setButton(ProgressDialog.BUTTON_NEGATIVE, "Batal", object : DialogInterface.OnClickListener {
            override fun onClick(p0: DialogInterface?, p1: Int) {
                GeneralHelper.move(this@TakePictureActivity, TakePictureActivity::class.java, null, true)
            }
        })

        progressDialog.show()

        val captureImage = this.imageCapture ?: return
        val photoFile = File(this.outputDirectory, SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(System.currentTimeMillis()) + ".jpg")

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        captureImage.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object :  ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    GlobalScope.launch {
                        Log.d(TAG, "Image Saved.")
                        val savedUri  = Uri.fromFile(photoFile)

                        Log.d("SavedImage", savedUri.toString())

                        val final = Compressor.compress(this@TakePictureActivity, photoFile) {
                            quality(80)
                        }

                        val requestBody = RequestBody.create(MediaType.parse("multipart/form-data;boundary"), final)
                        val multipartBody = MultipartBody.Part.createFormData("file", photoFile.name, requestBody)

                        Log.d(TAG, "Hit OCR KTP With RequestBody ${requestBody.contentType()}")


                        CompositeDisposable().add(
                            Apis.ocr(identityType, Constant.Key.API_KEY, multipartBody)
                                .subscribeWith(object : DisposableObserver<Response<OcrResponse>>() {
                                    override fun onNext(t: Response<OcrResponse>) {
                                        if (t.body() != null) {
                                            val body = t.body()

                                            if (body!!.result != null) {
                                                val result = body.result
                                                this@TakePictureActivity.result1 = result

                                                Log.d(TAG, "$result")
                                            }
                                        } else if  (t.errorBody() != null) {
                                            Log.e(TAG, t.errorBody()!!.string())
                                        }
                                    }

                                    override fun onError(e: Throwable) {
                                        progressDialog.dismiss()
                                        Log.e(TAG, e.message!!)
                                        Toast.makeText(
                                            this@TakePictureActivity,
                                            "Gagal, Silahkan Coba Kembali",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }

                                    override fun onComplete() {
                                        Log.d(TAG, "OCR $identityType onComplete();")
                                        val bundle = Bundle()
                                        bundle.putSerializable(Constant.Key.OCR_RESULT, this@TakePictureActivity.result1)
                                        bundle.putString(Constant.Key.IMAGE_URI, savedUri.toString())
                                        bundle.putLong(Constant.Key.FORM_ID, formId)
                                        bundle.putInt(Constant.Key.IDENTITY_TYPE, this@TakePictureActivity.identityType)

                                        when (identityType) {
                                            Constant.Identity.KTP -> {
                                                GeneralHelper.move(this@TakePictureActivity, KtpFormActivity::class.java, bundle, true)
                                            }
                                            Constant.Identity.SIM -> {
                                                GeneralHelper.move(this@TakePictureActivity, SimFormActivity::class.java, bundle, true)
                                            }
                                            else -> {
                                                GeneralHelper.move(this@TakePictureActivity, NpwpFormActivity::class.java, bundle, true)
                                            }
                                        }

                                    }

                                })
                        )
                    }
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e(TAG, exception.toString())
                }
            }
        )
    }
}