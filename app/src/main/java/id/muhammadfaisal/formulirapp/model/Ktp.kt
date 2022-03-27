package id.muhammadfaisal.formulirapp.model

import java.io.Serializable

data class Ktp (
    var id: Long,
    var formId: Long,
    var ktpImage: String,
    var name: String,
    var religion: String,
    var bornPlace: String,
    var bornDate: String,
    var address: String,
    var marriedStatus: Int,
    var job: String,
    var citizenship: String,
    var validUntil: String,
) : Serializable