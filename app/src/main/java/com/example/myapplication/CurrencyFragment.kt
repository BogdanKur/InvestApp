package com.example.myapplication

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.FragmentCurrencyBinding
import com.example.myapplication.databinding.FragmentMainBinding
import com.github.mikephil.charting.charts.CandleStickChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.CandleEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CurrencyFragment : Fragment() {
    private var _binding: FragmentCurrencyBinding? = null
    private val binding get() = _binding!!
    private lateinit var baseCurrencyAdapter: CurrencyAdapter
    private lateinit var convertCurrencyAdapter: ConvertCurrencyAdapter
    private lateinit var currencySearchAdapter: CurrencyAdapter
    private lateinit var convertCurrencySearchAdapter: ConvertCurrencyAdapter
    private lateinit var databaseHelper: FavoritesDatabaseHelper
    private lateinit var apiService: ApiService
    private var selectedBaseCurrency: String? = null
    private var selectedConvertCurrency: String? = null
    var interval = "1wk"
    var currentBase = ""
    var currentConversation = ""
    var baseNameFavorite = ""
    var secondNameFavorite = ""
    lateinit var bottomNav: BottomNavigationView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCurrencyBinding.inflate(inflater, container, false)
        val view = binding.root
        databaseHelper = FavoritesDatabaseHelper(requireContext())

        arguments.let { bundle->
            if(baseNameFavorite != "null1") {
                baseNameFavorite = bundle?.getString("nameBase").toString()
                secondNameFavorite = bundle?.getString("nameSecond").toString()
                currentBase = baseNameFavorite
                currentConversation = secondNameFavorite
            }
        }

        val res = requireActivity().getDrawable(R.drawable.item_country_asset_background_choose)
        val res1 = requireActivity().getDrawable(R.drawable.item_country_asset_background)
        binding.tvLoad.visibility = View.VISIBLE
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
            interval = "1mo"
            loadHistoryGraph()
            loadAllCurrency(currentBase, currentConversation)
        }
        binding.btn5m.setOnClickListener {
            interval = "5m"
            loadHistoryGraph()
            loadAllCurrency(currentBase, currentConversation)
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
            loadHistoryGraph()
            loadAllCurrency(currentBase, currentConversation)
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
            loadHistoryGraph()
            loadAllCurrency(currentBase, currentConversation)
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
            loadHistoryGraph()
            loadAllCurrency(currentBase, currentConversation)
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
            loadHistoryGraph()
            loadAllCurrency(currentBase, currentConversation)
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
            loadHistoryGraph()
            loadAllCurrency(currentBase, currentConversation)
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
            loadHistoryGraph()
            loadAllCurrency(currentBase, currentConversation)
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
            loadHistoryGraph()
            loadAllCurrency(currentBase, currentConversation)
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
        val retrofit = Retrofit.Builder()
            .baseUrl("http://bigidulka2.ddns.net:8000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)
        binding.tvLoad.visibility = View.VISIBLE

        binding.recyclerViewBaseCurrency.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewConvertCurrency.layoutManager = LinearLayoutManager(requireContext())

        baseCurrencyAdapter = CurrencyAdapter { baseCurrency ->
            selectedBaseCurrency = baseCurrency.base
            checkCurrencyPairReady()
        }

        convertCurrencyAdapter = ConvertCurrencyAdapter { convertCurrency ->
            selectedConvertCurrency = convertCurrency
            checkCurrencyPairReady()
        }

        currencySearchAdapter = CurrencyAdapter { baseCurrency ->
            selectedBaseCurrency = baseCurrency.base
            checkCurrencyPairReady() }
        convertCurrencySearchAdapter = ConvertCurrencyAdapter { convertCurrency ->
            selectedConvertCurrency = convertCurrency
            checkCurrencyPairReady()
        }

        binding.recyclerViewBaseCurrency.adapter = baseCurrencyAdapter
        binding.recyclerViewConvertCurrency.adapter = convertCurrencyAdapter

        binding.imgBtnSearchCurrencies.setOnClickListener{
            binding.editTextSearch.visibility = View.VISIBLE
            binding.editTextSearch.requestFocus()

            val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding.editTextSearch, InputMethodManager.SHOW_IMPLICIT)

            binding.editTextSearch.addTextChangedListener { text ->
                filterCurrencyByName(text.toString())
                filterConvertCurrencyByName(text.toString())
            }
        }
        binding.imgBtnRefresh.setOnClickListener {
            loadAllCurrency(selectedBaseCurrency!!, selectedConvertCurrency!!)
        }

        binding.imgBtnAddFavorite.setOnClickListener {
            databaseHelper.addCurrencyPairToFavorites(selectedBaseCurrency!!, selectedConvertCurrency!!)
        }

        binding.imgBtnBackToCryptoFavorite.setOnClickListener {
            binding.tvNameActiv.visibility = View.GONE
            binding.tvNameActiv.text = ""
            bottomNav.visibility = View.VISIBLE
            clearCandleStickChart()
            binding.btnLoadGraphic.visibility = View.GONE
            binding.imgBtnBackToCryptoFavorite.visibility = View.GONE
            binding.imgBtnBackToCurrency.visibility = View.VISIBLE
            baseNameFavorite = "null1"
            findNavController().navigate(R.id.action_currencyFragment_to_electFragment)
        }

        binding.btnLoadGraphic.setOnClickListener {
            loadHistoryGraph()
        }

        if(baseNameFavorite != "" && baseNameFavorite != "null") {
            bottomNav = requireActivity().findViewById(R.id.bottomNavigationView)
            bottomNav.visibility = View.GONE
            binding.tvCurrencyPair.visibility = View.GONE
            binding.imgBtnBackToCryptoFavorite.visibility = View.VISIBLE
            binding.imgBtnBackToCurrency.visibility = View.GONE
            loadCurrencyData(baseNameFavorite, secondNameFavorite)
            if(databaseHelper.isCurrencyPairAlreadyInFavorites(baseNameFavorite, secondNameFavorite)) {
                val res = requireActivity().getDrawable(R.drawable.icon_elect)
                binding.imgBtnAddFavorite.background = res
            }
        } else {
            loadCurrencyPairs()
        }

        return view
    }

    fun clearCandleStickChart() {
        binding.chart.clear()
        binding.chart.invalidate()
    }
    private fun loadHistoryGraph() {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val toDate = dateFormat.format(Calendar.getInstance().time)
        val utcOffset = 3
        var fromDate = ""

        when (interval) {
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
        if(baseNameFavorite != "" && baseNameFavorite != "null" && baseNameFavorite != "null1") {
            loadHistoricalCurrencyData(baseNameFavorite,secondNameFavorite, fromDate, toDate, utcOffset, interval)
        } else {
            loadHistoricalCurrencyData(currentBase, currentConversation, fromDate, toDate, utcOffset, interval)
        }
    }
    private fun filterCurrencyByName(query: String) {
        val filteredCurrency = baseCurrencyAdapter.getCurrencies()
            .filter { it.base.contains(query, ignoreCase = true) }
            .distinct()

        currencySearchAdapter.setCurrencies(filteredCurrency)
        binding.recyclerViewBaseCurrency.adapter = currencySearchAdapter
    }

    private fun filterConvertCurrencyByName(query: String) {
        val filteredConvertCurrency = convertCurrencyAdapter.getCurrencies()
            .filter { it.contains(query, ignoreCase = true) }
            .distinct()

        convertCurrencySearchAdapter.setCurrencies(filteredConvertCurrency)
        binding.recyclerViewConvertCurrency.adapter = convertCurrencySearchAdapter
    }

    private fun loadAllCurrency(baseCurrency: String, convertCurrency: String) {
        binding.tvLoad.visibility = View.VISIBLE

        loadCurrencyData(baseCurrency, convertCurrency)

        binding.imgBtnBackToCurrency.setOnClickListener{
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
            binding.imgBtnAddFavorite.visibility = View.GONE
            binding.imgBtnRefresh.visibility = View.GONE
            binding.imgBtnBackToCurrency.visibility = View.GONE
            binding.llRecyclerView.visibility = View.VISIBLE
            binding.llTextView.visibility = View.VISIBLE
            binding.chart.visibility = View.GONE
            binding.hsvStocksMenu.visibility = View.GONE
            binding.currentPrice.visibility = View.GONE
            binding.dailyChange.visibility = View.GONE
            binding.dayLow.visibility = View.GONE
            binding.dayHigh.visibility = View.GONE
            binding.previousClose.visibility = View.GONE
            binding.imgBtnSearchCurrencies.visibility = View.VISIBLE
            binding.imgBtnShowData.visibility = View.GONE
            val background = ContextCompat.getDrawable(requireContext(), R.drawable.item_country_asset_background)
            setRecyclerViewBackground(binding.recyclerViewBaseCurrency, background)
            setRecyclerViewBackground(binding.recyclerViewConvertCurrency, background)
            selectedBaseCurrency = null
            selectedConvertCurrency = null
        }
    }

    fun setRecyclerViewBackground(recyclerView: RecyclerView, background: Drawable?) {
        for (i in 0 until recyclerView.childCount) {
            val itemView = recyclerView.getChildAt(i)
            itemView.background = background
        }
    }



    private fun loadHistoricalCurrencyData(baseCurrency: String, convertCurrency: String, fromDate: String, toDate: String, utcOffset: Int, interval: String) {
        val pair = "$baseCurrency$convertCurrency"
        apiService.getHistoricalCurrencyData(pair, fromDate, toDate, utcOffset, interval).enqueue(object : Callback<List<JsonObject>> {
            override fun onResponse(call: Call<List<JsonObject>>, response: Response<List<JsonObject>>) {
                if (response.isSuccessful && response.body() != null) {
                    val historicalData = response.body()!!
                    binding.imgBtnAddFavorite.visibility = View.VISIBLE
                    val historicalJsonList = historicalData.map { jsonObject ->
                        val adjustedJson = JsonObject()

                        if(interval == "1d" || interval == "1wk" || interval == "1mo")
                            adjustedJson.addProperty("Date", jsonObject.get("Date").asString)
                        else
                            adjustedJson.addProperty("Datetime", jsonObject.get("Datetime").asString)

                        adjustedJson.addProperty("Adj Close_$pair=X", jsonObject.get("Adj Close_$pair=X").asDouble)
                        adjustedJson.addProperty("Close_$pair=X", jsonObject.get("Close_$pair=X").asDouble)
                        adjustedJson.addProperty("High_$pair=X", jsonObject.get("High_$pair=X").asDouble)
                        adjustedJson.addProperty("Low_$pair=X", jsonObject.get("Low_$pair=X").asDouble)
                        adjustedJson.addProperty("Open_$pair=X", jsonObject.get("Open_$pair=X").asDouble)
                        adjustedJson.addProperty("Volume_$pair=X", jsonObject.get("Volume_$pair=X").asInt)

                        adjustedJson
                    }

                    displayHistoricalCurrencyChart(historicalJsonList, pair, interval)
                } else {
                    Toast.makeText(requireContext(), "Ошибка загрузки исторических данных по валютной паре", Toast.LENGTH_SHORT).show()
                    binding.imgBtnBackToCurrency.visibility = View.GONE
                    binding.imgBtnBackToCryptoFavorite.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<List<JsonObject>>, t: Throwable) {
                Toast.makeText(requireContext(), "Ошибка соединения: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun displayHistoricalCurrencyChart(data: List<JsonObject>, pair: String, interval: String) {
        binding.chart.visibility = View.VISIBLE
        val chart: CandleStickChart = binding.chart

        val candleEntries = mutableListOf<CandleEntry>()

        val highFieldName = "High_$pair=X"
        val lowFieldName = "Low_$pair=X"
        val openFieldName = "Open_$pair=X"
        val closeFieldName = "Close_$pair=X"
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

        // Рассчитываем отступ с учетом интервала
        var margin = (maxPrice - minPrice) * 700
        if (interval == "1wk" || interval == "1mo") {
            margin = (maxPrice - minPrice) * 15
        }

        // Убедимся, что минимальное значение не будет меньше 0
        val adjustedMinPrice = maxOf(minPrice - margin, 0.0) // Невозможность отрицательных значений
        val adjustedMaxPrice = maxPrice + margin

        val candleDataSet = CandleDataSet(candleEntries, "Цена валютной пары (Candlestick)")

        candleDataSet.setDrawValues(false)
        candleDataSet.shadowColor = Color.DKGRAY
        candleDataSet.shadowWidth = 0.7f
        candleDataSet.neutralColor = Color.LTGRAY

        candleDataSet.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return if (value > 0) String.format("%.2f", value) else ""
            }
        }

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
        chart.axisLeft.setAxisMinimum(adjustedMinPrice.toFloat()) // Устанавливаем минимальное значение на оси Y
        chart.axisLeft.setAxisMaximum(adjustedMaxPrice.toFloat())
        chart.axisLeft.setLabelCount(10, false)

        chart.axisLeft.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return String.format("%.2f", value)
            }
        }

        for (i in 0 until chart.data.dataSetCount) {
            val dataSet = chart.data.getDataSetByIndex(i) as CandleDataSet
            for (entry in dataSet.values) {
                val candleEntry = entry as CandleEntry
                if (candleEntry.close > candleEntry.open) {
                    // Устанавливаем зеленый для свечей, где close > open
                    dataSet.increasingColor = Color.GREEN
                } else {
                    // Устанавливаем красный для свечей, где close < open
                    dataSet.decreasingColor = Color.RED
                }
            }
        }
    }






    private fun loadCurrencyPairs() {
        apiService.getCurrencyPairs().enqueue(object : Callback<List<CurrencyPair>> {
            override fun onResponse(call: Call<List<CurrencyPair>>, response: Response<List<CurrencyPair>>) {
                if (response.isSuccessful && response.body() != null) {
                    val currencyPairs = response.body()!!
                    val convertCurrencies = currencyPairs.map { it.second }.distinct()
                    val uniqueBaseCurrency = currencyPairs.distinctBy { it.base }
                    currencySearchAdapter.setCurrencies(uniqueBaseCurrency)
                    convertCurrencySearchAdapter.setCurrencies(convertCurrencies)

                    baseCurrencyAdapter.setCurrencies(uniqueBaseCurrency)
                    convertCurrencyAdapter.setCurrencies(convertCurrencies)
                    binding.imgBtnSearchCurrencies.visibility = View.VISIBLE
                    binding.tvLoad.visibility = View.GONE
                    binding.tvCurrencyPair.visibility = View.VISIBLE
                    if(databaseHelper.isCurrencyPairAlreadyInFavorites(currentBase, currentConversation)) {
                        val res = requireActivity().getDrawable(R.drawable.icon_elect)
                        binding.imgBtnAddFavorite.background = res
                    } else {
                        val res = requireActivity().getDrawable(R.drawable.icon_not_elect)
                        binding.imgBtnAddFavorite.background = res
                    }
                } else {
                    Toast.makeText(requireContext(), "Ошибка загрузки валютных пар", Toast.LENGTH_SHORT).show()
                    binding.imgBtnBackToCurrency.visibility = View.GONE
                    binding.imgBtnBackToCryptoFavorite.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<List<CurrencyPair>>, t: Throwable) {
                Toast.makeText(requireContext(), "Ошибка соединения: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun checkCurrencyPairReady() {
        if (selectedBaseCurrency != null && selectedConvertCurrency != null) {
            binding.tvCurrencyPair.visibility = View.GONE
            binding.imgBtnShowData.visibility = View.VISIBLE
            binding.imgBtnShowData.setOnClickListener {
                binding.imgBtnAddFavorite.visibility = View.VISIBLE
                binding.imgBtnRefresh.visibility = View.VISIBLE
                currentBase = selectedBaseCurrency!!
                currentConversation = selectedConvertCurrency!!
                loadAllCurrency(selectedBaseCurrency!!, selectedConvertCurrency!!)
                binding.imgBtnShowData.visibility = View.GONE
                binding.imgBtnSearchCurrencies.visibility = View.GONE
                binding.imgBtnBackToCurrency.visibility = View.VISIBLE
                binding.editTextSearch.visibility = View.GONE
            }
        } else {
            binding.imgBtnShowData.visibility = View.GONE
            binding.imgBtnSearchCurrencies.visibility = View.VISIBLE
            binding.imgBtnAddFavorite.visibility = View.GONE
        }
    }

    private fun loadCurrencyData(baseCurrency: String, convertCurrency: String) {
        binding.tvCurrencyPair.visibility = View.GONE
        val pair = "$baseCurrency$convertCurrency"
        apiService.getCurrentCurrencyData(pair).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful && response.body() != null) {
                    val currencyData = response.body()!!

                    val open = currencyData.getAsJsonPrimitive("Open")?.asDouble ?: 0.0
                    val close = currencyData.getAsJsonPrimitive("Close")?.asDouble ?: 0.0
                    val high = currencyData.getAsJsonPrimitive("High")?.asDouble ?: 0.0
                    val low = currencyData.getAsJsonPrimitive("Low")?.asDouble ?: 0.0
                    binding.tvNameActiv.visibility = View.VISIBLE
                    binding.tvNameActiv.text = "$baseCurrency$convertCurrency"
                    binding.btnLoadGraphic.visibility = View.VISIBLE
                    binding.llRecyclerView.visibility = View.GONE
                    binding.llTextView.visibility = View.GONE
                    binding.chart.visibility = View.VISIBLE
                    binding.hsvStocksMenu.visibility = View.VISIBLE
                    binding.currentPrice.visibility = View.VISIBLE
                    binding.dailyChange.visibility = View.VISIBLE
                    binding.dayLow.visibility = View.VISIBLE
                    binding.dayHigh.visibility = View.VISIBLE
                    binding.previousClose.visibility = View.VISIBLE
                    binding.tvLoad.visibility = View.GONE
                    binding.currentPrice.text = "Текущий курс: $${formatValue(open)}"
                    binding.dailyChange.text = "Дневное изменение: $${formatValue(open - close)}"
                    binding.dayLow.text = "Минимум дня: $${formatValue(low)}"
                    binding.dayHigh.text = "Максимум дня: $${formatValue(high)}"
                    binding.previousClose.text = "Цена закрытия: $${formatValue(close)}"

                } else {
                    Toast.makeText(requireContext(), "Ошибка загрузки данных по валютной паре", Toast.LENGTH_SHORT).show()
                    binding.imgBtnBackToCurrency.visibility = View.VISIBLE
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
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

    override fun onPause() {
        super.onPause()
        clearCandleStickChart()
        binding.btnLoadGraphic.visibility = View.GONE
        binding.imgBtnBackToCryptoFavorite.visibility = View.GONE
        binding.imgBtnBackToCurrency.visibility = View.VISIBLE
        baseNameFavorite = "null1"
    }
}


