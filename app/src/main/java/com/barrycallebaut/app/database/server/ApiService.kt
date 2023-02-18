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

    @GET("getPetaniPetugasId.php")
    fun getPetaniPetugasId(
        @Query("petugas_id") petugas_id: String?
    ): Call<Responses.ResponsePetani>?

    @GET("getPetaniDeportasi.php")
    fun getPetaniDeportasi(
        @Query("petugas_id") petugas_id: String?,
        @Query("role") role: String?
    ): Call<Responses.ResponsePetani>?

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


    @Multipart
    @POST("addInspeksi.php")
    fun addInspeksi(
        @Part("tanggal") tanggal: RequestBody?,
        @Part("tahun") tahun: RequestBody?,
        @Part("petani_id") petani_id: RequestBody?,
        @Part("petugas_id") petugas_id: RequestBody?,
        @Part("a1") a1: RequestBody?,
        @Part("a2") a2: RequestBody?,
        @Part("a3") a3: RequestBody?,
        @Part("a4") a4: RequestBody?,
        @Part("a5") a5: RequestBody?,
        @Part("a6") a6: RequestBody?,
        @Part("a7") a7: RequestBody?,
        @Part("a8") a8: RequestBody?,
        @Part("a9") a9: RequestBody?,
        @Part foto: MultipartBody.Part?
    ): Call<Responses.ResponseInspeksi>?


    // PENILAIAN PETANI
    @GET("getPenilaianPetani.php")
    fun getPenilaianPetani(
        @Query("petani_id") petani_id: String?
    ): Call<Responses.ResponsePenilaian>?


    // JUMLAH SENSUS PETANI
    @GET("getJumlahSensus.php")
    fun getJumlahSensus(
        @Query("role") role: String?,
        @Query("petugas_id") petugas_id: String?
    ): Call<Responses.ResponseJumlahSensus>?

    // LOGIN
    @GET("login.php")
    fun login(
        @Query("username") username: String?,
        @Query("password") password: String?
    ): Call<Responses.ResponseKaryawan>?


    // KARYAWAN
    @GET("getKaryawanId.php")
    fun getKaryawanId(
        @Query("id") id: String?
    ): Call<Responses.ResponseKaryawan>?

    @GET("getKaryawanPosisi.php")
    fun getKaryawanPosisi(
        @Query("posisi") posisi: String?
    ): Call<Responses.ResponseKaryawan>?

    @Multipart
    @POST("updatePhotoKaryawan.php")
    fun updatePhotoKaryawan(
        @Part("id") id: RequestBody?,
        @Part foto: MultipartBody.Part?
    ): Call<Responses.ResponseKaryawan>?

    // UBAH PASSWORD
    @GET("updatePasswrd.php")
    fun updatePasswrd(
        @Query("id") id: String?,
        @Query("password_lama") password_lama: String?,
        @Query("password_baru") password_baru: String?
    ): Call<Responses.ResponseKaryawan>?

    //update Karyawan one data
    @FormUrlEncoded
    @POST("updateKaryawanOneData.php")
    fun updateKaryawanOneData(
        @Field("id") id: String?,
        @Field("key") key: String?,
        @Field("value") value: String?
    ): Call<Responses.ResponseKaryawan>?

}