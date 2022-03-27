package id.muhammadfaisal.formulirapp.utils

class Constant {
    class Key {
        companion object {
            const val FORM = "FORM"
            const val FORM_ID = "FORM_ID"
            const val MODE = "MODE"
            const val OCR_RESULT = "OCR_RESULT"
            const val API_KEY = "bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0eXBlIjoiVVNFUiIsImlkIjoxMzUsImtleSI6MjAyMjM3ODQwNywiaWF0IjoxNjQ3MzI3NTYyfQ.eK3_Fl80vQRB8-DCRhPHzx3RIXlMBzZ6JnlQPIVOikE"
            const val BUNDLING = "BUNDLING"
            const val IDENTITY_TYPE = "IDENTITY_TYPE"
            const val CAMERA = "CAMERA"
            const val IMAGE_URI = "IMAGE_URI"
            const val SHARED_PREFERENCE_NAME = "SHARED_LOCAL"
            const val FONT = "FONT"
        }
    }

    class ImageType {
        companion object {
            const val URI = 1
            const val URI_2 = 2
        }
    }

    class Path {
        companion object {
            const val FORM = "form"
            const val KTP_FORM = "ktp-form"
            const val NPWP_FORM = "npwp-form"
            const val SIM_FORM = "sim-form"
        }
    }

    class Mode {
        companion object {
            const val VIEW = 1
            const val EDIT = 2
        }
    }

    class Status {
        companion object {
            const val LOCAL = 1001
            const val SYNCHRONIZED = 1011
        }
    }

    class URL {
        companion object {
            const val BASE_URL = "https://api.aksarakan.com"
            const val OCR_KTP = "/document/ktp"
            const val OCR_SIM = "/document/sim-2019"
            const val OCR_NPWP = "/document/npwp"
        }
    }

    class Identity {
        companion object {
            const val KTP = 1
            const val NPWP = 2
            const val SIM = 3
        }
    }

    class IdentityName {
        companion object {
            const val KTP = "KTP"
            const val NPWP = "NPWP"
            const val SIM = "SIM"
        }
    }
}