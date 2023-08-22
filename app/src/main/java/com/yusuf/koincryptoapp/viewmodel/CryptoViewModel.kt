package com.yusuf.koincryptoapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yusuf.koincryptoapp.model.Crypto
import com.yusuf.koincryptoapp.repository.CryptoRepository


import com.yusuf.koincryptoapp.service.CryptoAPI
import com.yusuf.koincryptoapp.util.API
import com.yusuf.koincryptoapp.util.Resource

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CryptoViewModel(
    private val repo: CryptoRepository
): ViewModel() {

    private var job: Job? = null

    val cryptoList = MutableLiveData<Resource<List<Crypto>>>()
    val cryptoError = MutableLiveData<Resource<Boolean>>()
    val cryptoLoading = MutableLiveData<Resource<Boolean>>()

    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        println("Error: ${throwable.localizedMessage}" )

        cryptoError.value= Resource.error(throwable.localizedMessage?:"Something went wrong",data = true)
    }


    fun getDataFromAPI(){

        cryptoLoading.value = Resource.loading(data = true)
        /*
                val retrofit = Retrofit.Builder()
                    .baseUrl(API.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(CryptoAPI::class.java)
                */

        job = CoroutineScope(Dispatchers.IO +exceptionHandler).launch {
           // val response = retrofit.getCryptos()
            val resource = repo.downloadCryptos()

            withContext(Dispatchers.Main){

                resource.data?.let {
                    cryptoList.value=resource
                    cryptoLoading.value = Resource.loading(data = false)
                    cryptoError.value = Resource.error("",data = false)
                }
                /*if (response.isSuccessful){
               cryptoLoading.value = false
               cryptoError.value= false
               response.body()?.let {
                       cryptoList.value= it
                   }

               }*/
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