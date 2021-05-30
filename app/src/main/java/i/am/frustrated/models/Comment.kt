package i.am.frustrated.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Comment() : Parcelable {

    @SerializedName("venter_id")
    @Expose
    var venterId: String? = null
    @SerializedName("comment_text")
    @Expose
    var commentText: String? = null
    @SerializedName("is_by_admin")
    @Expose
    var isByAdmin: Boolean = false

    constructor(parcel: Parcel) : this() {
        venterId = parcel.readString()
        commentText = parcel.readString()
        isByAdmin = parcel.readByte() != 0.toByte()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(venterId)
        parcel.writeString(commentText)
        parcel.writeByte(if (isByAdmin) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Comment> {
        override fun createFromParcel(parcel: Parcel): Comment {
            return Comment(parcel)
        }

        override fun newArray(size: Int): Array<Comment?> {
            return arrayOfNulls(size)
        }
    }


}