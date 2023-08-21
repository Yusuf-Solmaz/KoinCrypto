package com.yusuf.koincryptoapp.service

import com.yusuf.koincryptoapp.model.Crypto
import com.yusuf.koincryptoapp.util.API.API_KEY

import retrofit2.Response
import retrofit2.http.GET

interface CryptoAPI {

    //

    @GET(API_KEY)
    suspend fun getCryptos(): Response<List<Crypto>>

}