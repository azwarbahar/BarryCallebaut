package com.barrycallebaut.app.models

import android.os.Parcel
import android.os.Parcelable

data class Petani (
    val tanggal_sensus: String?,
    val alamat: String?,
    val created_at: String?,
    val dokumentasi_sensus: String?,
    val foto: String?,
    val hasil_panen: String?,
    val id: String?,
    val id_petani: String?,
    val jarak_tanah: String?,
    val jenis_kelamin: String?,
    val jumlah_lahan: String?,
    val kakao_s1: String?,
    val kakao_s2: String?,
    val kakau_lokal: String?,
    val kecamatan: String?,
    val kelompok: String?,
    val kelurahan: String?,
    val kontak: String?,
    val luas_lahan: String?,
    val nama: String?,
    val pendidikan: String?,
    val petugas_id: String?,
    val status: String?,
    val tanggal_lahir: String?,
    val updated_at: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(tanggal_sensus)
        parcel.writeString(alamat)
        parcel.writeString(created_at)
        parcel.writeString(dokumentasi_sensus)
        parcel.writeString(foto)
        parcel.writeString(hasil_panen)
        parcel.writeString(id)
        parcel.writeString(id_petani)
        parcel.writeString(jarak_tanah)
        parcel.writeString(jenis_kelamin)
        parcel.writeString(jumlah_lahan)
        parcel.writeString(kakao_s1)
        parcel.writeString(kakao_s2)
        parcel.writeString(kakau_lokal)
        parcel.writeString(kecamatan)
        parcel.writeString(kelompok)
        parcel.writeString(kelurahan)
        parcel.writeString(kontak)
        parcel.writeString(luas_lahan)
        parcel.writeString(nama)
        parcel.writeString(pendidikan)
        parcel.writeString(petugas_id)
        parcel.writeString(status)
        parcel.writeString(tanggal_lahir)
        parcel.writeString(updated_at)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Petani> {
        override fun createFromParcel(parcel: Parcel): Petani {
            return Petani(parcel)
        }

        override fun newArray(size: Int): Array<Petani?> {
            return arrayOfNulls(size)
        }
    }
}