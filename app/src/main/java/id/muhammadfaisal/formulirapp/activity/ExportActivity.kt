package id.muhammadfaisal.formulirapp.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.get
import id.muhammadfaisal.formulirapp.R
import id.muhammadfaisal.formulirapp.databinding.ActivityExportBinding
import id.muhammadfaisal.formulirapp.databinding.LayoutKtpBinding
import id.muhammadfaisal.formulirapp.databinding.LayoutNpwpBinding
import id.muhammadfaisal.formulirapp.databinding.LayoutSimBinding
import id.muhammadfaisal.formulirapp.helper.DataHelper
import id.muhammadfaisal.formulirapp.helper.DatabaseHelper
import id.muhammadfaisal.formulirapp.helper.FontHelper
import id.muhammadfaisal.formulirapp.helper.ViewHelper
import id.muhammadfaisal.formulirapp.utils.Constant
import id.muhammadfaisal.formulirapp.utils.SharedPreferences
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.util.*
import kotlin.collections.ArrayList

class ExportActivity : AppCompatActivity(),
    AdapterView.OnItemClickListener, View.OnClickListener {

    private lateinit var binding: ActivityExportBinding

    private lateinit var fonts: ArrayList<String>

    private var formId: Long = 0
    private var identityType: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = ActivityExportBinding.inflate(this.layoutInflater)
        this.setContentView(this.binding.root)

        this.extract()
        this.init()
        this.setupDocuments()
        this.setupFonts()
        this.data()
    }

    private fun setupDocuments() {
        val strings: ArrayList<String> = ArrayList()

        val ktpDao = DatabaseHelper.ktpDao(this)
        val simDao = DatabaseHelper.simDao(this)
        val npwpDao = DatabaseHelper.npwpDao(this)

        val isKtpReady = ktpDao.get(formId) != null
        val isSimReady = simDao.get(formId) != null
        val isNpwpReady = npwpDao.get(formId) != null

        if (isKtpReady) {
            strings.add(Constant.IdentityName.KTP)
        }

        if (isSimReady) {
            strings.add(Constant.IdentityName.SIM)
        }

        if (isNpwpReady) {
            strings.add(Constant.IdentityName.NPWP)
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, strings)
        this.binding.inputDocument.setAdapter(adapter)
    }

    private fun data() {
        if (this.identityType == Constant.Identity.KTP) {
            val ktpBinding = this.binding.layoutKtp

            this.processKtp(ktpBinding)
        } else if (this.identityType == Constant.Identity.NPWP) {
            val npwpBinding = this.binding.layoutNpwp

            this.processNpwp(npwpBinding)
        } else if (this.identityType == Constant.Identity.SIM){
            val simBinding = this.binding.layoutSim
            this.processSim(simBinding)
        }
    }

    private fun processSim(simBinding: LayoutSimBinding) {
        val simDao = DatabaseHelper.simDao(this)
        val sim = simDao.get(this.formId)

        this.binding.apply {
            this.layoutSim.linearParent.visibility = View.VISIBLE
            this.layoutKtp.linearKtp.visibility = View.GONE
            this.layoutNpwp.linearParent.visibility = View.GONE
        }

        if (sim != null) {
            simBinding.apply {
                this.textAddress.text = sim.address
                this.textBirthDate.text = sim.bornDate
                this.textBirthPlace.text = sim.bornPlace
                this.textBloodType.text = sim.bloodType
                this.textGender.text = DataHelper.genderByGenderId(sim.genderId)
                this.textJob.text = sim.job
                this.textName.text = sim.name
                this.textNumber.text = sim.number
                this.textPolice.text = sim.police
                this.textType.text = sim.type
                this.textSubType.text = sim.subType
                this.textValidUntil.text = sim.validUntil
            }
        }
    }

    private fun processNpwp(npwpBinding: LayoutNpwpBinding) {
        val npwpDao = DatabaseHelper.npwpDao(this)
        val npwp = npwpDao.get(this.formId)

        this.binding.apply {
            this.layoutSim.linearParent.visibility = View.GONE
            this.layoutKtp.linearKtp.visibility = View.GONE
            this.layoutNpwp.linearParent.visibility = View.VISIBLE
        }

        if (npwp != null) {
            npwpBinding.apply {
                this.textAddress.text = npwp.address
                this.textIdNpwp.text = npwp.number
                this.textKpp.text = npwp.kpp
                this.textName.text = npwp.name
                this.textRegion.text = npwp.region
                this.textRegistered.text = npwp.registered
            }
        }
    }

    private fun processKtp(ktpBinding: LayoutKtpBinding) {
        val ktpDao = DatabaseHelper.ktpDao(this)
        val ktp = ktpDao.get(this.formId)

        this.binding.apply {
            this.layoutSim.linearParent.visibility = View.GONE
            this.layoutKtp.linearKtp.visibility = View.VISIBLE
            this.layoutNpwp.linearParent.visibility = View.GONE
        }

        if (ktp != null) {
            ktpBinding.apply {

                this.textNik.text = ktp.nik
                this.textName.text = ktp.name
                this.textGender.text = ktp.genderId.toString()
                this.textAddress.text = ktp.address
                this.textRtRw.text = "${ktp.rt}/${ktp.rw}"
                this.textSubDistrict.text = ktp.subDistrict
                this.textDistrict.text = ktp.district
                this.textReligion.text = ktp.religion
                this.textMarriedStatus.text = ktp.marriedStatus
                this.textJob.text = "Wiraswasta"
                this.textCitizenship.text = ktp.citizenship
                this.textValidUntil.text = ktp.validUntil
            }
        }
    }

    private fun extract() {
        val bundle = this.intent.getBundleExtra(Constant.Key.BUNDLING)

        if (bundle != null) {
            this.formId = bundle.getLong(Constant.Key.FORM_ID, 0)
        }
    }

    private fun init() {
        this.fonts = ArrayList()

        val fontLocal = SharedPreferences.get(this, Constant.Key.FONT, String::class.java)

        if (fontLocal != null) {
            this.binding.inputFont.setText(fontLocal.toString())
            FontHelper.setFont(this@ExportActivity, fontLocal.toString(), binding.layoutKtp.textName, binding.layoutKtp.textGender, binding.layoutKtp.textNik)
        }

        this.binding.apply {
            this.inputFont.setOnItemClickListener { adapterView, view, i, l ->
                if (adapterView != null) {
                    val fontName = adapterView.getItemAtPosition(i).toString()
                    SharedPreferences.save(this@ExportActivity, Constant.Key.FONT, fontName)
                    FontHelper.setFont(this@ExportActivity, fontName, binding.layoutKtp.textName, binding.layoutKtp.textGender, binding.layoutKtp.textNik)
                }
            }

            this.inputDocument.setOnKeyListener(null)
            this.inputDocument.onItemClickListener = this@ExportActivity

            ViewHelper.makeClickable(this@ExportActivity, this.buttonExport)
        }
    }

    private fun setupFonts() {
        this.fonts.add("Lato")
        this.fonts.add("Roboto")
        this.fonts.add("Poppins")
        this.fonts.add("Noto-Sans")

        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, this.fonts)
        this.binding.inputFont.setAdapter(arrayAdapter)
    }

    override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        if (p0 != null) {
            val value = p0.getItemAtPosition(p2).toString()

            Log.d(ExportActivity::class.simpleName, "Item Selected is $value")

            if (value == Constant.IdentityName.KTP) {
                this.identityType = Constant.Identity.KTP
            } else if (value == Constant.IdentityName.NPWP) {
                this.identityType = Constant.Identity.NPWP
            } else {
                this.identityType = Constant.Identity.SIM
            }

            this.data()
        }
    }

    override fun onClick(p0: View?) {
        if (p0 == this.binding.buttonExport) {
            this.export()
        }
    }

    private fun export() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 401)
            this.export()
        } else {


            this.binding.cardView.isDrawingCacheEnabled = true
            val b: Bitmap = this.binding.cardView.drawingCache
            val random = Random().nextInt(100000 - 100 + 1) + 100000
            val root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val location = "/data-share-$random.png"
            val path = root.toString() + location
            val imageDir = File(root, location)
            try {
                b.compress(Bitmap.CompressFormat.PNG, 95, FileOutputStream(path))
                Log.d(ExportActivity::class.java.simpleName, "===== Success Share Image")
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
                Log.d(
                    ExportActivity::class.java.simpleName,
                    "===== Err When Share Image ${e.message}"
                )
            }
            val finalPath = FileInputStream(File(path))
            finalPath.close()
            val i = Intent()
            i.action = Intent.ACTION_SEND
            i.type = "image/png"
            i.putExtra(Intent.EXTRA_STREAM, Uri.parse(imageDir.absolutePath))
            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            startActivity(Intent.createChooser(i, "Bagikan"))
        }
    }
}