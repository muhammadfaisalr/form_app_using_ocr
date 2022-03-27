package id.muhammadfaisal.formulirapp.database.dao

import androidx.room.*
import id.muhammadfaisal.formulirapp.database.entity.FormKtpEntity

@Dao
interface KtpDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(ktpEntity: FormKtpEntity)

    @Update
    fun update(ktpEntity: FormKtpEntity)

    @Delete
    fun delete(ktpEntity: FormKtpEntity)

    @Query("SELECT * FROM m_form_ktp WHERE form_id = :formId")
    fun get(formId: Long) : FormKtpEntity?
}