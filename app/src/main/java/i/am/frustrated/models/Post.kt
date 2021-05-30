package i.am.frustrated.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Post() : Parcelable {

    @SerializedName("id")
    @Expose
    var post_id: String? = null
    @SerializedName("venter_id")
    @Expose
    var venter_id: String? = null
    @SerializedName("post_text")
    @Expose
    var post_text: String? = null
    @SerializedName("created_at")
    @Expose
    var created_at: String? = null
    @SerializedName("mood")
    @Expose
    var mood: Int = 0
    @SerializedName("time_limit")
    @Expose
    var time_limit: String? = null
    @SerializedName("is_by_admin")
    @Expose
    var is_by_admin: Boolean? = null
    @SerializedName("is_nsfw")
    @Expose
    var is_nsfw: Boolean? = null
    @SerializedName("is_comments_enabled")
    @Expose
    var is_comments_enabled: Boolean? = null
    @SerializedName("likes")
    @Expose
    var likes: ArrayList<Like>? = null
    @SerializedName("comments")
    @Expose
    var comments: ArrayList<Comment>? = null
    @SerializedName("comment_count")
    @Expose
    var comment_count: Int = 0
    @SerializedName("token")
    @Expose
    var token: String? = null

    constructor(parcel: Parcel) : this() {
        post_id = parcel.readString()
        venter_id = parcel.readString()
        post_text = parcel.readString()
        created_at = parcel.readString()
        mood = parcel.readInt()
        time_limit = parcel.readString()
        is_by_admin = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
        is_nsfw = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
        is_comments_enabled = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
        likes = parcel.readArrayList(Like::class.java.classLoader) as ArrayList<Like>
        comments = parcel.readArrayList(Comment::class.java.classLoader) as ArrayList<Comment>
        token = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(post_id)
        parcel.writeString(venter_id)
        parcel.writeString(post_text)
        parcel.writeString(created_at)
        parcel.writeInt(mood)
        parcel.writeString(time_limit)
        parcel.writeValue(is_by_admin)
        parcel.writeValue(is_nsfw)
        parcel.writeValue(is_comments_enabled)
        parcel.writeList(likes)
        parcel.writeList(comments)
        parcel.writeString(token)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Post> {
        override fun createFromParcel(parcel: Parcel): Post {
            return Post(parcel)
        }

        override fun newArray(size: Int): Array<Post?> {
            return arrayOfNulls(size)
        }
    }


}