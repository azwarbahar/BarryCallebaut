package com.barrycallebaut.app.database.server

import com.barrycallebaut.app.models.Responses
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {

    // PETANI
    @GET("getPetani.php")
    fun getPetani(): Call<Responses.ResponsePetani>?

}