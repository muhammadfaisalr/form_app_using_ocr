package id.muhammadfaisal.formulirapp.database

import androidx.room.Database
import androidx.room.Entity
import androidx.room.RoomDatabase
import id.muhammadfaisal.formulirapp.database.dao.FormDao
import id.muhammadfaisal.formulirapp.database.dao.KtpDao
import id.muhammadfaisal.formulirapp.database.dao.NpwpDao
import id.muhammadfaisal.formulirapp.database.dao.SimDao
import id.muhammadfaisal.formulirapp.database.entity.FormEntity
import id.muhammadfaisal.formulirapp.database.entity.FormKtpEntity
import id.muhammadfaisal.formulirapp.database.entity.FormNpwpEntity
import id.muhammadfaisal.formulirapp.database.entity.FormSimEntity

@Database(entities = [FormKtpEntity::class, FormEntity::class, FormSimEntity::class, FormNpwpEntity::class], version = 1)
abstract class AppDB : RoomDatabase() {
    abstract fun formDao() : FormDao
    abstract fun ktpDao() : KtpDao
    abstract fun simDao() : SimDao
    abstract fun npwpDao() : NpwpDao
}