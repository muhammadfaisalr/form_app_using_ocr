package id.muhammadfaisal.formulirapp.activity

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Matrix
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.net.toUri
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import id.muhammadfaisal.formulirapp.R
import id.muhammadfaisal.formulirapp.api.model.Result
import id.muhammadfaisal.formulirapp.database.entity.FormKtpEntity
import id.muhammadfaisal.formulirapp.databinding.ActivityKtpFormBinding
import id.muhammadfaisal.formulirapp.helper.DataHelper
import id.muhammadfaisal.formulirapp.helper.DatabaseHelper
import id.muhammadfaisal.formulirapp.helper.GeneralHelper
import id.muhammadfaisal.formulirapp.helper.ViewHelper
import id.muhammadfaisal.formulirapp.listener.CameraListener
import id.muhammadfaisal.formulirapp.model.Camera
import id.muhammadfaisal.formulirapp.model.Form
import id.muhammadfaisal.formulirapp.utils.Constant
import java.net.URI

class KtpFormActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityKtpFormBinding
    private lateinit var form: Form

    private var formId: Long = 0
    private var bundle: Bundle? = null
    private var sUri: String? = null
    private var identityType: Int? = null
    private var result: Result? = null
    private var formKtpEntity: FormKtpEntity? = null

    private val TAG = KtpFormActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = ActivityKtpFormBinding.inflate(this.layoutInflater)
        this.setContentView(this.binding.root)

        this.extract()
        this.init()
        this.data()
    }

    private fun data() {
        val formKtpDao = DatabaseHelper.ktpDao(this)
        val formKtpEntity = formKtpDao.get(this.formId)

        if (formKtpEntity != null) {
            this.binding.apply {
                val uri = formKtpEntity.image
                this.imageKtp.visibility = View.VISIBLE

                if (formKtpEntity.imageType == Constant.ImageType.URI_2) {
                    this.imageKtp.minimumWidth = 0
                    this.imageKtp.minimumHeight = 0
                    this.imageKtp.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
                    this.imageKtp.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT

                    this.imageKtp.rotation = -270F
                    this.imageKtp.scaleType = ImageView.ScaleType.FIT_XY
                }

                this.imageKtp.setImageURI(uri.toUri())

                this.textKtp.visibility = View.GONE
                this.cardKTP.isEnabled = false

                this.inputIdCard.setText(formKtpEntity.nik)
                this.inputName.setText(formKtpEntity.name)
                this.inputBornDate.setText(formKtpEntity.bornDate)
                this.inputGender.setText(DataHelper.genderByGenderId(formKtpEntity.genderId))
                this.inputAddress.setText(formKtpEntity.address)
                this.inputRtRw.setText(formKtpEntity.rt + "/" + formKtpEntity.rw)
                this.inputSubDistrict.setText(formKtpEntity.subDistrict)
                this.inputDistrict.setText(formKtpEntity.district)
                this.inputReligion.setText(formKtpEntity.religion)
                this.inputMarriedStatus.setText(formKtpEntity.marriedStatus)
                this.inputJob.setText(formKtpEntity.job)
                this.inputCitizenship.setText(formKtpEntity.citizenship)
                this.inputValidUntil.setText(formKtpEntity.validUntil)

                this.buttonSave.visibility = View.GONE
            }
        }
    }

    private fun extract() {
        this.bundle = this.intent.getBundleExtra(Constant.Key.BUNDLING)

        if (this.bundle != null) {
            this.result = this.bundle!!.getSerializable(Constant.Key.OCR_RESULT) as Result?
            this.sUri = this.bundle!!.getString(Constant.Key.IMAGE_URI)
            this.identityType = this.bundle!!.getInt(Constant.Key.IDENTITY_TYPE)
            this.formId = this.bundle!!.getLong(Constant.Key.FORM_ID, 0L)

            if (this.bundle!!.getSerializable(Constant.Key.FORM) != null) {
                this.form = this.bundle!!.getSerializable(Constant.Key.FORM) as Form
            }
        }
    }

    private fun init() {
        this.binding.textFormId.text = formId.toString()
        if (this.result != null) {
            this.binding.apply {
                val result = this@KtpFormActivity.result!!

                val born = "${result.birthPlace!!.value}, ${result.birthDate!!.value}"

                this.inputIdCard.setText(result.nik!!.value)
                if (result.nik.score!!.toInt() < 80) {
                    this@KtpFormActivity.setError(this.inputIdCard, this.layoutIdCard)
                }

                this.inputName.setText(result.name!!.value)
                if (result.name.score!!.toInt() < 80) {
                    this@KtpFormActivity.setError(this.inputName, this.layoutName)
                }

                this.inputBornDate.setText(born)
                if (result.birthDate.score!!.toInt() < 80 || result.birthPlace.score!!.toInt() < 80) {
                    this@KtpFormActivity.setError(this.inputBornDate, this.layoutBorn)
                }


                this.inputGender.setText(result.gender!!.value)
                if (result.gender.score!!.toInt() < 80) {
                    this@KtpFormActivity.setError(this.inputGender, this.layoutGender)
                }

                this.inputAddress.setText(result.address!!.value)
                if (result.address.score!!.toInt() < 80) {
                    this@KtpFormActivity.setError(this.inputAddress, this.layoutAddress)
                }

                this.inputRtRw.setText("${result.rt!!.value}/${result.rw!!.value}")
                if (result.rt.score!!.toInt() < 80 || result.rw.score!!.toInt() < 80) {
                    this@KtpFormActivity.setError(this.inputRtRw, this.layoutRtRw)
                }

                this.inputSubDistrict.setText(result.subDistrict!!.value)
                if (result.subDistrict.score!!.toInt() < 80) {
                    this@KtpFormActivity.setError(this.inputSubDistrict, this.layoutSubDisctrict)
                }

                this.inputDistrict.setText(result.district!!.value)
                if (result.district.score!!.toInt() < 80) {
                    this@KtpFormActivity.setError(this.inputDistrict, this.layoutDistrict)
                }

                this.inputReligion.setText(result.religion!!.value)
                if (result.religion.score!!.toInt() < 80) {
                    this@KtpFormActivity.setError(this.inputReligion, this.layoutReligion)
                }

                this.inputMarriedStatus.setText(result.marriedStatus!!.value)
                if (result.marriedStatus.score!!.toInt() < 80) {
                    this@KtpFormActivity.setError(this.inputMarriedStatus, this.layoutMarriedStatus)
                }

                this.inputCitizenship.setText(result.citizenship!!.value)
                if (result.citizenship.score!!.toInt() < 80) {
                    this@KtpFormActivity.setError(this.inputCitizenship, this.layoutCitizenship)
                }

                this.inputJob.setText(result.job!!.value)
                if (result.job.score!!.toInt() < 80) {
                    this@KtpFormActivity.setError(this.inputJob, this.layoutJob)
                }

                this.inputValidUntil.setText(result.validUntil!!.value)
                if (result.validUntil.score!!.toInt() < 80) {
                    this@KtpFormActivity.setError(this.inputValidUntil, this.layoutValidUntil)
                }
            }
        }

        this.binding.apply {

            if (sUri != null) {
                val uri = sUri!!.toUri()
                this.imageKtp.visibility = View.VISIBLE
                this.imageKtp.setImageURI(uri)
                this.textKtp.visibility = View.GONE
            }

            ViewHelper.makeClickable(this@KtpFormActivity, this.cardKTP, this.buttonSave)
        }
    }

    private fun setError(text: TextInputEditText, layout: TextInputLayout) {
        text.isEnabled = true
        layout.helperText = getString(R.string.score_under_80_desc)
    }

    override fun onClick(p0: View?) {
        if (p0 == this.binding.cardKTP) {
            val bundle = Bundle()
            bundle.putInt(Constant.Key.IDENTITY_TYPE, Constant.Identity.KTP)
            bundle.putLong(Constant.Key.FORM_ID, this.formId)
            GeneralHelper.move(this, TakePictureActivity::class.java, bundle, true)
        } else if (p0 == this.binding.buttonSave) {
            this.save()
        }
    }

    private fun save() {
        val ktpDao = DatabaseHelper.ktpDao(this)
        if (this.sUri != null) {

            this.binding.let {
                val nik = it.inputIdCard.text.toString()
                val address = it.inputAddress.text.toString()
                val born = it.inputBornDate.text.toString()
                val citizenship = it.inputCitizenship.text.toString()
                val district = it.inputDistrict.text.toString()
                val subDistrict = it.inputSubDistrict.text.toString()
                val gender = it.inputGender.text.toString()
                val marriedStatus = it.inputMarriedStatus.text.toString()
                val religion = it.inputReligion.text.toString()
                val name = it.inputName.text.toString()
                val rtrw = it.inputRtRw.text.toString()
                val validUntil = it.inputValidUntil.text.toString()
                val job = it.inputJob.text.toString()

                val genderId = GeneralHelper.setGenderId(gender)

                val bornInfo = born.split(",")
                val bornDate = bornInfo[1].replace(" ", "")
                val bornPlace = bornInfo[0].replace(" ", "")

                val rtrwInfo = rtrw.split("/")
                val rt = rtrwInfo[0]
                val rw = rtrwInfo[1]

                val ktpEntity = FormKtpEntity(
                    null,
                    this.formId,
                    this.sUri!!,
                    Constant.ImageType.URI,
                    nik,
                    name,
                    genderId,
                    bornDate,
                    bornPlace,
                    address,
                    rt,
                    rw,
                    subDistrict,
                    district,
                    "",
                    "",
                    religion,
                    marriedStatus,
                    citizenship,
                    job,
                    validUntil,
                    "",
                    ""
                )

                Log.d(TAG, "Try to saving data [$ktpEntity]")
                ktpDao.insert(ktpEntity)
                finish()
            }

        } else {
            Toast.makeText(this, "KTP Belum Di Unggah", Toast.LENGTH_SHORT).show()
        }
    }
}