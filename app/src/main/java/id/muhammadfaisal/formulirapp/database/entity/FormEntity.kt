package id.muhammadfaisal.formulirapp.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "m_form")
data class FormEntity(
    @PrimaryKey(autoGenerate = false) var id: Long = 0,
    @ColumnInfo(name = "full_name") var fullName: String = "",
    @ColumnInfo(name = "gender_id") var genderId: Int = 0,
    @ColumnInfo(name = "job") var job: String = "",
    @ColumnInfo(name = "address") var address: String = "",
    @ColumnInfo(name = "city") var city: String = "",
    @ColumnInfo(name = "phone_number") var phoneNumber: Long = 0,
    @ColumnInfo(name = "whatsapp_number") var whatsappNumber: Long = 0,
    @ColumnInfo(name = "email") var email: String = "",
    @ColumnInfo(name = "status") var status: Int = 0,
) : Serializable

@Entity(tableName = "m_form_ktp")
data class FormKtpEntity(
    @PrimaryKey(autoGenerate = true) var id: Long? = null,
    @ColumnInfo(name = "form_id") var formId: Long = 0,
    @ColumnInfo(name = "image") var image: String = "",
    @ColumnInfo(name = "image_type") var imageType: Int = 0,
    @ColumnInfo(name = "nik") var nik: String = "",
    @ColumnInfo(name = "name") var name: String = "",
    @ColumnInfo(name = "gender_id") var genderId: Int = 0,
    @ColumnInfo(name = "born_date") var bornDate: String = "",
    @ColumnInfo(name = "born_place") var bornPlace: String = "",
    @ColumnInfo(name = "address") var address: String = "",
    @ColumnInfo(name = "rt") var rt: String = "",
    @ColumnInfo(name = "rw") var rw: String = "",
    @ColumnInfo(name = "sub_district") var subDistrict: String = "",
    @ColumnInfo(name = "district") var district: String = "",
    @ColumnInfo(name = "city") var city: String = "",
    @ColumnInfo(name = "province") var province: String = "",
    @ColumnInfo(name = "religion") var religion: String = "",
    @ColumnInfo(name = "married_status") var marriedStatus: String = "",
    @ColumnInfo(name = "citizenship") var citizenship: String = "",
    @ColumnInfo(name = "job") var job: String = "",
    @ColumnInfo(name = "valid_until") var validUntil: String = "",
    @ColumnInfo(name = "release_date") var releaseDate: String = "",
    @ColumnInfo(name = "release_place") var releasePlace: String = "",
) : Serializable

@Entity(tableName = "m_form_sim")
data class FormSimEntity(
    @PrimaryKey(autoGenerate = true) var id: Long? = null,
    @ColumnInfo(name = "form_id") var formId: Long,
    @ColumnInfo(name = "image") var image: String,
    @ColumnInfo(name = "image_type") var imageType: Int,
    @ColumnInfo(name = "type") var type: String,
    @ColumnInfo(name = "sub_type") var subType: String,
    @ColumnInfo(name = "no") var number: String,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "born_place") var bornPlace: String,
    @ColumnInfo(name = "born_date") var bornDate: String,
    @ColumnInfo(name = "blood_type") var bloodType: String,
    @ColumnInfo(name = "gender_id") var genderId: Int,
    @ColumnInfo(name = "address") var address: String,
    @ColumnInfo(name = "job") var job: String,
    @ColumnInfo(name = "police") var police: String,
    @ColumnInfo(name = "valid_until") var validUntil: String,
)

@Entity(tableName = "m_form_npwp")
data class FormNpwpEntity(
    @PrimaryKey(autoGenerate = true) var id: Long? = null,
    @ColumnInfo(name = "form_id") var formId: Long,
    @ColumnInfo(name = "image") var image: String,
    @ColumnInfo(name = "image_type") var imageType: Int,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "no") var number: String,
    @ColumnInfo(name = "address") var address: String,
    @ColumnInfo(name = "registered") var registered: String,
    @ColumnInfo(name = "nik") var nik: String,
    @ColumnInfo(name = "kpp") var kpp: String,
    @ColumnInfo(name = "region") var region: String,
) : Serializable