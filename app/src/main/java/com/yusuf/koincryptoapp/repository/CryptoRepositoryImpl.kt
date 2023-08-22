package com.yusuf.koincryptoapp.repository

import com.yusuf.koincryptoapp.model.Crypto
import com.yusuf.koincryptoapp.service.CryptoAPI
import com.yusuf.koincryptoapp.util.Resource

class CryptoRepositoryImpl(private val api: CryptoAPI): CryptoRepository {



    override suspend fun downloadCryptos(): Resource<List<Crypto>> {
        return try{
            val response = api.getCryptos()
            if (response.isSuccessful){
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error("Error!",null)
            }
            else{
                Resource.error("Error!",null)
            }
        }
        catch (e: Exception){
            e.localizedMessage?.let { Resource.error(it,null) } ?: Resource.error("Something went wrong",null)
        }

    }
}