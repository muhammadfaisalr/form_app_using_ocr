package id.muhammadfaisal.formulirapp.database.dao

import androidx.room.*
import id.muhammadfaisal.formulirapp.database.entity.FormKtpEntity
import id.muhammadfaisal.formulirapp.database.entity.FormNpwpEntity

@Dao
interface NpwpDao {

    @Insert
    fun insert(npwpEntity: FormNpwpEntity)

    @Update
    fun update(npwpEntity: FormNpwpEntity)

    @Delete
    fun delete(npwpEntity: FormNpwpEntity)

    @Query("SELECT * FROM m_form_npwp WHERE form_id = :formId")
    fun get(formId: Long) : FormNpwpEntity?
}