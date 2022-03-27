package id.muhammadfaisal.formulirapp.helper

import android.content.Context
import android.graphics.Typeface
import android.view.View
import android.widget.TextView
import id.muhammadfaisal.formulirapp.databinding.ActivityExportBinding

class FontHelper {
    companion object {
        private const val robotoPath ="font/roboto.ttf"
        private const val latoPath ="font/lato.ttf"
        private const val notoSansPath ="font/noto_sans.ttf"
        private const val poppinsPath ="font/poppins.ttf"

        fun setFont(context: Context, fontType: String, vararg views: View) {
            if (fontType == "Roboto") {
                for (view in views) {
                    Roboto.regular(context, view)
                }
            } else if (fontType == "Lato") {
                for (view in views) {
                    Lato.regular(context, view)
                }
            } else if (fontType == "Poppins") {
                for (view in views) {
                    Poppins.regular(context, view)
                }
            } else {
                for (view in views) {
                    NotoSans.regular(context, view)
                }
            }
        }
    }

    private class Poppins {
        companion object {
            fun regular (context: Context, vararg views: View) {
                for (view in views) {
                    if (view is TextView) {
                        view.typeface = Typeface.createFromAsset(context.assets, FontHelper.poppinsPath)
                    }
                }
            }
        }
    }

    private class Roboto {
        companion object {
            fun regular (context: Context, vararg views: View) {
                for (view in views) {
                    if (view is TextView) {
                        view.typeface = Typeface.createFromAsset(context.assets, FontHelper.robotoPath)
                    }
                }
            }
        }
    }

    private class Lato {
        companion object {
            fun regular (context: Context, vararg views: View) {
                for (view in views) {
                    if (view is TextView) {
                        view.typeface = Typeface.createFromAsset(context.assets, FontHelper.latoPath)
                    }
                }
            }
        }
    }

    private class NotoSans {
        companion object {
            fun regular (context: Context, vararg views: View) {
                for (view in views) {
                    if (view is TextView) {
                        view.typeface = Typeface.createFromAsset(context.assets, FontHelper.notoSansPath)
                    }
                }
            }
        }
    }
}