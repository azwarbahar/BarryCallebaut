package com.barrycallebaut.app.models

import android.os.Parcel
import android.os.Parcelable

data class Karyawan(
    val alamat: String?,
    val created_at: String?,
    val foto: String?,
    val id: String?,
    val jenis_kelamin: String?,
    val kontak: String?,
    val koordinator: String?,
    val nama: String?,
    val no_kepegawaian: String?,
    val password: String?,
    val posisi: String?,
    val tanggal_lahir: String?,
    val tempat_lahir: String?,
    val updated_at: String?,
    val username: String?
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
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(alamat)
        parcel.writeString(created_at)
        parcel.writeString(foto)
        parcel.writeString(id)
        parcel.writeString(jenis_kelamin)
        parcel.writeString(kontak)
        parcel.writeString(koordinator)
        parcel.writeString(nama)
        parcel.writeString(no_kepegawaian)
        parcel.writeString(password)
        parcel.writeString(posisi)
        parcel.writeString(tanggal_lahir)
        parcel.writeString(tempat_lahir)
        parcel.writeString(updated_at)
        parcel.writeString(username)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Karyawan> {
        override fun createFromParcel(parcel: Parcel): Karyawan {
            return Karyawan(parcel)
        }

        override fun newArray(size: Int): Array<Karyawan?> {
            return arrayOfNulls(size)
        }
    }
}