package i.am.frustrated.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Venter {

    @SerializedName("id")
    @Expose
    var venter_id: String? = null
    @SerializedName("token")
    @Expose
    var token: String? = null
    @SerializedName("is_admin")
    @Expose
    var is_admin: Boolean = false
    @SerializedName("fcm_token")
    @Expose
    var fcm_token: String? = null
    @SerializedName("android_id")
    @Expose
    var android_id: String? = null

}