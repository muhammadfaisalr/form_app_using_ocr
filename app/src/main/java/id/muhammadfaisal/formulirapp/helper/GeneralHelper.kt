package id.muhammadfaisal.formulirapp.helper

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import id.muhammadfaisal.formulirapp.utils.Constant

class GeneralHelper {
    companion object {
        fun move(ctx: Context, clz: Class<*>, isFinish: Boolean) {
            this.move(ctx, clz, null, isFinish)
        }

        fun move(ctx: Context, clz: Class<*>, bundle: Bundle?, isFinish: Boolean) {
            val intent = Intent(ctx, clz)

            if (bundle != null) {
                intent.putExtra(Constant.Key.BUNDLING, bundle)
            }

            ctx.startActivity(intent)
            if (isFinish) {
                (ctx as AppCompatActivity).finish()
            }
        }

        fun setGenderId(gender: String) : Int {
            return if (gender.contains("Laki") or !gender.contains("Female")) {
                1
            } else {
                2
            }
        }
    }
}