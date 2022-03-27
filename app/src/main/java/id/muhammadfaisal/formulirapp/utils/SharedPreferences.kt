package id.muhammadfaisal.formulirapp.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

class SharedPreferences {
    companion object {

        private val TAG: String = id.muhammadfaisal.formulirapp.utils.SharedPreferences::class.java.simpleName
        private var sharedPreferences: SharedPreferences? = null

        fun getSharedPreferences(context: Context): SharedPreferences? {
            if (this.sharedPreferences == null) {
                this.sharedPreferences = context.getSharedPreferences(
                    Constant.Key.SHARED_PREFERENCE_NAME,
                    Context.MODE_PRIVATE
                )
            }
            return this.sharedPreferences
        }

        fun save(context: Context?, key: String?, value: Any?) {
            if (this.sharedPreferences == null) {
                this.sharedPreferences = this.getSharedPreferences(context!!)
            }
            val editor: SharedPreferences.Editor = this.sharedPreferences!!.edit()

            when (value) {
                is String -> {
                    Log.d("SharedPreference", "String data type for $value")
                    editor.putString(key, value)
                }
                is Long -> {
                    Log.d("SharedPreference", "Long data type for $value")
                    editor.putLong(key, value)
                }
                is Boolean -> {
                    Log.d("SharedPreference", "Boolean data type for $value")
                    editor.putBoolean(key, value)
                }
                is Int -> {
                    Log.d("SharedPreference", "Int data type for $value")
                    editor.putInt(key, value)
                }
            }

            editor.apply()
        }

        fun get(context: Context, key: String?, clazz: Any): Any? {
            return when (clazz) {
                String::class.java -> {
                    Log.d("SharedPreference", "Get String value for key $key")
                    getSharedPreferences(context)!!.getString(key, null)
                }
                Long::class.java -> {
                    Log.d("SharedPreference", "Get Long value for key $key")
                    getSharedPreferences(context)!!.getLong(key, 0L)
                }
                Boolean::class.java -> {
                    Log.d("SharedPreference", "Get Boolean value for key $key")
                    getSharedPreferences(context)!!.getBoolean(key, false)
                }
                else -> {
                    Log.d("SharedPreference", "Get Int value for key $key")
                    getSharedPreferences(context)!!.getInt(key, 0)
                }
            }
        }
    }
}