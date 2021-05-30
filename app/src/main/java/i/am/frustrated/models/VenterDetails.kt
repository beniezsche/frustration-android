package i.am.frustrated.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class VenterDetails() {


    @SerializedName("fcm_token")
    @Expose
    var fcm_token: String? = null
    @SerializedName("android_id")
    @Expose
    var android_id: String? = null
}