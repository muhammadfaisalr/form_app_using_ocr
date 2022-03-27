package id.muhammadfaisal.formulirapp.activity

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.material.button.MaterialButtonToggleGroup
import id.muhammadfaisal.formulirapp.R
import id.muhammadfaisal.formulirapp.database.FirebaseHelper
import id.muhammadfaisal.formulirapp.database.dao.KtpDao
import id.muhammadfaisal.formulirapp.database.dao.NpwpDao
import id.muhammadfaisal.formulirapp.database.dao.SimDao
import id.muhammadfaisal.formulirapp.database.entity.FormEntity
import id.muhammadfaisal.formulirapp.databinding.ActivityFormBinding
import id.muhammadfaisal.formulirapp.helper.DataHelper
import id.muhammadfaisal.formulirapp.helper.DatabaseHelper
import id.muhammadfaisal.formulirapp.helper.GeneralHelper
import id.muhammadfaisal.formulirapp.helper.ViewHelper
import id.muhammadfaisal.formulirapp.utils.Constant
import java.io.ByteArrayOutputStream

class FormActivity : AppCompatActivity(), View.OnClickListener,
    MaterialButtonToggleGroup.OnButtonCheckedListener {

    private lateinit var binding: ActivityFormBinding
    private lateinit var formEntity: FormEntity

    private lateinit var ktpDao: KtpDao
    private lateinit var simDao: SimDao
    private lateinit var npwpDao: NpwpDao

    private var isKtpUploaded = false
    private var isNpwpUploaded = false
    private var isSimUploaded = false

    private var mode = Constant.Mode.EDIT

    private var formId = 0L
    private var genderId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = ActivityFormBinding.inflate(this.layoutInflater)
        this.setContentView(this.binding.root)

        this.extract()
        this.init()
        this.check()
    }

    private fun extract() {
        val bundle = this.intent.getBundleExtra(Constant.Key.BUNDLING)

        if (bundle != null) {
            this.formId = bundle.getLong(Constant.Key.FORM_ID, 0)
            this.mode = bundle.getInt(Constant.Key.MODE, Constant.Mode.EDIT)
        } else {
            this.formId = 0
        }

    }

    private fun init() {
        val formDao = DatabaseHelper.formDao(this)

        this.ktpDao = DatabaseHelper.ktpDao(this)
        this.simDao = DatabaseHelper.simDao(this)
        this.npwpDao = DatabaseHelper.npwpDao(this)

        if (this.formId == 0L) {
            this.formId = System.currentTimeMillis()
            this.formEntity = FormEntity(this.formId, "", 1, "", "", "", 0L, 0L, "", Constant.Status.LOCAL)

            formDao.insert(this.formEntity)
        }

        if (this.mode == Constant.Mode.EDIT) {
            this.formEntity = formDao.get(this.formId)!!
        }

        this.binding.apply {

            if (mode == Constant.Mode.EDIT) {
                editMode()
            } else {
                viewMode()
            }

            this.toggleGroup.addOnButtonCheckedListener(this@FormActivity)

            ViewHelper.makeClickable(
                this@FormActivity,
                this.cardKTP,
                this.cardSim,
                this.cardNpwp,
                this.buttonProcess,
                this.buttonEdit,
                this.buttonExport,
                this.buttonUpload,
                this.buttonDelete
            )
        }
    }

    private fun editMode() {

        val formDao = DatabaseHelper.formDao(this)
        val form = formDao.get(this.formId)!!

        this.binding.let {

            if (form.email.isNotEmpty()) {
                it.inputEmail.setText(form.email)
            }

            if (form.fullName.isNotEmpty()) {
                it.inputName.setText(form.fullName)
            }

            if (form.city.isNotEmpty()) {
                it.inputCity.setText(form.city)
            }

            if (form.job.isNotEmpty()) {
                it.inputJob.setText(form.job)
            }

            if (form.address.isNotEmpty()) {
                it.inputAddress.setText(form.address)
            }

            if (form.phoneNumber != 0L) {
                it.inputPhone.setText(form.phoneNumber.toString())
            }

            if (form.whatsappNumber != 0L) {
                it.inputWhatsapp.setText(form.whatsappNumber.toString())
            }

            ViewHelper.enabled(
                it.inputEmail,
                it.inputAddress,
                it.inputCity,
                it.inputJob,
                it.inputName,
                it.inputEmail,
                it.inputPhone,
                it.inputWhatsapp,
                it.buttonMale,
                it.buttonFemale
            )

            it.layoutButton.visibility = View.GONE
        }
    }

    private fun viewMode() {

        val formDao = DatabaseHelper.formDao(this)
        val form = formDao.get(this.formId)!!
        this.binding.let {
            it.buttonProcess.visibility = View.GONE

            it.inputEmail.setText(form.email)
            it.inputAddress.setText(form.address)
            it.inputCity.setText(form.city)
            it.inputJob.setText(form.job)
            it.inputName.setText(form.fullName)
            it.inputPhone.setText(form.phoneNumber.toString())
            it.inputWhatsapp.setText(form.whatsappNumber.toString())

            if (form.genderId == 1) {
                //Male
                it.toggleGroup.check(R.id.buttonMale)
            } else {
                it.toggleGroup.check(R.id.buttonFemale)
            }

            ViewHelper.disabled(
                it.inputEmail,
                it.inputAddress,
                it.inputCity,
                it.inputJob,
                it.inputName,
                it.inputEmail,
                it.inputPhone,
                it.inputWhatsapp,
                it.buttonMale,
                it.buttonFemale
            )
        }
    }

    override fun onClick(p0: View?) {
        val bundle = Bundle()
        bundle.putLong(Constant.Key.FORM_ID, this.formId)

        when (p0) {
            this.binding.cardKTP -> {
                GeneralHelper.move(this, KtpFormActivity::class.java, bundle, false)
            }
            this.binding.cardSim -> {
                GeneralHelper.move(this, SimFormActivity::class.java, bundle, false)
            }
            this.binding.cardNpwp -> {
                GeneralHelper.move(this, NpwpFormActivity::class.java, bundle, false)
            } this.binding.buttonEdit -> {
                bundle.putInt(Constant.Key.MODE, Constant.Mode.EDIT)
                GeneralHelper.move(this, FormActivity::class.java, bundle, true)
            } this.binding.buttonExport -> {
                bundle.putInt(Constant.Key.IDENTITY_TYPE, Constant.Identity.KTP)
                GeneralHelper.move(this, ExportActivity::class.java, bundle, false)
            } this.binding.buttonUpload -> {
                this.upload()
            } this.binding.buttonDelete -> {
                this.delete()
            } else -> {
                this.save()
            }
        }
    }

    private fun delete() {
        val formDao = DatabaseHelper.formDao(this)
        val ktpDao = DatabaseHelper.ktpDao(this)
        val npwpDao = DatabaseHelper.npwpDao(this)
        val simDao = DatabaseHelper.simDao(this)

        val formEntity = formDao.get(formId)
        val ktpEntity = ktpDao.get(formId)
        val npwpEntity = npwpDao.get(formId)
        val simEntity = simDao.get(formId)

        if (formEntity != null) {
            this.binding.let {
                it.buttonDelete.text = "Menghapus..."
                ViewHelper.disabled(it.buttonUpload, it.buttonExport, it.buttonEdit, it.buttonDelete)
                FirebaseHelper
                    .formReference(formId)
                    .removeValue()
                    .addOnSuccessListener {
                        formDao.delete(formEntity)
                    }
            }

            if (ktpEntity != null) {
                FirebaseHelper
                    .ktpReference(formId)
                    .removeValue()
                    .addOnSuccessListener {
                        ktpDao.delete(ktpEntity)
                    }
            }

            if (npwpEntity != null) {
                FirebaseHelper
                    .npwpReference(formId)
                    .removeValue()
                    .addOnSuccessListener {
                        npwpDao.delete(npwpEntity)
                    }
            }

            if (simEntity != null) {
                FirebaseHelper
                    .simReference(formId)
                    .removeValue()
                    .addOnSuccessListener {
                        simDao.delete(simEntity)
                    }
            }
        }

        Handler(Looper.myLooper()!!).postDelayed({
            finish()
        }, 7000L)
    }

    private fun upload() {
        val formDao = DatabaseHelper.formDao(this)
        val ktpDao = DatabaseHelper.ktpDao(this)
        val npwpDao = DatabaseHelper.npwpDao(this)
        val simDao = DatabaseHelper.simDao(this)


        val formEntity = formDao.get(formId)
        val ktpEntity = ktpDao.get(formId)
        val npwpEntity = npwpDao.get(formId)
        val simEntity = simDao.get(formId)

        if (formEntity != null) {
            this.binding.let {
                it.buttonUpload.text = "Mengunggah..."
                ViewHelper.disabled(it.buttonExport, it.buttonExport, it.buttonEdit, it.buttonDelete)

                FirebaseHelper
                    .formReference(formId)
                    .setValue(formEntity)
                    .addOnCompleteListener { it2 ->
                        if (it2.isSuccessful) {
                            Toast.makeText(this, "Sukses Upload Ke Server!", Toast.LENGTH_SHORT).show()
                            formEntity.status = Constant.Status.SYNCHRONIZED

                            formDao.update(formEntity)

                            it.buttonUpload.text = getString(R.string.upload)
                            it.buttonUpload.isEnabled = true
                        } else {
                            Toast.makeText(this, "Gagal Upload Server", Toast.LENGTH_SHORT).show()
                            it.buttonUpload.text = getString(R.string.upload)
                            it.buttonUpload.isEnabled = true
                        }
                    }
            }

            if (ktpEntity != null) {
                val uri = Uri.parse(ktpEntity.image)
                val inputStream = contentResolver.openInputStream(uri)
                val bitmap = BitmapFactory.decodeStream(inputStream)

                val base64Image = convertUriToBase64(bitmap)

                ktpEntity.image = base64Image

                FirebaseHelper
                    .ktpReference(formId)
                    .setValue(ktpEntity)
            }

            if (npwpEntity != null) {
                val uri = Uri.parse(npwpEntity.image)
                val inputStream = contentResolver.openInputStream(uri)
                val bitmap = BitmapFactory.decodeStream(inputStream)

                val base64Image = convertUriToBase64(bitmap)

                npwpEntity.image = base64Image

                FirebaseHelper
                    .npwpReference(formId)
                    .setValue(npwpEntity)
            }

            if (simEntity != null) {
                val uri = Uri.parse(simEntity.image)
                val inputStream = contentResolver.openInputStream(uri)
                val bitmap = BitmapFactory.decodeStream(inputStream)

                val base64Image = convertUriToBase64(bitmap)

                simEntity.image = base64Image

                FirebaseHelper
                    .npwpReference(formId)
                    .setValue(simEntity)
            }
        }

    }

    private fun convertUriToBase64(bitmap: Bitmap): String {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val byteA = baos.toByteArray()

        val result = Base64.encodeToString(byteA, Base64.DEFAULT)
        Log.d(FormActivity::class.simpleName, "Compress Result $result")

        return result
    }

    private fun save() {
        Log.d(FormActivity::class.simpleName, "Processing Data.")

        val formDao = DatabaseHelper.formDao(this)
        this.binding.let {
            val name = it.inputName.text.toString()
            val address = it.inputAddress.text.toString()
            val city = it.inputCity.text.toString()
            val email = it.inputEmail.text.toString()
            val job = it.inputJob.text.toString()
            val phone = it.inputPhone.text.toString()
            val whatsapp = it.inputWhatsapp.text.toString()

            val isPassed = DataHelper.validateEmptyString(name, address, city, email, job, phone, whatsapp)

            if (isPassed) {
                // There is not have empty string

                val formEntity = this.formEntity

                formEntity.fullName = name
                formEntity.address = address
                formEntity.city = name
                formEntity.email = email
                formEntity.phoneNumber = phone.toLong()
                formEntity.whatsappNumber = whatsapp.toLong()
                formEntity.job = job
                formEntity.genderId = this.genderId

                Log.d(FormActivity::class.simpleName, "Trying to Update Data [$formEntity]")
                formDao.update(formEntity)
                finish()
            } else {
                Toast.makeText(this, "Data yang dimasukkan belum lengkap", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    override fun onButtonChecked(
        group: MaterialButtonToggleGroup?,
        checkedId: Int,
        isChecked: Boolean
    ) {
        if (checkedId == R.id.buttonMale) {
            this.genderId = 1
        } else {
            this.genderId = 2
        }
    }

    override fun onResume() {
        super.onResume()
        this.check()
    }

    private fun check() {
        this.isKtpUploaded = this.ktpDao.get(this.formId) != null
        this.isSimUploaded = this.simDao.get(this.formId) != null
        this.isNpwpUploaded = this.npwpDao.get(this.formId) != null

        this.binding.let {
            if (this.isKtpUploaded) {
                it.textKtp.text = "KTP Telah Diunggah (Klik untuk melihat)"
            }

            if (this.isSimUploaded) {
                it.textSim.text = "SIM Telah Diunggah (Klik untuk melihat)"
            }

            if (this.isNpwpUploaded) {
                it.textNpwp.text = "NPWP Telah Diunggah (Klik untuk melihat)"
            }
        }
    }
}