package com.barrycallebaut.app.models

class Responses {

    data class ResponsePetani(
        val `petani_data`: List<Petani>?,
        val `result_petani`: Petani?,
        val kode: String?,
        val pesan: String?
    )
}