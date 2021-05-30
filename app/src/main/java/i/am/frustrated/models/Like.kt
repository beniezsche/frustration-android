package i.am.frustrated.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Like() :Parcelable {

    @SerializedName("id")
    @Expose
    var like_id: String? = null
    @SerializedName("venter_id")
    @Expose
    var venter_id: String? = null

    constructor(parcel: Parcel) : this() {
        like_id = parcel.readString()
        venter_id = parcel.readString()
    }

    override fun equals(other: Any?): Boolean {
        return if (other is Like) {
            this.venter_id.equals(other.venter_id)
        } else false
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(like_id)
        parcel.writeString(venter_id)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun hashCode(): Int {
        var result = like_id?.hashCode() ?: 0
        result = 31 * result + (venter_id?.hashCode() ?: 0)
        return result
    }

    companion object CREATOR : Parcelable.Creator<Like> {
        override fun createFromParcel(parcel: Parcel): Like {
            return Like(parcel)
        }

        override fun newArray(size: Int): Array<Like?> {
            return arrayOfNulls(size)
        }
    }


}