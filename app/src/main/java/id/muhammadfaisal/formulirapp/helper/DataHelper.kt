package id.muhammadfaisal.formulirapp.helper

import android.view.View

class DataHelper {
    companion object {
        fun validateEmptyString(vararg ss: String): Boolean {
            for (s in ss) {
                if (s.isEmpty()) {
                    return false
                }
            }

            return true
        }

        fun genderByGenderId(genderId: Int) : String {
            return if (genderId == 1) {
                "Laki-Laki"
            } else {
                "Perempuan"
            }
        }
    }
}