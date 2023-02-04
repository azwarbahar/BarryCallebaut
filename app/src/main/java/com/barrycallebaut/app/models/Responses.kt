package com.barrycallebaut.app.models

class Responses {

    data class ResponsePetani(
        val `petani_data`: List<Petani>?,
        val `result_petani`: Petani?,
        val kode: String?,
        val pesan: String?
    )

    data class ResponseSensus(
        val `sensus_data`: List<Sensus>?,
        val `result_sensus`: Sensus?,
        val kode: String?,
        val pesan: String?
    )

    data class ResponseInspeksi(
        val `inspeksi_data`: List<Inspeksi>?,
        val `result_inspeksi`: Inspeksi?,
        val kode: String?,
        val pesan: String?
    )
}