package id.muhammadfaisal.formulirapp.api.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class OcrResponse(

	@field:SerializedName("result")
	val result: Result? = null,

	@field:SerializedName("name")
	val name: String? = null
)

data class Address(

	@field:SerializedName("score")
	val score: Double? = null,

	@field:SerializedName("value")
	val value: String? = null
) : Serializable

data class Rt(

	@field:SerializedName("score")
	val score: Double? = null,

	@field:SerializedName("value")
	val value: String? = null
) : Serializable

data class SubDistrict(

	@field:SerializedName("score")
	val score: Double? = null,

	@field:SerializedName("value")
	val value: String? = null
) : Serializable

data class Religion(

	@field:SerializedName("score")
	val score: Double? = null,

	@field:SerializedName("value")
	val value: String? = null
) : Serializable

data class BirthDate(

	@field:SerializedName("score")
	val score: Double? = null,

	@field:SerializedName("value")
	val value: String? = null
) : Serializable

data class ValidUntil(

	@field:SerializedName("score")
	val score: Double? = null,

	@field:SerializedName("value")
	val value: String? = null
) : Serializable

data class MarriedStatus(

	@field:SerializedName("score")
	val score: Double? = null,

	@field:SerializedName("value")
	val value: String? = null
) : Serializable

data class Nik(

	@field:SerializedName("score")
	val score: Double? = null,

	@field:SerializedName("value")
	val value: String? = null
) : Serializable

data class BloodType(

	@field:SerializedName("score")
	val score: Double? = null,

	@field:SerializedName("value")
	val value: String? = null
) : Serializable

data class Province(

	@field:SerializedName("score")
	val score: Double? = null,

	@field:SerializedName("value")
	val value: String? = null
) : Serializable

data class PublishedDate(

	@field:SerializedName("score")
	val score: Double? = null,

	@field:SerializedName("value")
	val value: String? = null
) : Serializable

data class Result(

	@field:SerializedName("provinsi")
	val province: Province? = null,

	@field:SerializedName("jenis")
	val type: Type? = null,

	@field:SerializedName("subJenis")
	val subType: SubType? = null,

	@field:SerializedName("no")
	val no: No? = null,

	@field:SerializedName("kepolisian")
	val police: Police? = null,

	@field:SerializedName("rt")
	val rt: Rt? = null,

	@field:SerializedName("kabupatenKota")
	val city: City,

	@field:SerializedName("rw")
	val rw: Rw? = null,

	@field:SerializedName("agama")
	val religion: Religion? = null,

	@field:SerializedName("wilayah")
	val region: Region? = null,

	@field:SerializedName("terdaftar")
	val registered: Registered? = null,

	@field:SerializedName("kpp")
	val kpp: Kpp? = null,

	@field:SerializedName("statusPerkawinan")
	val marriedStatus: MarriedStatus? = null,

	@field:SerializedName("tanggalLahir")
	val birthDate: BirthDate? = null,

	@field:SerializedName("tanggalDiterbitkan")
	val publishedDate: PublishedDate? = null,

	@field:SerializedName("berlakuHingga")
	val validUntil: ValidUntil? = null,

	@field:SerializedName("alamat")
	val address: Address? = null,

	@field:SerializedName("kewarganegaraan")
	val citizenship: Citizenship? = null,

	@field:SerializedName("nik")
	val nik: Nik? = null,

	@field:SerializedName("nama")
	val name: Name? = null,

	@field:SerializedName("pekerjaan")
	val job: Job? = null,

	@field:SerializedName("tempatDiterbitkan")
	val publishedPlace: PublishedPlace? = null,

	@field:SerializedName("kecamatan")
	val district: District? = null,

	@field:SerializedName("tempatLahir")
	val birthPlace: BirthPlace? = null,

	@field:SerializedName("jenisKelamin")
	val gender: Gender? = null,

	@field:SerializedName("golonganDarah")
	val bloodType: BloodType? = null,

	@field:SerializedName("kelurahanDesa")
	val subDistrict: SubDistrict? = null
): Serializable

data class Police(
	@field:SerializedName("score")
	val score: Double? = null,

	@field:SerializedName("value")
	val value: String? = null
) : Serializable

data class Kpp(
	@field:SerializedName("score")
	val score: Double? = null,

	@field:SerializedName("value")
	val value: String? = null
) : Serializable

data class Registered(
	@field:SerializedName("score")
	val score: Double? = null,

	@field:SerializedName("value")
	val value: String? = null
) : Serializable

data class Region(
	@field:SerializedName("score")
	val score: Double? = null,

	@field:SerializedName("value")
	val value: String? = null
) : Serializable

data class No(
	@field:SerializedName("score")
	val score: Double? = null,

	@field:SerializedName("value")
	val value: String? = null
): Serializable

data class SubType(
	@field:SerializedName("score")
	val score: Double? = null,

	@field:SerializedName("value")
	val value: String? = null
) : Serializable

data class Type(
	@field:SerializedName("score")
	val score: Double? = null,

	@field:SerializedName("value")
	val value: String? = null
) : Serializable

data class City(

	@field:SerializedName("score")
	val score: Double? = null,

	@field:SerializedName("value")
	val value: String? = null
) : Serializable

data class Rw(

	@field:SerializedName("score")
	val score: Double? = null,

	@field:SerializedName("value")
	val value: String? = null
) : Serializable

data class Job(

	@field:SerializedName("score")
	val score: Double? = null,

	@field:SerializedName("value")
	val value: String? = null
) : Serializable

data class BirthPlace(

	@field:SerializedName("score")
	val score: Double? = null,

	@field:SerializedName("value")
	val value: String? = null
) : Serializable

data class PublishedPlace(

	@field:SerializedName("score")
	val score: Double? = null,

	@field:SerializedName("value")
	val value: String? = null
) : Serializable

data class Gender(

	@field:SerializedName("score")
	val score: Double? = null,

	@field:SerializedName("value")
	val value: String? = null
) : Serializable

data class Citizenship(

	@field:SerializedName("score")
	val score: Double? = null,

	@field:SerializedName("value")
	val value: String? = null
) : Serializable

data class Name(

	@field:SerializedName("score")
	val score: Double? = null,

	@field:SerializedName("value")
	val value: String? = null
) : Serializable

data class District(

	@field:SerializedName("score")
	val score: Double? = null,

	@field:SerializedName("value")
	val value: String? = null
) : Serializable
