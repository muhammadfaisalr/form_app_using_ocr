package id.muhammadfaisal.formulirapp.activity

import android.graphics.Bitmap
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
import id.muhammadfaisal.formulirapp.database.entity.FormNpwpEntity
import id.muhammadfaisal.formulirapp.databinding.ActivityNpwpFormBinding
import id.muhammadfaisal.formulirapp.helper.DatabaseHelper
import id.muhammadfaisal.formulirapp.helper.GeneralHelper
import id.muhammadfaisal.formulirapp.helper.ViewHelper
import id.muhammadfaisal.formulirapp.model.Form
import id.muhammadfaisal.formulirapp.utils.Constant

class NpwpFormActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityNpwpFormBinding

    private var bundle: Bundle? = null
    private var sUri: String? = null
    private var identityType: Int? = null
    private var result: Result? = null

    private var formId = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = ActivityNpwpFormBinding.inflate(this.layoutInflater)
        this.setContentView(this.binding.root)

        this.extract()
        this.init()
        this.data()
    }

    private fun data() {
        val dao = DatabaseHelper.npwpDao(this)
        val entity = dao.get(this.formId)

        if (entity != null) {
            this.binding.apply {
                val uri = entity.image
                this.imageNpwp.visibility = View.VISIBLE

                if (entity.imageType == Constant.ImageType.URI_2) {
                    this.imageNpwp.rotation = 90F
                }

                this.imageNpwp.setImageURI(uri.toUri())

                this.textNpwp.visibility = View.GONE

                this.inputName.setText(entity.name)
                this.inputNo.setText(entity.number)
                this.inputAddress.setText(entity.address)
                this.inputRegistered.setText(entity.registered)
                this.inputIdCard.setText(entity.nik)
                this.inputKpp.setText(entity.kpp)
                this.inputRegion.setText(entity.registered)

                this.buttonSave.visibility = View.GONE
                this.cardNpwp.isEnabled = false
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
                val result = this@NpwpFormActivity.result!!

                if (sUri != null) {
                    val uri = sUri!!.toUri()
                    this.imageNpwp.visibility = View.VISIBLE
                    this.imageNpwp.setImageURI(uri)
                    this.textNpwp.visibility = View.GONE
                }


                this.inputIdCard.setText(result.nik!!.value)
                if (result.nik.score!!.toInt() < 80) {
                    this@NpwpFormActivity.setError(this.inputIdCard, this.layoutIdCard)
                }

                this.inputAddress.setText(result.address!!.value)
                if (result.address.score!!.toInt() < 80) {
                    this@NpwpFormActivity.setError(this.inputAddress, this.layoutAddress)
                }

                this.inputRegion.setText(result.region!!.value)
                if (result.region.score!!.toInt() < 80) {
                    this@NpwpFormActivity.setError(this.inputRegion, this.layoutRegion)
                }

                this.inputRegistered.setText(result.registered!!.value)
                if (result.registered.score!!.toInt() < 80) {
                    this@NpwpFormActivity.setError(this.inputRegistered, this.layoutRegion)
                }

                this.inputKpp.setText(result.kpp!!.value)
                if (result.kpp.score!!.toInt() < 80) {
                    this@NpwpFormActivity.setError(this.inputKpp, this.layoutKpp)
                }

                this.inputName.setText(result.name!!.value)
                if (result.name.score!!.toInt() < 80) {
                    this@NpwpFormActivity.setError(this.inputName, this.layoutName)
                }

                this.inputNo.setText(result.no!!.value)
                if (result.no.score!!.toInt() < 80) {
                    this@NpwpFormActivity.setError(this.inputNo, this.layoutNo)
                }

            }
        }

        ViewHelper.makeClickable(this, this.binding.cardNpwp, this.binding.buttonSave)
    }

    private fun setError(input: TextInputEditText, layout: TextInputLayout) {
        input.isEnabled = true
        layout.helperText = getString(R.string.score_under_80_desc)
    }

    override fun onClick(p0: View?) {
        if (p0 == this.binding.cardNpwp) {
            val bundle = Bundle()
            bundle.putInt(Constant.Key.IDENTITY_TYPE, Constant.Identity.NPWP)
            bundle.putLong(Constant.Key.FORM_ID, this.formId)
            GeneralHelper.move(this, TakePictureActivity::class.java, bundle, true)
        } else if (p0 == this.binding.buttonSave) {
            this.save()
        }
    }

    private fun save() {
        if (this.sUri != null) {

            val npwpDao = DatabaseHelper.npwpDao(this)

            this.binding.let {
                val name = it.inputName.text.toString()
                val address = it.inputAddress.text.toString()
                val idCard = it.inputIdCard.text.toString()
                val kpp = it.inputKpp.text.toString()
                val no = it.inputNo.text.toString()
                val region = it.inputRegion.text.toString()
                val registered = it.inputRegistered.text.toString()

                val npwpEntity = FormNpwpEntity(
                    null,
                    this.formId,
                    this.sUri!!,
                    Constant.ImageType.URI,
                    name,
                    no,
                    address,
                    registered,
                    idCard,
                    kpp,
                    region
                )

                Log.d(
                    NpwpFormActivity::class.simpleName,
                    "Trying to saving NPWP Data [$npwpEntity]"
                )
                npwpDao.insert(npwpEntity)
                finish()
            }

        } else {
            Toast.makeText(this, "NPWP Belum Di Unggah", Toast.LENGTH_SHORT).show()
        }
    }
}