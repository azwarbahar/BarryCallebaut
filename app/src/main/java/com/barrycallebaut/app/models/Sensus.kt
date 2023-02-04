package com.barrycallebaut.app.models

import android.os.Parcel
import android.os.Parcelable

data class Sensus (
    val created_at: String?,
    val id: String?,
    val petani_id: String?,
    val petugas_id: String?,
    val tanggal: String?,
    val updated_at: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(created_at)
        parcel.writeString(id)
        parcel.writeString(petani_id)
        parcel.writeString(petugas_id)
        parcel.writeString(tanggal)
        parcel.writeString(updated_at)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Sensus> {
        override fun createFromParcel(parcel: Parcel): Sensus {
            return Sensus(parcel)
        }

        override fun newArray(size: Int): Array<Sensus?> {
            return arrayOfNulls(size)
        }
    }
}