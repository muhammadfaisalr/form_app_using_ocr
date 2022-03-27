package id.muhammadfaisal.formulirapp.helper

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import id.muhammadfaisal.formulirapp.database.AppDB
import id.muhammadfaisal.formulirapp.database.dao.FormDao
import id.muhammadfaisal.formulirapp.database.dao.KtpDao
import id.muhammadfaisal.formulirapp.database.dao.NpwpDao
import id.muhammadfaisal.formulirapp.database.dao.SimDao

class DatabaseHelper {
    companion object {
        private fun connect(context: Context) : AppDB {
            return Room.databaseBuilder(context, AppDB::class.java, "form-database").allowMainThreadQueries().build()
        }

        fun formDao(context: Context) : FormDao {
            val appDb = connect(context)

            return appDb.formDao()
        }

        fun ktpDao(context: Context) : KtpDao {
            val appDb = connect(context)

            return appDb.ktpDao()
        }

        fun simDao(context: Context) : SimDao {
            val appDb = connect(context)

            return appDb.simDao()
        }

        fun npwpDao(context: Context) : NpwpDao {
            val appDb = connect(context)

            return appDb.npwpDao()
        }
    }
}