package id.muhammadfaisal.formulirapp.activity

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import id.muhammadfaisal.formulirapp.R
import id.muhammadfaisal.formulirapp.adapter.FormAdapter
import id.muhammadfaisal.formulirapp.database.FirebaseHelper
import id.muhammadfaisal.formulirapp.database.dao.FormDao
import id.muhammadfaisal.formulirapp.database.entity.FormEntity
import id.muhammadfaisal.formulirapp.database.entity.FormKtpEntity
import id.muhammadfaisal.formulirapp.databinding.ActivityMainBinding
import id.muhammadfaisal.formulirapp.helper.DatabaseHelper
import id.muhammadfaisal.formulirapp.helper.GeneralHelper
import id.muhammadfaisal.formulirapp.helper.ViewHelper
import id.muhammadfaisal.formulirapp.utils.Constant
import java.io.ByteArrayOutputStream

class MainActivity : AppCompatActivity(), View.OnClickListener, TextWatcher,
    SwipeRefreshLayout.OnRefreshListener, View.OnTouchListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var formDao: FormDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = ActivityMainBinding.inflate(this.layoutInflater)
        this.setContentView(this.binding.root)

        this.init()
        this.setup()
    }

    private fun setup() {

        this.binding.let {
            it.recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
            it.recyclerView.adapter = FormAdapter(this, this.formDao.getAll())
            it.recyclerView.addItemDecoration(DividerItemDecoration(this, RecyclerView.VERTICAL))

            it.swipe.setOnRefreshListener(this)
        }
    }

    private fun init() {
        this.formDao = DatabaseHelper.formDao(this)

        this.binding.apply {
            ViewHelper.makeClickable(this@MainActivity, this.exfabAdd)

            this.inputSearch.addTextChangedListener(this@MainActivity)
        }
    }

    override fun onClick(p0: View?) {
        if (p0 == this.binding.exfabAdd) {
            val bundle = Bundle()
            bundle.putInt(Constant.Key.MODE, Constant.Mode.EDIT)
            GeneralHelper.move(this, FormActivity::class.java, bundle, false)
        }
    }

    override fun onResume() {
        super.onResume()
        this.setup()
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun afterTextChanged(p0: Editable?) {
        if (p0 != null) {
            if (p0.length > 1) {
                val image = resources.getDrawable(R.drawable.ic_baseline_close_24, null)
                image.setBounds(0, 0, 60, 60)
                this.binding.inputSearch.setCompoundDrawables(null, null, image, null)
                this.binding.inputSearch.setOnTouchListener(this)
                this.binding.let {
                    it.recyclerView.adapter = FormAdapter(this, this.formDao.query(p0.toString()))
                }
            } else {
                this.binding.inputSearch.setCompoundDrawables(null, null, null, null)
                setup()
            }
        }
    }

    override fun onRefresh() {
        this.binding.swipe.isRefreshing = true
        this.refresh()
    }

    private fun refresh() {
        FirebaseHelper
            .formReference()
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (shot in snapshot.children) {
                        val form = shot.getValue(FormEntity::class.java)

                        var i = 1
                        if (form != null) {
                            Log.d(MainActivity::class.java.simpleName, "$i Data Update")
                            formDao.insert(form)

                            i += 1
                        }

                    }

                    FirebaseHelper
                        .ktpReference()
                        .addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val ktpDao = DatabaseHelper.ktpDao(this@MainActivity)
                                for (shot in snapshot.children) {
                                    val form = shot.getValue(FormKtpEntity::class.java)

                                    var i = 1
                                    if (form != null) {
                                        Log.d(MainActivity::class.java.simpleName, "$i KTP Data Update")

                                        val decodedBase64 = Base64.decode(form.image, Base64.DEFAULT)
                                        val bitmap = BitmapFactory.decodeByteArray(decodedBase64, 0, decodedBase64.size)
                                        val imageUri = getImageUri(bitmap)


                                        form.imageType = Constant.ImageType.URI_2
                                        form.image = imageUri.toString()

                                        ktpDao.insert(form)

                                        i += 1
                                    }

                                }
                            }
                            override fun onCancelled(error: DatabaseError) {
                            }

                        })

                    binding.swipe.isRefreshing = false
                    setup()
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

    }

    private fun getImageUri(bitmap: Bitmap): Uri {
        val bous = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bous)
        val path = MediaStore.Images.Media.insertImage(this.contentResolver, bitmap, System.currentTimeMillis().toString(), null)

        return Uri.parse(path)
    }

    override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
        if (p1!!.action == MotionEvent.ACTION_UP) {
            if (p1.rawX >= (this.binding.inputSearch.right - this.binding.inputSearch.compoundDrawables[2].bounds.width())) {
                this.binding.inputSearch.setText("")
                return false
            }
        }

        return true
    }
}