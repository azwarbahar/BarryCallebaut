package com.barrycallebaut.app.database.server

import com.barrycallebaut.app.models.Responses
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    // PETANI
    @GET("getPetani.php")
    fun getPetani(): Call<Responses.ResponsePetani>?

    @GET("getPetaniId.php")
    fun getPetaniId(
        @Query("id") id: String?
    ): Call<Responses.ResponsePetani>?

    @Multipart
    @POST("updatePhotoPetani.php")
    fun updatePhotoPetani(
        @Part("id") id: RequestBody?,
        @Part foto: MultipartBody.Part?
    ): Call<Responses.ResponsePetani>?

    //update one data
    @FormUrlEncoded
    @POST("updatePetaniOneData.php")
    fun updatePetaniOneData(
        @Field("id") id: String?,
        @Field("key") key: String?,
        @Field("value") value: String?
    ): Call<Responses.ResponsePetani>?

    @FormUrlEncoded
    @POST("deleteDoctSensus.php")
    fun deleteDoctSensus(
        @Field("id") id: String?
    ): Call<Responses.ResponsePetani>?


    // SENSUS
    @GET("getSensusPetaniId.php")
    fun getSensusPetaniId(
        @Query("petani_id") petani_id: String?
    ): Call<Responses.ResponseSensus>?

    @Multipart
    @POST("updatePhotoDokumentasiSensus.php")
    fun updatePhotoDokumentasiSensus(
        @Part("id") id: RequestBody?,
        @Part foto: MultipartBody.Part?
    ): Call<Responses.ResponseSensus>?


    // INSPEKSI
    @GET("getInspeksiPetaniId.php")
    fun getInspeksiPetaniId(
        @Query("petani_id") petani_id: String?
    ): Call<Responses.ResponseInspeksi>?
}