package com.barrycallebaut.app.models

import android.os.Parcel
import android.os.Parcelable

data class Inspeksi (
    val a1: String?,
    val a2: String?,
    val a3: String?,
    val a4: String?,
    val a5: String?,
    val a6: String?,
    val a7: String?,
    val a8: String?,
    val a9: String?,
    val created_at: String?,
    val foto: String?,
    val id: String?,
    val petani_id: String?,
    val petugas_id: String?,
    val tahun: String?,
    val tanggal: String?,
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
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(a1)
        parcel.writeString(a2)
        parcel.writeString(a3)
        parcel.writeString(a4)
        parcel.writeString(a5)
        parcel.writeString(a6)
        parcel.writeString(a7)
        parcel.writeString(a8)
        parcel.writeString(a9)
        parcel.writeString(created_at)
        parcel.writeString(foto)
        parcel.writeString(id)
        parcel.writeString(petani_id)
        parcel.writeString(petugas_id)
        parcel.writeString(tahun)
        parcel.writeString(tanggal)
        parcel.writeString(updated_at)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Inspeksi> {
        override fun createFromParcel(parcel: Parcel): Inspeksi {
            return Inspeksi(parcel)
        }

        override fun newArray(size: Int): Array<Inspeksi?> {
            return arrayOfNulls(size)
        }
    }
}