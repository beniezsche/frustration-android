package i.am.frustrated.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import i.am.frustrated.models.Post
import i.am.frustrated.models.Venter
import i.am.frustrated.models.VenterDetails
import i.am.frustrated.networking.RetrofitClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VenterViewModel: ViewModel() {

    fun getVenterCredentials(): MutableLiveData<Venter> {

        val data = MutableLiveData<Venter>()

        RetrofitClient.dataService.getVenterCredentials().enqueue(object : Callback<Venter> {
            override fun onFailure(call: Call<Venter>, t: Throwable) {
                data.value = null
                Log.d("fail", t.localizedMessage)
            }

            override fun onResponse(call: Call<Venter>, response: Response<Venter>) {
                if (response.isSuccessful){
                    if(response.body() != null){
                        data.value = response.body()
                    }
                }
            }
        })

        return data
    }

    fun uploadVenterCredentials(venter: Venter): MutableLiveData<ResponseBody> {

        val data = MutableLiveData<ResponseBody>()

        RetrofitClient.dataService.uploadVenterCredentials(venter).enqueue(object : Callback<ResponseBody> {
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
}