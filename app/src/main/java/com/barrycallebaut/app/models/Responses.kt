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

    data class ResponseKaryawan(
        val `karyawan_data`: List<Karyawan>?,
        val `result_karyawan`: Karyawan?,
        val `data`: Karyawan?,
        val kode: String?,
        val pesan: String?
    )

    data class ResponsePenilaian(
        val kode: String?,
        val pesan: String?,
        val nilai: String?
    )

    data class ResponseJumlahSensus(
        val kode: String?,
        val pesan: String?,
        val sudah_sensus: String?,
        val belum_sensus: String?,
        val suspended: String?
    )
}