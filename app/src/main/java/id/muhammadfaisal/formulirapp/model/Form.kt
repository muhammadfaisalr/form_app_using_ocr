package id.muhammadfaisal.formulirapp.model

import java.io.Serializable

data class Form(
   var id: Long,
   var name: String,
   var gender: String,
   var job: String,
   var address: String,
   var city: String,
   var phone: Long,
   var whatsapp: Long,
   var email: String,
   var ktp: Ktp?,
) : Serializable