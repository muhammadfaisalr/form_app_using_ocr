package id.muhammadfaisal.formulirapp.database.dao

import androidx.room.*
import id.muhammadfaisal.formulirapp.database.entity.FormKtpEntity
import id.muhammadfaisal.formulirapp.database.entity.FormNpwpEntity
import id.muhammadfaisal.formulirapp.database.entity.FormSimEntity

@Dao
interface SimDao {

    @Insert
    fun insert(simEntity: FormSimEntity)

    @Update
    fun update(simEntity: FormSimEntity)

    @Delete
    fun delete(simEntity: FormSimEntity)

    @Query("SELECT * FROM m_form_sim WHERE form_id = :formId")
    fun get(formId: Long) : FormSimEntity?
}