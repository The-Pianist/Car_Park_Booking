package com.example.carparkbooking.models

import android.os.Parcel
import android.os.Parcelable

data class Order(
    val createdBy:String="",
    val date:Long=0,
    val startTime:Long=0,
    val endTime:Long=0,
    var parkNum:Int=0,
    var documentId:String=""
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readLong(),
        parcel.readLong(),
        parcel.readLong(),
        parcel.readInt(),
        parcel.readString()!!
    ) {
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(createdBy)
        parcel.writeLong(date)
        parcel.writeLong(startTime)
        parcel.writeLong(endTime)
        parcel.writeInt(parkNum)
        parcel.writeString(documentId)
    }

    companion object CREATOR : Parcelable.Creator<Order> {
        override fun createFromParcel(parcel: Parcel): Order {
            return Order(parcel)
        }

        override fun newArray(size: Int): Array<Order?> {
            return arrayOfNulls(size)
        }
    }
}