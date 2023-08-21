package com.yusuf.koincryptoapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yusuf.koincryptoapp.model.Crypto


import com.yusuf.koincryptoapp.service.CryptoAPI
import com.yusuf.koincryptoapp.util.API

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CryptoViewModel: ViewModel() {

    private var job: Job? = null

    val cryptoList = MutableLiveData<List<Crypto>>()
    val cryptoError = MutableLiveData<Boolean>()
    val cryptoLoading = MutableLiveData<Boolean>()

    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        println("Error: ${throwable.localizedMessage}" )

        cryptoError.value= true
    }


    fun getDataFromAPI(){

        cryptoLoading.value = true

        val retrofit = Retrofit.Builder()
            .baseUrl(API.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CryptoAPI::class.java)


        job = CoroutineScope(Dispatchers.IO +exceptionHandler).launch {
            val response = retrofit.getCryptos()

            withContext(Dispatchers.Main){
                if (response.isSuccessful){
                    cryptoLoading.value = false
                    cryptoError.value= false
                    response.body()?.let {
                            cryptoList.value= it
                        }

                    }


                }
            }

        /*
        viewModelScope.launch(Dispatchers.IO+exceptionHandler){
            val response = retrofit.getCryptos()

            withContext(Dispatchers.Main){
                if (response.isSuccessful){
                    response.body()?.let {

                    }

                }

        }*/



    }


    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }

}