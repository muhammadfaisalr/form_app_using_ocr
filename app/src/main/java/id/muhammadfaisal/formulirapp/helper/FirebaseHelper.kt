package id.muhammadfaisal.formulirapp.helper

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import id.muhammadfaisal.formulirapp.utils.Constant

class FirebaseHelper {
    companion object {
        fun formReference(formId: Long): DatabaseReference {
            return FirebaseDatabase.getInstance().getReference(Constant.Path.FORM).child(formId.toString())
        }

        fun formReference(): DatabaseReference {
            return FirebaseDatabase.getInstance().getReference(Constant.Path.FORM)
        }

        fun ktpReference(formId: Long) : DatabaseReference {
            return FirebaseDatabase.getInstance().getReference(Constant.Path.KTP_FORM).child(formId.toString())
        }

        fun ktpReference() : DatabaseReference {
            return FirebaseDatabase.getInstance().getReference(Constant.Path.KTP_FORM)
        }

        fun simReference(formId: Long) : DatabaseReference {
            return FirebaseDatabase.getInstance().getReference(Constant.Path.SIM_FORM).child(formId.toString())
        }

        fun npwpReference(formId: Long) : DatabaseReference {
            return FirebaseDatabase.getInstance().getReference(Constant.Path.NPWP_FORM).child(formId.toString())
        }
    }
}