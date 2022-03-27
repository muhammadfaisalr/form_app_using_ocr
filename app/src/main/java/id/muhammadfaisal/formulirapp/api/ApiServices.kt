package id.muhammadfaisal.formulirapp.api

import id.muhammadfaisal.formulirapp.api.model.OcrResponse
import id.muhammadfaisal.formulirapp.utils.Constant
import io.reactivex.Observable
import okhttp3.MultipartBody
import retrofit2.http.*

interface ApiServices {

    @Multipart
    @PUT(Constant.URL.OCR_KTP)
    fun ocrKtp(
        @Header("Authentication") auth: String,
        @Part file: MultipartBody.Part) : Observable<retrofit2.Response<OcrResponse>>

    @Multipart
    @PUT(Constant.URL.OCR_SIM)
    fun ocrSim(
        @Header("Authentication") auth: String,
        @Part file: MultipartBody.Part) : Observable<retrofit2.Response<OcrResponse>>

    @Multipart
    @PUT(Constant.URL.OCR_NPWP)
    fun ocrNpwp(
        @Header("Authentication") auth: String,
        @Part file: MultipartBody.Part) : Observable<retrofit2.Response<OcrResponse>>

}