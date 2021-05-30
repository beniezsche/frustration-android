package i.am.frustrated.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Page() {

    @SerializedName("id")
    @Expose
    var venter_id: String? = null
    @SerializedName("token")
    @Expose
    var token: String? = null
    @SerializedName("mood")
    @Expose
    var mood: Int = 0
    @SerializedName("nsfw_allowed")
    @Expose
    var nsfw_allowed: Boolean? = null
    @SerializedName("page")
    @Expose
    var page: Int? = null

}