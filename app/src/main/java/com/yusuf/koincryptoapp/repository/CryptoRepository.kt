package com.yusuf.koincryptoapp.repository

import com.yusuf.koincryptoapp.model.Crypto
import com.yusuf.koincryptoapp.util.Resource

interface CryptoRepository {
    suspend fun downloadCryptos() : Resource<List<Crypto>>
}