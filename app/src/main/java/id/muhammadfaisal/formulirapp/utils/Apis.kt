package id.muhammadfaisal.formulirapp.utils

import id.muhammadfaisal.formulirapp.api.ApiServices
import id.muhammadfaisal.formulirapp.api.model.OcrResponse
import id.muhammadfaisal.formulirapp.api.tools.RetrofitBuilder
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import retrofit2.Response

class Apis {
    companion object {
        private fun getServices() : ApiServices {
            return RetrofitBuilder.getRetrofit().create(ApiServices::class.java)
        }

        fun ocr(identityType: Int, auth:String, image: MultipartBody.Part): Observable<Response<OcrResponse>> {
            when (identityType) {
                Constant.Identity.KTP -> {
                    return getServices()
                        .ocrKtp(auth, image)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                }
                Constant.Identity.SIM -> {
                    return getServices()
                        .ocrSim(auth, image)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                }
                else -> {
                    return getServices()
                        .ocrNpwp(auth, image)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                }
            }
        }
    }
}