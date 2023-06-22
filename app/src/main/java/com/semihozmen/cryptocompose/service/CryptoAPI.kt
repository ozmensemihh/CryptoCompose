package com.semihozmen.cryptocompose.service

import com.semihozmen.cryptocompose.model.CryptoModel
import retrofit2.Call
import retrofit2.http.GET

interface CryptoAPI {

    // https://raw.githubusercontent.com/
    // atilsamancioglu/K21-JSONDataSet/master/crypto.json
    @GET(value="atilsamancioglu/K21-JSONDataSet/master/crypto.json")
    fun gelAll():Call<List<CryptoModel>>

}