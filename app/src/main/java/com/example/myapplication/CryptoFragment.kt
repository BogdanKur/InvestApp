package com.example.myapplication

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
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
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.myapplication.databinding.FragmentCryptoBinding
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

class CryptoFragment : Fragment() {
    private var _binding: FragmentCryptoBinding? = null
    private val binding get() = _binding!!
    private lateinit var cryptoAdapter: CryptoAdapter
    private lateinit var cryptoSearchAdapter: CryptoAdapter
    private lateinit var apiService: ApiService
    private var interval = "1d"
    private var currentCrypto: Crypto? = null
    private lateinit var databaseHelper: FavoritesDatabaseHelper
    private lateinit var viewModel: CryptoViewModel
    var nameFavorite = ""
    var symbolFavorite = ""
    lateinit var bottomNav: BottomNavigationView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCryptoBinding.inflate(inflater, container, false)
        val view = binding.root
        databaseHelper = FavoritesDatabaseHelper(requireContext())
        viewModel = ViewModelProvider(this).get(CryptoViewModel::class.java)
        arguments.let { bundle ->
            if(nameFavorite != "null1") {
                viewModel.nameFavorite = bundle?.getString("nameCrypto").toString()
                symbolFavorite = bundle?.getString("symbolCrypto").toString()
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
            if (currentCrypto != null)
                loadHistoricalCryptoData(currentCrypto!!.symbol, "1m")
            else loadHistoricalCryptoData(symbolFavorite, "1m")
            setIntervalAndReload("1m")
        }
        binding.btn5m.setOnClickListener {
            if (currentCrypto != null)
                loadHistoricalCryptoData(currentCrypto!!.symbol, "5m")
            else loadHistoricalCryptoData(symbolFavorite, "5m")
            setIntervalAndReload("5m")
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
            if (currentCrypto != null)
                loadHistoricalCryptoData(currentCrypto!!.symbol, "15m")
            else loadHistoricalCryptoData(symbolFavorite, "15m")
            setIntervalAndReload("15m")
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
            if (currentCrypto != null)
                loadHistoricalCryptoData(currentCrypto!!.symbol, "30m")
            else loadHistoricalCryptoData(symbolFavorite, "30m")
            setIntervalAndReload("30m")
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
            if (currentCrypto != null)
                loadHistoricalCryptoData(currentCrypto!!.symbol, "90m")
            else loadHistoricalCryptoData(symbolFavorite, "90m")
            setIntervalAndReload("90m")
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
            if (currentCrypto != null)
            loadHistoricalCryptoData(currentCrypto!!.symbol, "1h")
            else loadHistoricalCryptoData(symbolFavorite, "1h")
            setIntervalAndReload("1h")
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
            if (currentCrypto != null)
                loadHistoricalCryptoData(currentCrypto!!.symbol, "1d")
            else loadHistoricalCryptoData(symbolFavorite, "1d")
            setIntervalAndReload("1d")
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
            if (currentCrypto != null)
            loadHistoricalCryptoData(currentCrypto!!.symbol, "1wk")
            else loadHistoricalCryptoData(currentCrypto!!.symbol, "1wk")
            setIntervalAndReload("1wk")
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
            if (currentCrypto != null)
                loadHistoricalCryptoData(currentCrypto!!.symbol, "1mo")
            else loadHistoricalCryptoData(symbolFavorite, "1mo")
            setIntervalAndReload("1mo")
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

        binding.imgBtnSearchCrypto.setOnClickListener {
            binding.editTextSearch.visibility = View.VISIBLE
            binding.editTextSearch.requestFocus()
            (activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                .showSoftInput(binding.editTextSearch, InputMethodManager.SHOW_IMPLICIT)

            binding.editTextSearch.addTextChangedListener { text ->
                filterCryptosByName(text.toString())
            }
        }

        binding.imgBtnAddFavorite.setOnClickListener {
            if (currentCrypto!= null)
            databaseHelper.addCryptoToFavorites(currentCrypto!!.symbol, currentCrypto!!.name)
        }
        binding.imgBtnBackToCrypto.setOnClickListener {
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
            binding.chart.visibility = View.GONE
            binding.imgBtnBackToCrypto.visibility = View.GONE
            binding.hsvStocksMenu.visibility = View.GONE
            binding.currentPrice.visibility = View.GONE
            binding.volume.visibility = View.GONE
            binding.dailyChange.visibility = View.GONE
            binding.dayLow.visibility = View.GONE
            binding.dayHigh.visibility = View.GONE
            binding.previousClose.visibility = View.GONE
            binding.imgBtnSearchCrypto.visibility = View.VISIBLE
            binding.recyclerView.visibility = View.VISIBLE
            nameFavorite = "null1"
        }

        binding.imgBtnBackToCryptoFavorite.setOnClickListener {
            binding.tvNameActiv.visibility = View.GONE
            binding.tvNameActiv.text = ""
            bottomNav.visibility =View.VISIBLE
            clearCandleStickChart()
            binding.btnLoadGraphic.visibility = View.GONE
            binding.imgBtnBackToCryptoFavorite.visibility = View.GONE
            binding.imgBtnBackToCrypto.visibility = View.VISIBLE
            findNavController().navigate(R.id.action_cryptoCurrencyFragment_to_electFragment)
        }

        binding.btnLoadGraphic.setOnClickListener {
            loadHistoricalCryptoData(currentCrypto!!.symbol, interval)
        }

        return view
    }
    fun clearCandleStickChart() {
        binding.chart.clear()
        binding.chart.invalidate()
    }

    private fun setIntervalAndReload(newInterval: String) {
        interval = newInterval
        currentCrypto?.let { loadCryptoData(it) }
    }

    private fun filterCryptosByName(query: String) {
        val filteredCryptos = cryptoAdapter.getCryptos().filter {
            it.name.contains(query, ignoreCase = true)
        }
        cryptoSearchAdapter.setCryptos(filteredCryptos)
        binding.recyclerView.adapter = cryptoSearchAdapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val retrofit = Retrofit.Builder()
            .baseUrl("http://bigidulka2.ddns.net:8000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)
        cryptoSearchAdapter = CryptoAdapter { crypto ->
            showCryptoDetails(crypto)
        }

        cryptoAdapter = CryptoAdapter { crypto ->
            showCryptoDetails(crypto)
        }

        binding.imgBtnRefresh.setOnClickListener {
            if(currentCrypto != null) loadCryptoData(currentCrypto!!)
        }

        if(viewModel.nameFavorite != "" && viewModel.nameFavorite != "null" && viewModel.nameFavorite != "null1") {
            bottomNav = requireActivity().findViewById(R.id.bottomNavigationView)
            bottomNav.visibility = View.GONE
            binding.tvCrypto.visibility = View.GONE
            binding.imgBtnBackToCryptoFavorite.visibility = View.VISIBLE
            binding.imgBtnBackToCrypto.visibility = View.GONE
            loadCryptoData(Crypto(viewModel.nameFavorite, symbolFavorite))
            if(databaseHelper.isCryptoAlreadyInFavorites(viewModel.nameFavorite)) {
                val res = requireActivity().getDrawable(R.drawable.icon_elect)
                binding.imgBtnAddFavorite.background = res
            }
        } else {
            loadCryptocurrencies()

        }

    }

    private fun showCryptoDetails(crypto: Crypto) {
        binding.editTextSearch.visibility = View.GONE
        binding.imgBtnSearchCrypto.visibility = View.GONE
        binding.recyclerView.visibility = View.GONE
        binding.imgBtnBackToCrypto.visibility = View.VISIBLE
        currentCrypto = crypto
        loadCryptoData(currentCrypto!!)
    }

    private fun loadCryptocurrencies() {
        apiService.getCryptocurrencies().enqueue(object : Callback<List<Crypto>> {
            override fun onResponse(call: Call<List<Crypto>>, response: Response<List<Crypto>>) {
                if (response.isSuccessful && response.body() != null) {
                    val uniqueCryptos = response.body()!!.distinctBy { it.symbol }
                    cryptoAdapter.setCryptos(uniqueCryptos)
                    cryptoSearchAdapter.setCryptos(uniqueCryptos)
                    binding.imgBtnSearchCrypto.visibility = View.VISIBLE
                    binding.tvLoad.visibility = View.GONE
                    binding.tvCrypto.visibility = View.VISIBLE
                    binding.recyclerView.adapter = cryptoAdapter

                } else {
                    Toast.makeText(requireContext(), "Ошибка загрузки списка криптовалют", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Crypto>>, t: Throwable) {
                Toast.makeText(requireContext(), "Ошибка соединения: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun loadCryptoData(crypto: Crypto) {
        if(currentCrypto != null) {
            binding.tvNameActiv.visibility = View.VISIBLE
            binding.tvNameActiv.text = currentCrypto!!.name
        } else {
            binding.tvNameActiv.visibility = View.VISIBLE
            binding.tvNameActiv.text = nameFavorite
        }
        binding.chart.visibility = View.VISIBLE
        binding.tvCrypto.visibility = View.GONE
        binding.tvLoad.visibility = View.VISIBLE
        binding.imgBtnAddFavorite.visibility = View.VISIBLE
        binding.imgBtnRefresh.visibility = View.VISIBLE
        loadCurrentCryptoData(crypto.symbol)
    }

    private fun loadCurrentCryptoData(symbol: String) {
        apiService.getCurrentCryptoData(symbol).enqueue(object : Callback<CryptoData> {
            override fun onResponse(call: Call<CryptoData>, response: Response<CryptoData>) {
                if (response.isSuccessful && response.body() != null) {
                    val cryptoData = response.body()!!
                    if(currentCrypto != null) {
                        binding.tvNameActiv.visibility = View.VISIBLE
                        binding.tvNameActiv.text = currentCrypto!!.name
                    } else if(nameFavorite != "" && nameFavorite != "null") {
                        binding.tvNameActiv.visibility = View.VISIBLE
                        binding.tvNameActiv.text = nameFavorite
                    }

                    if(currentCrypto != null && databaseHelper.isCryptoAlreadyInFavorites(currentCrypto!!.name)) {
                        val res = requireActivity().getDrawable(R.drawable.icon_elect)
                        binding.imgBtnAddFavorite.background = res
                    } else {
                        val res = requireActivity().getDrawable(R.drawable.icon_not_elect)
                        binding.imgBtnAddFavorite.background = res
                    }

                    binding.btnLoadGraphic.visibility = View.VISIBLE
                    binding.tvLoad.visibility = View.GONE
                    binding.tvCrypto.visibility = View.GONE
                    binding.recyclerView.visibility = View.GONE
                    binding.imgBtnSearchCrypto.visibility = View.GONE
                    binding.hsvStocksMenu.visibility = View.VISIBLE
                    binding.currentPrice.visibility = View.VISIBLE
                    binding.volume.visibility = View.VISIBLE
                    binding.dailyChange.visibility = View.VISIBLE
                    binding.dayLow.visibility = View.VISIBLE
                    binding.dayHigh.visibility = View.VISIBLE
                    binding.previousClose.visibility = View.VISIBLE
                    binding.currentPrice.text = "Текущая цена: \$${formatValue(cryptoData.Open)}"
                    binding.volume.text = "Объем: \$${formatValue(cryptoData.Volume)}"
                    binding.dailyChange.text = "Дневное изменение: \$${formatValue(cryptoData.Open - cryptoData.Close)}"
                    binding.dayLow.text = "Минимум дня: \$${formatValue(cryptoData.Low)}"
                    binding.dayHigh.text = "Максимум дня: \$${formatValue(cryptoData.High)}"
                    binding.previousClose.text = "Цена закрытия: \$${formatValue(cryptoData.Close)}"
                } else {
                    Toast.makeText(requireContext(), "Ошибка загрузки текущих данных по криптовалюте", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CryptoData>, t: Throwable) {
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

    private fun loadHistoricalCryptoData(symbol: String, interval: String) {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val toDate = dateFormat.format(Calendar.getInstance().time)
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
        val fromDate = dateFormat.format(calendar.time)

        apiService.getHistoricalCryptoData(symbol, fromDate, toDate,0, interval).enqueue(object :
            Callback<List<JsonObject>> {
            override fun onResponse(call: Call<List<JsonObject>>, response: Response<List<JsonObject>>) {
                if (response.isSuccessful && response.body() != null) {
                    val historicalData = response.body()!!
                    val historicalJsonList = historicalData.map { jsonObject ->
                        val adjustedJson = JsonObject()

                        if(interval == "1d" || interval == "1wk" || interval == "1mo")
                            adjustedJson.addProperty("Date", jsonObject.get("Date").asString)
                        else
                            adjustedJson.addProperty("Datetime", jsonObject.get("Datetime").asString)

                        adjustedJson.addProperty("Adj Close_$symbol-USD", jsonObject.get("Adj Close_$symbol-USD").asDouble)
                        adjustedJson.addProperty("Close_$symbol-USD", jsonObject.get("Close_$symbol-USD").asDouble)
                        adjustedJson.addProperty("High_$symbol-USD", jsonObject.get("High_$symbol-USD").asDouble)
                        adjustedJson.addProperty("Low_$symbol-USD", jsonObject.get("Low_$symbol-USD").asDouble)
                        adjustedJson.addProperty("Open_$symbol-USD", jsonObject.get("Open_$symbol-USD").asDouble)
                        adjustedJson.addProperty("Volume_$symbol-USD", jsonObject.get("Volume_$symbol-USD").asInt)

                        adjustedJson
                    }
                    displayCryptoChart(historicalJsonList, symbol)
                } else {
                    Toast.makeText(requireContext(), "Ошибка загрузки исторических данных по криптовалюте", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<JsonObject>>, t: Throwable) {
                Toast.makeText(requireContext(), "Ошибка соединения: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun displayCryptoChart(data: List<JsonObject>, symbol: String) {
        binding.chart.visibility = View.VISIBLE
        val chart: CandleStickChart = binding.chart
        val candleEntries = mutableListOf<CandleEntry>()

        val highFieldName = "High_$symbol-USD"
        val lowFieldName = "Low_$symbol-USD"
        val openFieldName = "Open_$symbol-USD"
        val closeFieldName = "Close_$symbol-USD"
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

        val margin = (maxPrice - minPrice) * 0.3
        var adjustedMinPrice = minPrice - margin
        val adjustedMaxPrice = maxPrice + margin

        // Убедитесь, что минимальное значение оси Y не меньше 0
        if (adjustedMinPrice < 0) adjustedMinPrice = 0.0

        val candleDataSet = CandleDataSet(candleEntries, "Цена валютной пары (Candlestick)")
        candleDataSet.decreasingColor = Color.RED
        candleDataSet.decreasingPaintStyle = Paint.Style.FILL
        candleDataSet.increasingColor = Color.GREEN
        candleDataSet.increasingPaintStyle = Paint.Style.FILL
        candleDataSet.shadowColor = Color.LTGRAY
        candleDataSet.shadowWidth = 0.7f
        candleDataSet.neutralColor = Color.DKGRAY
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
        chart.axisLeft.setAxisMinimum(adjustedMinPrice.toFloat()) // Устанавливаем минимальное значение оси Y
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
        arguments = null
        viewModel.nameFavorite = "null1"
        findNavController().popBackStack(R.id.electFragment, false)
        clearCandleStickChart()
        binding.btnLoadGraphic.visibility = View.GONE
        binding.imgBtnBackToCryptoFavorite.visibility = View.GONE
        binding.imgBtnBackToCrypto.visibility = View.GONE
        binding.btnLoadGraphic.visibility = View.GONE
        binding.imgBtnAddFavorite.visibility = View.GONE
        binding.imgBtnSearchCrypto.visibility = View.VISIBLE
        binding.hsvStocksMenu.visibility = View.GONE
        binding.chart.visibility = View.GONE
        binding.currentPrice.visibility = View.GONE
        binding.volume.visibility = View.GONE
        binding.dailyChange.visibility = View.GONE
        binding.dayLow.visibility = View.GONE
        binding.dayHigh.visibility = View.GONE
        binding.previousClose.visibility = View.GONE
        binding.recyclerView.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        arguments = null
        viewModel.nameFavorite = "null1"
        findNavController().popBackStack(R.id.electFragment, false)
    }

}
