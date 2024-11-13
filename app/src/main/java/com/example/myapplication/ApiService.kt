package com.example.myapplication

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("stocks")
    fun getCountries(): Call<List<Asset>>

    @GET("stocks")
    fun getAssets(): Call<List<Asset>>

    @GET("current/stock/{symbol}?interval=1d")
    fun getCurrentStockData(@Path("symbol") symbol: String): Call<StockData>

    @GET("historical/stock/{symbol}")
    fun getHistoricalStockData(
        @Path("symbol") symbol: String,
        @Query("from_date") fromDate: String,
        @Query("to_date") toDate: String,
        @Query("utc_offset") utcOffset: Int,
        @Query("interval") interval: String
    ): Call<List<JsonObject>>



    @GET("currency_crosses")
    fun getCurrencyPairs(): Call<List<CurrencyPair>>

    @GET("current/currency/{symbol}")
    fun getCurrentCurrencyData(@Path("symbol") symbol: String): Call<JsonObject>

    @GET("historical/currency/{symbol}")
    fun getHistoricalCurrencyData(
        @Path("symbol") symbol: String,
        @Query("from_date") fromDate: String,
        @Query("to_date") toDate: String,
        @Query("utc_offset") utcOffset: Int,
        @Query("interval") interval: String
    ): Call<List<JsonObject>>



    @GET("cryptocurrencies")
    fun getCryptocurrencies(): Call<List<Crypto>>

    @GET("current/crypto/{symbol}")
    fun getCurrentCryptoData(
        @Path("symbol") symbol: String
    ): Call<CryptoData>

    @GET("historical/crypto/{symbol}")
    fun getHistoricalCryptoData(
        @Path("symbol") symbol: String,
        @Query("from_date") fromDate: String,
        @Query("to_date") toDate: String,
        @Query("utc_offset") utcOffset: Int = 0,
        @Query("interval") interval: String
    ): Call<List<JsonObject>>
}