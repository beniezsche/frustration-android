package i.am.frustrated.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import i.am.frustrated.models.*
import i.am.frustrated.networking.RetrofitClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostsViewModel: ViewModel() {

//    val timer = Timer()
//    val PERIOD = 5000L

    fun getAllPosts(page: Page): MutableLiveData<List<Post>> {

        val data = MutableLiveData<List<Post>>()

//        timer.scheduleAtFixedRate(object : TimerTask() {
//            override fun run() {
//                RetrofitClient.dataService.getAllPosts().enqueue(object : Callback<List<Post>> {
//                    override fun onFailure(call: Call<List<Post>>, t: Throwable) {
//                        data.value = null
//                    }
//
//                    override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
//                        if (response.isSuccessful){
//                            if (response.body() != null){
//                                data.value = response.body()
//                            }
//                        }
//                    }
//
//                })
//            }
//
//        }, 0, PERIOD)

        RetrofitClient.dataService.getAllPosts(page).enqueue(object : Callback<List<Post>> {
            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                data.value = null
            }

            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                if (response.isSuccessful){

                    if (response.body() != null){
                        data.value = response.body()
                    }
                }
            }

        })

        return data
    }

//    override fun onCleared() {
//        super.onCleared()
//
//        timer.cancel()
//        timer.purge()
//    }

    fun getMyPosts(venter: Venter): MutableLiveData<List<Post>> {

        val data = MutableLiveData<List<Post>>()

        RetrofitClient.dataService.getMyPosts(venter).enqueue(object : Callback<List<Post>> {
            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                data.value = null
            }

            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                if (response.isSuccessful){
                    if (response.body() != null){
                        data.value = response.body()
                    }
                }
            }

        })

        return data
    }

    fun getSinglePost(post: Post): MutableLiveData<Post> {

        val data = MutableLiveData<Post>()

        RetrofitClient.dataService.getSinglePost(post).enqueue(object : Callback<Post> {
            override fun onFailure(call: Call<Post>, t: Throwable) {
                data.value = null
            }

            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                if (response.isSuccessful){
                    if (response.body() != null){
                        data.value = response.body()
                    }
                }
            }

        })

        return data
    }




    fun postPost(post : Post): MutableLiveData<ResponseBody> {

        val data = MutableLiveData<ResponseBody>()

        RetrofitClient.dataService.postPost(post).enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                data.value = null
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                response.code()
                if (response.code() == 200){
                    data.value = response.body()
                }
            }
        })

        return data
    }

    fun likePost(post: Post) : MutableLiveData<Post> {


        val data = MutableLiveData<Post>()

        RetrofitClient.dataService.likePost(post).enqueue(object : Callback<Post> {
            override fun onFailure(call: Call<Post>, t: Throwable) {
                data.value = null
            }

            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                response.code()
                if (response.code() == 200){
                    data.value = response.body()
                }
            }
        })

        return data

    }

    fun deleteLike(post: Post) : MutableLiveData<Post> {

        val data = MutableLiveData<Post>()

        RetrofitClient.dataService.deleteLike(post).enqueue(object : Callback<Post> {
            override fun onFailure(call: Call<Post>, t: Throwable) {
                data.value = null
            }

            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                response.code()
                if (response.code() == 200){
                    data.value = response.body()
                }
            }
        })

        return data

    }

    fun deletePost(post: Post) : MutableLiveData<ResponseBody>{

        val data = MutableLiveData<ResponseBody>()

        RetrofitClient.dataService.deletePost(post).enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                call.request()
                data.value = null
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                if (response.code() == 200){
                    data.value = response.body()
                }
                else{
                    data.value = null
                }
            }
        })

        return data

    }

    fun blockPost(post: Post) : MutableLiveData<ResponseBody>{

        val data = MutableLiveData<ResponseBody>()

        RetrofitClient.dataService.blockPost(post).enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                data.value = null
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                response.code()
                if (response.code() == 200){
                    data.value = response.body()
                }
            }
        })

        return data

    }

    fun reportPost(post: ReportPost) : MutableLiveData<ResponseBody>{

        val data = MutableLiveData<ResponseBody>()

        RetrofitClient.dataService.reportPost(post).enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                data.value = null
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                response.code()
                if (response.code() == 200){
                    data.value = response.body()
                }
            }
        })

        return data

    }

    fun postComment(postComment: PostComment) : MutableLiveData<Post> {

        val data = MutableLiveData<Post>()

        RetrofitClient.dataService.postComment(postComment).enqueue(object : Callback<Post> {
            override fun onFailure(call: Call<Post>, t: Throwable) {
                data.value = null
            }

            override fun onResponse(call: Call<Post>, response: Response<Post>) {

                if (response.code() == 200){
                    data.value = response.body()
                }
            }
        })

        return data

    }


    fun toggleComments(post: Post) : MutableLiveData<Post> {

        val data = MutableLiveData<Post>()

        RetrofitClient.dataService.toggleComments(post).enqueue(object : Callback<Post> {
            override fun onFailure(call: Call<Post>, t: Throwable) {
                data.value = null
            }

            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                Log.d("PostViewModel", response.code().toString() ) //Gson().toJson(call.request()))
                if (response.code() == 200){
                    data.value = response.body()
                }
            }
        })

        return data

    }

}