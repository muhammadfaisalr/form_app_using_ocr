package id.muhammadfaisal.formulirapp.database.dao

import androidx.room.*
import id.muhammadfaisal.formulirapp.database.entity.FormEntity

@Dao
interface FormDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(formEntity: FormEntity)

    @Update
    fun update(formEntity: FormEntity)

    @Delete
    fun delete(formEntity: FormEntity)

    @Query("SELECT * FROM m_form")
    fun getAll() : List<FormEntity>

    @Query("SELECT * FROM m_form WHERE id = :id")
    fun get(id: Long) : FormEntity?

    @Query("SELECT * FROM m_form WHERE full_name LIKE '%' || :q || '%' OR whatsapp_number LIKE '%' || :q || '%' OR phone_number LIKE '%' || :q || '%' ")
    fun query(q: String) : List<FormEntity>
}