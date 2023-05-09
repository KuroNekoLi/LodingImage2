package com.example.loadimage2

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.google.gson.Gson

class GalleryViewModel(application: Application) : AndroidViewModel(application) {
    private val _photoListLive = MutableLiveData<List<PhotoItem>>()
    val photoListLive: LiveData<List<PhotoItem>>
        get()=_photoListLive

    fun fetchData() {
        val stringRequest = StringRequest(
            Request.Method.GET,
            getUrl(),
            {_photoListLive.value = Gson().fromJson(it,Pixabay::class.java).hits.toList() },
            { Log.d("LinLi", it.toString())}
            )
        VolleySingleton.getInstance(getApplication()).requestQueue.add(stringRequest)
    }
    private fun getUrl(): String {
        return "https://pixabay.com/api/?key=35059839-d7e76aebfe5a30bb8eff79268&q=${keyWords.random()}&image_type=photo&pretty=true&per_page=100"
    }
    private val keyWords = arrayOf("star","god","moon","sun","galaxy","cloud")
}

