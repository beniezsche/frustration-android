package i.am.frustrated.networking

import i.am.frustrated.models.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface DataService {


    // Venters
    @Headers("Content-Type: application/json")
    @GET("/venters/")
    fun getVenterCredentials(): Call<Venter>

    // Posts
    @Headers("Content-Type: application/json")
    @POST("/venters/uploadusercreds/")
    fun uploadVenterCredentials(@Body venter: Venter): Call<ResponseBody>

    // Posts
    @Headers("Content-Type: application/json")
    @POST("/posts/v2/getallposts/")
    fun getAllPosts(@Body page: Page): Call<List<Post>>

    @Headers("Content-Type: application/json")
    @POST("/posts/v2/getmyposts/")
    fun getMyPosts(@Body venter: Venter): Call<List<Post>>

    @Headers("Content-Type: application/json")
    @POST("/posts/getpost/")
    fun getSinglePost(@Body post: Post): Call<Post>

    @Headers("Content-Type: application/json")
    @POST("/posts/createpost/")
    fun postPost(@Body post: Post): Call<ResponseBody>

    @Headers("Content-Type: application/json")
    @POST("/posts/deletepost/")
    fun deletePost(@Body post: Post): Call<ResponseBody>

    @Headers("Content-Type: application/json")
    @POST("/posts/blockpost/")
    fun blockPost(@Body post: Post): Call<ResponseBody>

    @Headers("Content-Type: application/json")
    @POST("/posts/reportpost/")
    fun reportPost(@Body post: ReportPost): Call<ResponseBody>

    // Likes

    @Headers("Content-Type: application/json")
    @POST("/posts/postlike/")
    fun likePost(@Body post: Post): Call<Post>

    @Headers("Content-Type: application/json")
    @POST("/posts/deletelike/")
    fun deleteLike(@Body post: Post): Call<Post>

    // Comments

    @Headers("Content-Type: application/json")
    @POST("/posts/postcomment/")
    fun postComment(@Body postComment: PostComment): Call<Post>

    @Headers("Content-Type: application/json")
    @POST("/posts/allowcomments/")
    fun toggleComments(@Body post: Post): Call<Post>




}