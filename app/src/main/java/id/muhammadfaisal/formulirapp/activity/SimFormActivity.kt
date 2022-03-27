package id.muhammadfaisal.formulirapp.activity

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.net.toUri
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import id.muhammadfaisal.formulirapp.R
import id.muhammadfaisal.formulirapp.api.model.Result
import id.muhammadfaisal.formulirapp.database.entity.FormSimEntity
import id.muhammadfaisal.formulirapp.databinding.ActivitySimFormBinding
import id.muhammadfaisal.formulirapp.helper.DataHelper
import id.muhammadfaisal.formulirapp.helper.DatabaseHelper
import id.muhammadfaisal.formulirapp.helper.GeneralHelper
import id.muhammadfaisal.formulirapp.helper.ViewHelper
import id.muhammadfaisal.formulirapp.utils.Constant

class SimFormActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivitySimFormBinding

    private var bundle: Bundle? = null
    private var sUri: String? = null
    private var identityType: Int? = null
    private var result: Result? = null

    private var formId = 0L


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = ActivitySimFormBinding.inflate(this.layoutInflater)
        this.setContentView(this.binding.root)

        this.extract()
        this.init()
        this.data()
    }

    private fun data() {
        val dao = DatabaseHelper.simDao(this)
        val entity = dao.get(this.formId)

        if (entity != null) {
            this.binding.apply {
                val uri = entity.image
                this.imageSim.visibility = View.VISIBLE

                if (entity.imageType == Constant.ImageType.URI_2) {
                    this.imageSim.rotation = 90F
                }

                this.imageSim.setImageURI(uri.toUri())

                this.textSim.visibility = View.GONE

                this.inputType.setText(entity.type)
                this.inputSubType.setText(entity.subType)
                this.inputNo.setText(entity.number)
                this.inputName.setText(entity.name)
                this.inputBirthPlace.setText(entity.bornPlace)
                this.inputBirthDate.setText(entity.bornDate)
                this.inputBloodType.setText(entity.bloodType)
                this.inputGender.setText(DataHelper.genderByGenderId(entity.genderId))
                this.inputAddress.setText(entity.address)
                this.inputJob.setText(entity.job)
                this.inputPolice.setText(entity.police)
                this.inputValidUntil.setText(entity.validUntil)

                this.buttonSave.visibility = View.GONE
                this.cardSIM.isEnabled = false
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
        }
    }

    private fun init() {
        this.binding.textFormId.text = formId.toString()
        if (this.result != null) {
            this.binding.apply {
                val result = this@SimFormActivity.result!!

                if (sUri != null) {
                    val uri = sUri!!.toUri()
                    this.imageSim.visibility = View.VISIBLE
                    this.imageSim.setImageURI(uri)
                    this.textSim.visibility = View.GONE
                }

                this.inputType.setText(result.type!!.value)
                if (result.type.score!!.toInt() < 80) {
                    this@SimFormActivity.setError(this.inputType, this.layoutType)
                }

                this.inputSubType.setText(result.subType!!.value)
                if (result.subType.score!!.toInt() < 80) {
                    this@SimFormActivity.setError(this.inputSubType, this.layoutSubType)
                }

                this.inputNo.setText(result.no!!.value)
                if (result.no.score!!.toInt() < 80) {
                    this@SimFormActivity.setError(this.inputNo, this.layoutNo)
                }

                this.inputName.setText(result.name!!.value)
                if (result.name.score!!.toInt() < 80) {
                    this@SimFormActivity.setError(this.inputName, this.layoutName)
                }

                this.inputBirthPlace.setText(result.birthPlace!!.value)
                if (result.birthPlace.score!!.toInt() < 80) {
                    this@SimFormActivity.setError(this.inputBirthPlace, this.layoutBirthPlace)
                }

                this.inputBirthDate.setText(result.birthDate!!.value)
                if (result.birthDate.score!!.toInt() < 80) {
                    this@SimFormActivity.setError(this.inputBirthDate, this.layoutBirthDate)
                }

                this.inputBloodType.setText(result.bloodType!!.value)
                if (result.bloodType.score!!.toInt() < 80) {
                    this@SimFormActivity.setError(this.inputBloodType, this.layoutBloodType)
                }

                this.inputGender.setText(result.gender!!.value)
                if (result.gender.score!!.toInt() < 80) {
                    this@SimFormActivity.setError(this.inputGender, this.layoutGender)
                }

                this.inputAddress.setText(result.address!!.value)
                if (result.address.score!!.toInt() < 80) {
                    this@SimFormActivity.setError(this.inputAddress, this.layoutAddress)
                }

                this.inputJob.setText(result.job!!.value)
                if (result.job.score!!.toInt() < 80) {
                    this@SimFormActivity.setError(this.inputJob, this.layoutJob)
                }

                this.inputPolice.setText(result.police!!.value)
                if (result.police.score!!.toInt() < 80) {
                    this@SimFormActivity.setError(this.inputPolice, this.layoutPolice)
                }

                this.inputValidUntil.setText(result.validUntil!!.value)
                if (result.validUntil.score!!.toInt() < 80) {
                    this@SimFormActivity.setError(this.inputValidUntil, this.layoutValidUntil)
                }
            }
        }

        ViewHelper.makeClickable(this, this.binding.cardSIM, this.binding.buttonSave)
    }

    override fun onClick(p0: View?) {
        if (p0 == this.binding.cardSIM) {
            val bundle = Bundle()
            bundle.putInt(Constant.Key.IDENTITY_TYPE, Constant.Identity.SIM)
            bundle.putLong(Constant.Key.FORM_ID, formId)
            GeneralHelper.move(this, TakePictureActivity::class.java, bundle, true)
        } else if (p0 == this.binding.buttonSave) {
            this.save()
        }
    }

    private fun save() {
        if (this.sUri != null) {
            this.binding.let {
                val simDao = DatabaseHelper.simDao(this)

                val type = it.inputType.text.toString()
                val bloodType = it.inputBloodType.text.toString()
                val subType = it.inputSubType.text.toString()
                val address = it.inputAddress.text.toString()
                val birthDate = it.inputBirthDate.text.toString()
                val birthPlace = it.inputBirthPlace.text.toString()
                val gender = it.inputGender.text.toString()
                val no = it.inputNo.text.toString()
                val name = it.inputName.text.toString()
                val validUntil = it.inputValidUntil.text.toString()
                val police = it.inputPolice.text.toString()
                val job = it.inputJob.text.toString()

                val genderId = GeneralHelper.setGenderId(gender)

                val simEntity = FormSimEntity(
                    null,
                    this.formId,
                    this.sUri!!,
                    Constant.ImageType.URI,
                    type,
                    subType,
                    no,
                    name,
                    birthPlace,
                    birthDate,
                    bloodType,
                    genderId,
                    address,
                    job,
                    police,
                    validUntil
                )

                Log.d(SimFormActivity::class.simpleName, "Try to saving SIM Data [$simEntity]")
                simDao.insert(simEntity)
                finish()
            }
        } else {
            Toast.makeText(this, "SIM Belum Di Unggah", Toast.LENGTH_SHORT).show()
        }
    }


    private fun setError(text: TextInputEditText, layout: TextInputLayout) {
        text.isEnabled = true
        layout.helperText = getString(R.string.score_under_80_desc)
    }
}