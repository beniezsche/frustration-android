package i.am.frustrated.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PostComment {


    @SerializedName("id")
    @Expose
    var post_id: String? = null
    @SerializedName("venter_id")
    @Expose
    var venter_id: String? = null
    @SerializedName("comment_text")
    @Expose
    var comment_text: String? = null
    @SerializedName("token")
    @Expose
    var token: String? = null



}