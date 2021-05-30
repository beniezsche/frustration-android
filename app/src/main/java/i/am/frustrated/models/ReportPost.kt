package i.am.frustrated.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ReportPost {

    @SerializedName("id")
    @Expose
    var post_id: String? = null
    @SerializedName("venter_id")
    @Expose
    var venter_id: String? = null
    @SerializedName("reason")
    @Expose
    var reason: String? = null
    @SerializedName("token")
    @Expose
    var token: String? = null

}