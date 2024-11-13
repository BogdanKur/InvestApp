package com.example.myapplication

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myapplication.databinding.FragmentMainBinding
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.example.myapplication.MainFragment.Companion.symbolTHIS
import com.github.mikephil.charting.charts.CandleStickChart
import com.github.mikephil.charting.formatter.IValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ViewPortHandler
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.Locale
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.CandleEntry
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.JsonObject
import java.util.*

class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var assetAdapter: AssetAdapter
    private lateinit var assetSearchAdapter: AssetAdapter
    private lateinit var countryAdapter: CountryAdapter
    private lateinit var apiService: ApiService
    private lateinit var databaseHelper: FavoritesDatabaseHelper
    private var currentCountry = ""
    var interval = "1wk"
    var currentAsset: Asset? = null
    companion object{
        var symbolTHIS = "TS"
    }
    var nameFavorite = ""
    var symbolFavorite = ""
    var fullNameFavorite = ""
    var isinFavorite = ""
    var currencyFavorite = ""
    var countryFavorite = ""
    lateinit var bottomNav: BottomNavigationView
    lateinit var uniqueAsset: List<Asset>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        val view = binding.root
        val navController = findNavController()
        databaseHelper = FavoritesDatabaseHelper(requireContext())
        arguments.let { bundle ->
            Log.e("fdsvc", nameFavorite)
            if(nameFavorite != "null1") {
                nameFavorite = bundle?.getString("nameAsset").toString()
                symbolFavorite = bundle?.getString("symbolAsset").toString()
                fullNameFavorite = bundle?.getString("fullNameAsset").toString()
                countryFavorite = bundle?.getString("countryAsset").toString()
                currencyFavorite = bundle?.getString("currencyAsset").toString()
                isinFavorite = bundle?.getString("isinAsset").toString()
            }
        }
        val res = requireActivity().getDrawable(R.drawable.item_country_asset_background_choose)
        val res1 = requireActivity().getDrawable(R.drawable.item_country_asset_background)

        binding.btn1m.setOnClickListener {
            binding.btn1m.background = res
            binding.btn5m.background = res1
            binding.btn15m.background = res1
            binding.btn30m.background = res1
            binding.btn1h.background = res1
            binding.btn90m.background = res1
            binding.btn1w.background = res1
            binding.btn1mo.background = res1
            binding.btn90m.background = res1
            interval = "1m"
            currentAsset?.let { loadAll(it) }
            loadHistoryGraph()
        }
        binding.btn5m.setOnClickListener {
            interval = "5m"
            currentAsset?.let { loadAll(it) }
            loadHistoryGraph()
            binding.btn1m.background = res1
            binding.btn5m.background = res
            binding.btn15m.background = res1
            binding.btn30m.background = res1
            binding.btn1h.background = res1
            binding.btn90m.background = res1
            binding.btn1w.background = res1
            binding.btn1mo.background = res1
            binding.btn90m.background = res1}
        binding.btn15m.setOnClickListener {
            interval = "15m"
            currentAsset?.let { loadAll(it) }
            loadHistoryGraph()
            binding.btn1m.background = res1
            binding.btn5m.background = res1
            binding.btn15m.background = res
            binding.btn30m.background = res1
            binding.btn1h.background = res1
            binding.btn90m.background = res1
            binding.btn1w.background = res1
            binding.btn1mo.background = res1
            binding.btn90m.background = res1}
        binding.btn30m.setOnClickListener {
            interval = "30m"
            currentAsset?.let { loadAll(it) }
            loadHistoryGraph()
            binding.btn1m.background = res1
            binding.btn5m.background = res1
            binding.btn15m.background = res1
            binding.btn30m.background = res
            binding.btn1h.background = res1
            binding.btn90m.background = res1
            binding.btn1w.background = res1
            binding.btn1mo.background = res1
            binding.btn90m.background = res1}
        binding.btn90m.setOnClickListener {
            interval = "90m"
            currentAsset?.let { loadAll(it) }
            loadHistoryGraph()
            binding.btn1m.background = res1
            binding.btn5m.background = res1
            binding.btn15m.background = res1
            binding.btn30m.background = res1
            binding.btn1h.background = res1
            binding.btn90m.background = res
            binding.btn1w.background = res1
            binding.btn1mo.background = res1
            binding.btn90m.background = res1
        }
        binding.btn1h.setOnClickListener {
            interval = "1h"
            currentAsset?.let { loadAll(it) }
            loadHistoryGraph()
            binding.btn1m.background = res1
            binding.btn5m.background = res1
            binding.btn15m.background = res1
            binding.btn30m.background = res1
            binding.btn1h.background = res
            binding.btn90m.background = res1
            binding.btn1w.background = res1
            binding.btn1mo.background = res1
            binding.btn90m.background = res1 }
        binding.btn1d.setOnClickListener {
            interval = "1d"
            currentAsset?.let { loadAll(it) }
            loadHistoryGraph()
            binding.btn1m.background = res1
            binding.btn5m.background = res1
            binding.btn15m.background = res1
            binding.btn30m.background = res1
            binding.btn1h.background = res1
            binding.btn1w.background = res1
            binding.btn1mo.background = res1
            binding.btn90m.background = res1
            binding.btn1d.background = res}
        binding.btn1w.setOnClickListener {
            interval = "1wk"
            currentAsset?.let { loadAll(it) }
            loadHistoryGraph()
            binding.btn1m.background = res1
            binding.btn5m.background = res1
            binding.btn15m.background = res1
            binding.btn30m.background = res1
            binding.btn1h.background = res1
            binding.btn1w.background = res
            binding.btn1mo.background = res1
            binding.btn90m.background = res1
            binding.btn1d.background = res1}
        binding.btn1mo.setOnClickListener {
            interval = "1mo"
            currentAsset?.let { loadAll(it) }
            loadHistoryGraph()
            binding.btn1m.background = res1
            binding.btn5m.background = res1
            binding.btn15m.background = res1
            binding.btn30m.background = res1
            binding.btn1h.background = res1
            binding.btn1w.background = res1
            binding.btn1mo.background = res
            binding.btn90m.background = res1
            binding.btn1d.background = res1
        }

        binding.imgBtnSearchStocks.setOnClickListener {
            binding.editTextSearch.visibility = View.VISIBLE
            binding.editTextSearch.requestFocus()

            val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding.editTextSearch, InputMethodManager.SHOW_IMPLICIT)

            binding.editTextSearch.addTextChangedListener { text ->
                filterAssetsByName(text.toString())
            }
        }

        binding.imgBtnRefresh.setOnClickListener {
            currentAsset?.let { it1 -> loadAll(it1) }
        }

        binding.imgBtnAddFavorite.setOnClickListener {
            if(currentAsset != null) {
                databaseHelper.addAssetToFavorites(currentAsset!!.symbol, currentAsset!!.name, currentAsset!!.country, currentAsset!!.full_name, currentAsset!!.isin, currentAsset!!.currency)
            }
        }

        binding.imgBtnBackToAssetFavorite.setOnClickListener {
            bottomNav.visibility = View.VISIBLE
            clearCandleStickChart()
            binding.btnLoadGraphic.visibility = View.GONE
            binding.imgBtnBackToStocks.visibility = View.GONE
            binding.imgBtnBackToCountry.visibility = View.GONE
            binding.imgBtnBackToAssetFavorite.visibility = View.GONE
            binding.imgBtnAddFavorite.visibility = View.GONE
            binding.imgBtnSearchStocks.visibility = View.VISIBLE
            binding.imgBtnBackToStocks.visibility = View.GONE
            binding.imgBtnBackToCountry.visibility = View.VISIBLE
            binding.hsvStocksMenu.visibility = View.GONE
            binding.chart.visibility = View.GONE
            binding.currentPrice.visibility = View.GONE
            binding.volume.visibility = View.GONE
            binding.dailyChange.visibility = View.GONE
            binding.dayLow.visibility = View.GONE
            binding.dayHigh.visibility = View.GONE
            binding.previousClose.visibility = View.GONE
            binding.recyclerView.visibility = View.VISIBLE
            nameFavorite = "null1"
            findNavController().navigate(R.id.action_mainFragment_to_electFragment)
        }

        binding.btnLoadGraphic.setOnClickListener{
            loadHistoryGraph()
        }

        return view
    }

    private fun loadHistoryGraph() {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val toDate = dateFormat.format(Calendar.getInstance().time)
        val utcOffset = 3
        var fromDate = ""
        when(interval) {
            "1m" -> {
                calendar.add(Calendar.DAY_OF_YEAR, -2)
            }
            "5m" -> {
                calendar.add(Calendar.DAY_OF_YEAR, -3)
            }
            "15m" -> {
                calendar.add(Calendar.DAY_OF_YEAR, -5)
            }
            "30m" -> {
                calendar.add(Calendar.DAY_OF_YEAR, -7)
            }
            "1h" -> {
                calendar.add(Calendar.DAY_OF_YEAR, -10)
            }
            "90m" -> {
                calendar.add(Calendar.DAY_OF_YEAR, -14)
            }
            "1d" -> {
                calendar.add(Calendar.DAY_OF_YEAR, -150)
            }
            "1wk" -> {
                calendar.add(Calendar.DAY_OF_YEAR, -730)
            }
            "1mo" -> {
                calendar.add(Calendar.DAY_OF_YEAR, -1825)
            }
        }
        fromDate = dateFormat.format(calendar.time)
        if(nameFavorite != "" && nameFavorite != "null" && nameFavorite != "null1") {
            loadHistoricalStockData(symbolFavorite, fromDate, toDate, utcOffset, interval)
        } else {
            loadHistoricalStockData(currentAsset!!.symbol, fromDate, toDate, utcOffset, interval)
        }
    }

    private fun filterAssetsByName(query: String) {
        val filteredAssets = assetAdapter.getAssets().filter {
            it.name.contains(query, ignoreCase = true)
        }
        assetSearchAdapter.setAssets(filteredAssets)
        binding.recyclerView.adapter = assetSearchAdapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://bigidulka2.ddns.net:8000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)
        binding.tvLoad.visibility = View.VISIBLE
        assetSearchAdapter = AssetAdapter { asset ->
            binding.editTextSearch.visibility = View.GONE
            binding.imgBtnSearchStocks.visibility = View.GONE
            currentAsset = asset
            loadAll(currentAsset!!)
            binding.imgBtnBackToCountry.visibility = View.GONE
            binding.imgBtnBackToStocks.visibility = View.VISIBLE
            binding.imgBtnBackToStocks.setOnClickListener{
                val res1 = requireActivity().getDrawable(R.drawable.item_country_asset_background)
                binding.btn1m.background = res1
                binding.btn5m.background = res1
                binding.btn15m.background = res1
                binding.btn30m.background = res1
                binding.btn1h.background = res1
                binding.btn1w.background = res1
                binding.btn1mo.background = res1
                binding.btn90m.background = res1
                binding.btn1d.background = res1
                binding.tvNameActiv.visibility = View.GONE
                binding.tvNameActiv.text = ""
                clearCandleStickChart()
                binding.btnLoadGraphic.visibility = View.GONE
                binding.tvAsset.visibility = View.GONE
                binding.imgBtnAddFavorite.visibility = View.GONE
                binding.imgBtnSearchStocks.visibility = View.VISIBLE
                binding.imgBtnBackToStocks.visibility = View.GONE
                binding.imgBtnBackToCountry.visibility = View.VISIBLE
                binding.hsvStocksMenu.visibility = View.GONE
                binding.chart.visibility = View.GONE
                binding.currentPrice.visibility = View.GONE
                binding.volume.visibility = View.GONE
                binding.dailyChange.visibility = View.GONE
                binding.dayLow.visibility = View.GONE
                binding.dayHigh.visibility = View.GONE
                binding.previousClose.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
                binding.recyclerView.adapter = assetAdapter
            }
        }
        assetAdapter = AssetAdapter {asset ->
            binding.tvAsset.visibility = View.VISIBLE
            binding.editTextSearch.visibility = View.GONE
            binding.imgBtnSearchStocks.visibility = View.GONE
            currentAsset = asset
            loadAll(currentAsset!!)
            binding.imgBtnBackToCountry.visibility = View.GONE
            binding.imgBtnBackToStocks.visibility = View.VISIBLE
            binding.imgBtnBackToStocks.setOnClickListener{
                binding.tvNameActiv.visibility = View.GONE
                binding.tvNameActiv.text = ""
                clearCandleStickChart()
                binding.btnLoadGraphic.visibility = View.GONE
                binding.tvAsset.visibility = View.GONE
                binding.imgBtnAddFavorite.visibility = View.GONE
                binding.imgBtnRefresh.visibility = View.GONE
                binding.imgBtnSearchStocks.visibility = View.VISIBLE
                binding.imgBtnBackToStocks.visibility = View.GONE
                binding.imgBtnBackToCountry.visibility = View.VISIBLE
                binding.hsvStocksMenu.visibility = View.GONE
                binding.chart.visibility = View.GONE
                binding.currentPrice.visibility = View.GONE
                binding.volume.visibility = View.GONE
                binding.dailyChange.visibility = View.GONE
                binding.dayLow.visibility = View.GONE
                binding.dayHigh.visibility = View.GONE
                binding.previousClose.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
                binding.recyclerView.adapter = assetAdapter
            }
        }

        countryAdapter = CountryAdapter { country ->
            binding.tvCountry.visibility = View.GONE
            binding.tvLoad.visibility = View.VISIBLE
            currentCountry = country.country
            loadAssets()
            binding.imgBtnBackToCountry.setOnClickListener{
                binding.tvNameActiv.visibility = View.GONE
                binding.tvNameActiv.text = ""
                clearCandleStickChart()
                binding.imgBtnAddFavorite.visibility = View.GONE
                binding.imgBtnSearchStocks.visibility = View.GONE
                binding.editTextSearch.visibility = View.GONE
                binding.imgBtnBackToCountry.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
                binding.recyclerView.adapter = countryAdapter
            }
        }
        binding.recyclerView.adapter = countryAdapter
        if(nameFavorite != "null" && nameFavorite != "" && nameFavorite != "null1") {
            bottomNav = requireActivity().findViewById(R.id.bottomNavigationView)
            bottomNav.visibility = View.GONE
            binding.imgBtnBackToAssetFavorite.visibility = View.VISIBLE
            binding.imgBtnBackToStocks.visibility = View.GONE
            binding.imgBtnBackToCountry.visibility = View.GONE
            loadAll(Asset(countryFavorite, nameFavorite, fullNameFavorite, isinFavorite, currencyFavorite, symbolFavorite))
            if(databaseHelper.isAssetAlreadyInFavorites(symbolFavorite)) {
                val res = requireActivity().getDrawable(R.drawable.icon_elect)
                binding.imgBtnAddFavorite.background = res
            }
        } else {
            loadCountries()
        }
    }
    private fun loadAll(asset: Asset) {
        binding.tvNameActiv.visibility = View.VISIBLE
        if(currentAsset != null) binding.tvNameActiv.text = currentAsset!!.name
        binding.imgBtnRefresh.visibility = View.VISIBLE
        symbolTHIS = asset.symbol
        loadCurrentStockData(asset.symbol)
    }

    private fun loadCountries() {
        apiService.getCountries().enqueue(object : Callback<List<Asset>> {
            override fun onResponse(call: Call<List<Asset>>, response: Response<List<Asset>>) {
                if (response.isSuccessful && response.body() != null) {
                    uniqueAsset = response.body()!!.distinct()
                    val uniqueCountries = response.body()!!.map { it.country }.distinct()
                    val countries: List<Country> = uniqueCountries.map { Country(it) }
                    countryAdapter.setCountries(countries)
                    binding.tvLoad.visibility = View.GONE
                    binding.tvCountry.visibility = View.VISIBLE
                } else {
                    Toast.makeText(requireContext(), "Ошибка загрузки списка стран", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Asset>>, t: Throwable) {
                Toast.makeText(requireContext(), "Ошибка соединения: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun clearCandleStickChart() {
        binding.chart.clear()
        binding.chart.invalidate()
    }


    private fun loadAssets() {
        val uniqueAssets = uniqueAsset.filter { it.country ==  currentCountry}
        assetAdapter.setAssets(uniqueAssets)
        assetSearchAdapter.setAssets(uniqueAssets)
        binding.recyclerView.adapter = assetAdapter
        binding.imgBtnSearchStocks.visibility = View.VISIBLE
        binding.imgBtnBackToStocks.visibility = View.GONE
        binding.imgBtnBackToCountry.visibility = View.VISIBLE
        binding.tvLoad.visibility = View.GONE
        binding.tvAsset.visibility = View.VISIBLE
        binding.tvNameActiv.visibility = View.VISIBLE
        if(currentAsset!= null) binding.tvNameActiv.text = currentAsset!!.name
    }

    private fun loadCurrentStockData(symbol: String) {
        apiService.getCurrentStockData(symbol).enqueue(object : Callback<StockData> {
            override fun onResponse(call: Call<StockData>, response: Response<StockData>) {
                if (response.isSuccessful && response.body() != null) {
                    val stockData = response.body()!!
                    binding.btnLoadGraphic.visibility = View.VISIBLE
                    binding.imgBtnAddFavorite.visibility = View.VISIBLE
                    binding.recyclerView.visibility = View.GONE
                    binding.chart.visibility = View.VISIBLE
                    binding.hsvStocksMenu.visibility = View.VISIBLE
                    binding.currentPrice.visibility = View.VISIBLE
                    binding.volume.visibility = View.VISIBLE
                    binding.dailyChange.visibility = View.VISIBLE
                    binding.dayLow.visibility = View.VISIBLE
                    binding.dayHigh.visibility = View.VISIBLE
                    binding.previousClose.visibility = View.VISIBLE
                    binding.tvAsset.visibility = View.GONE
                    binding.tvLoad.visibility = View.GONE

                    binding.currentPrice.text = "Текущая цена: \$${formatValue(stockData.Open)}"
                    binding.volume.text = "Объем: \$${formatValue(stockData.Volume)}"
                    binding.dailyChange.text = "Дневное изменение: \$${formatValue(stockData.Open - stockData.Close)}"
                    binding.dayLow.text = "Минимум дня: \$${formatValue(stockData.Low)}"
                    binding.dayHigh.text = "Максимум дня: \$${formatValue(stockData.High)}"
                    binding.previousClose.text = "Цена закрытия: \$${formatValue(stockData.Close)}"

                    if(currentAsset != null && databaseHelper.isAssetAlreadyInFavorites(currentAsset!!.symbol)) {
                        val res = requireActivity().getDrawable(R.drawable.icon_elect)
                        binding.imgBtnAddFavorite.background = res
                    } else {
                        val res = requireActivity().getDrawable(R.drawable.icon_not_elect)
                        binding.imgBtnAddFavorite.background = res
                    }

                } else {
                    Toast.makeText(requireContext(), "Ошибка загрузки текущих данных по акции", Toast.LENGTH_SHORT).show()
                    binding.btnLoadGraphic.visibility = View.GONE
                    binding.imgBtnBackToCountry.visibility = View.VISIBLE
                    binding.imgBtnBackToStocks.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<StockData>, t: Throwable) {
                Toast.makeText(requireContext(), "Ошибка соединения: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun formatValue(value: Double): String {
        return if (value == 0.0) {
            "Данные недоступны"
        } else {
            String.format("%.10f", value)
        }
    }

    private fun loadHistoricalStockData(symbol: String, fromDate: String, toDate: String, utcOffset: Int, interval: String) {
        apiService.getHistoricalStockData(symbol, fromDate, toDate, utcOffset, interval).enqueue(object : Callback<List<JsonObject>> {
            override fun onResponse(call: Call<List<JsonObject>>, response: Response<List<JsonObject>>) {
                if (response.isSuccessful && response.body() != null) {
                    val historicalData = response.body()!!

                    val historicalJsonList = historicalData.map { jsonObject ->
                        val adjustedJson = JsonObject()


                        if(interval =="1d" || interval=="1wk" || interval=="1mo") adjustedJson.addProperty("Date", jsonObject.get("Date").asString)
                        else adjustedJson.addProperty("Datetime", jsonObject.get("Datetime").asString)
                        adjustedJson.addProperty("Adj Close_$symbol", jsonObject.get("Adj Close_$symbol").asDouble)
                        adjustedJson.addProperty("Close_$symbol", jsonObject.get("Close_$symbol").asDouble)
                        adjustedJson.addProperty("High_$symbol", jsonObject.get("High_$symbol").asDouble)
                        adjustedJson.addProperty("Low_$symbol", jsonObject.get("Low_$symbol").asDouble)
                        adjustedJson.addProperty("Open_$symbol", jsonObject.get("Open_$symbol").asDouble)
                        adjustedJson.addProperty("Volume_$symbol", jsonObject.get("Volume_$symbol").asInt)

                        adjustedJson
                    }

                    displayHistoricalChart(historicalJsonList, symbol, interval)
                } else {
                    Toast.makeText(requireContext(), "Ошибка загрузки исторических данных по акции", Toast.LENGTH_SHORT).show()
                    binding.imgBtnBackToCountry.visibility = View.VISIBLE
                    binding.imgBtnBackToStocks.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<List<JsonObject>>, t: Throwable) {
                Toast.makeText(requireContext(), "Ошибка соединения: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun displayHistoricalChart(data: List<JsonObject>, symbol: String, interval: String) {
        binding.chart.visibility = View.VISIBLE
        val chart: CandleStickChart = binding.chart

        val candleEntries = mutableListOf<CandleEntry>()

        val highFieldName = "High_$symbol"
        val lowFieldName = "Low_$symbol"
        val openFieldName = "Open_$symbol"
        val closeFieldName = "Close_$symbol"
        val dateTime = "Datetime"
        val date = "Date"

        var minPrice = Double.MAX_VALUE
        var maxPrice = Double.MIN_VALUE

        data.forEachIndexed { index, jsonObject ->
            val high = jsonObject.get(highFieldName).asDouble
            val low = jsonObject.get(lowFieldName).asDouble
            val open = jsonObject.get(openFieldName).asDouble
            val close = jsonObject.get(closeFieldName).asDouble
            var dates = ""
            if (interval == "1d" || interval == "1wk" || interval == "1mo") dates = jsonObject.get(date)?.asString ?: ""
            else dates = jsonObject.get(dateTime)?.asString ?: ""

            val datetimeStr = dates
            val date = if (!datetimeStr.isNullOrEmpty()) {
                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault()).parse(datetimeStr)
            } else {
                null
            }
            val timeInMillis = date?.time ?: 0L

            candleEntries.add(
                CandleEntry(
                    index.toFloat(),
                    high.toFloat(),
                    low.toFloat(),
                    open.toFloat(),
                    close.toFloat()
                )
            )

            minPrice = minOf(minPrice, low, close, open)
            maxPrice = maxOf(maxPrice, high, close, open)
        }

        // Рассчитываем маржу для отступа
        val margin = (maxPrice - minPrice) * 0.5
        val adjustedMinPrice = minPrice - margin
        val adjustedMaxPrice = maxPrice + margin

        // Убедитесь, что минимальное значение оси Y не меньше нуля
        val finalMinPrice = if (adjustedMinPrice < 0) 0.0 else adjustedMinPrice

        val candleDataSet = CandleDataSet(candleEntries, "Цена актива (Candlestick)")
        candleDataSet.decreasingColor = Color.RED
        candleDataSet.decreasingPaintStyle = Paint.Style.FILL
        candleDataSet.increasingColor = Color.GREEN
        candleDataSet.increasingPaintStyle = Paint.Style.FILL
        candleDataSet.shadowColor = Color.DKGRAY
        candleDataSet.shadowWidth = 0.7f
        candleDataSet.neutralColor = Color.LTGRAY
        candleDataSet.setDrawValues(false)

        val candleData = CandleData(candleDataSet)
        chart.data = candleData
        chart.invalidate()

        chart.setTouchEnabled(true)
        chart.setPinchZoom(true)
        chart.setDragEnabled(true)
        chart.description.isEnabled = false
        chart.legend.isEnabled = true
        chart.axisLeft.setDrawGridLines(false)
        chart.axisRight.isEnabled = false

        chart.xAxis.setPosition(XAxis.XAxisPosition.BOTTOM)
        chart.xAxis.setGranularity(1f)

        val dateFormat = if (interval == "1d" || interval == "1wk" || interval == "1mo") {
            "MM-dd"
        } else {
            "MM-dd HH:mm"
        }

        val dates = data.mapNotNull {
            val datetimeStr = if (interval == "1d" || interval == "1wk" || interval == "1mo") {
                it.get(date)?.asString ?: ""
            } else {
                it.get(dateTime)?.asString ?: ""
            }
            if (!datetimeStr.isNullOrEmpty()) {
                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault()).parse(datetimeStr)
                    ?.let { it1 ->
                        SimpleDateFormat(dateFormat, Locale.getDefault()).format(it1)
                    }
            } else {
                null
            }
        }

        chart.xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                val index = value.toInt()
                return if (index in dates.indices) dates[index] else ""
            }
        }

        chart.axisLeft.setGranularityEnabled(true)
        chart.axisLeft.setAxisMinimum(finalMinPrice.toFloat()) // Устанавливаем минимальное значение на оси Y
        chart.axisLeft.setAxisMaximum(adjustedMaxPrice.toFloat())
        chart.axisLeft.setLabelCount(10, false)

        chart.axisLeft.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return String.format("%.2f", value)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        clearCandleStickChart()
        binding.btnLoadGraphic.visibility = View.GONE
        binding.imgBtnBackToStocks.visibility = View.GONE
        binding.imgBtnBackToCountry.visibility = View.GONE
        binding.imgBtnBackToAssetFavorite.visibility = View.GONE
        binding.imgBtnAddFavorite.visibility = View.GONE
        binding.imgBtnSearchStocks.visibility = View.VISIBLE
        binding.imgBtnBackToStocks.visibility = View.GONE
        binding.imgBtnBackToCountry.visibility = View.VISIBLE
        binding.hsvStocksMenu.visibility = View.GONE
        binding.chart.visibility = View.GONE
        binding.currentPrice.visibility = View.GONE
        binding.volume.visibility = View.GONE
        binding.dailyChange.visibility = View.GONE
        binding.dayLow.visibility = View.GONE
        binding.dayHigh.visibility = View.GONE
        binding.previousClose.visibility = View.GONE
        binding.recyclerView.visibility = View.VISIBLE
        nameFavorite = "null1"
    }


}


data class StockData(
    val Open: Double,
    val Volume: Double,
    val Low: Double,
    val High: Double,
    val Close: Double,
    val targetMeanPrice: Double,
    val targetHighPrice: Double,
    val targetLowPrice: Double,
    val recommendationKey: String,
    val beta: Double,
    val marketCap: Double,
    val fiftyTwoWeekLow: Double,
    val fiftyTwoWeekHigh: Double,
    val earningsGrowth: Double,
    val revenueGrowth: Double
)

