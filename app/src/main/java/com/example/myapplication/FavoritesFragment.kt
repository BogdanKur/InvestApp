package com.example.myapplication

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.myapplication.databinding.FragmentFavoritesBinding

class FavoritesFragment : Fragment() {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private lateinit var favoritesAdapter: FavoritesAdapter
    private lateinit var databaseHelper: FavoritesDatabaseHelper

    private var hasNavigated = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        val view = binding.root
        databaseHelper = FavoritesDatabaseHelper(requireContext())

        val cryptoList = getCryptoList().toMutableList()
        val currencyPairList = getCurrencyPairList().toMutableList()
        val assetList = getFavoritesAsset().toMutableList()

        favoritesAdapter = FavoritesAdapter(
            databaseHelper,
            cryptoList = cryptoList,
            currencyPairList = currencyPairList,
            assetList = assetList,
            onCryptoItemClicked = { selectedCrypto ->
                if (!hasNavigated) {
                    hasNavigated = true
                    val bundle = Bundle().apply {
                        putString("nameCrypto", selectedCrypto.name)
                        putString("symbolCrypto", selectedCrypto.symbol)
                    }
                    findNavController().navigate(R.id.action_electFragment_to_cryptoCurrencyFragment, bundle)
                    bundle.clear()
                }
            },
            onCurrencyPairItemClicked = { selectedCurrency ->
                if (!hasNavigated) {
                    hasNavigated = true
                    val bundle = Bundle().apply {
                        putString("nameBase", selectedCurrency.base)
                        putString("nameSecond", selectedCurrency.second)
                    }
                    findNavController().navigate(R.id.action_electFragment_to_currencyFragment, bundle)
                    bundle.clear()
                }
            },
            onAssetItemClicked = { selectedAsset ->
                if (!hasNavigated) {
                    hasNavigated = true
                    val bundle = Bundle().apply {
                        putString("nameAsset", selectedAsset.name)
                        putString("symbolAsset", selectedAsset.symbol)
                        putString("countryAsset", selectedAsset.country)
                        putString("fullNameAsset", selectedAsset.full_name)
                        putString("isinAsset", selectedAsset.isin)
                        putString("currencyAsset", selectedAsset.currency)
                    }
                    findNavController().navigate(R.id.action_electFragment_to_mainFragment, bundle)
                    bundle.clear()
                }
            }
        )
        if(cryptoList.isEmpty() && currencyPairList.isEmpty() && assetList.isEmpty()) binding.tvEmptyFavorite.visibility = View.VISIBLE
        else binding.tvEmptyFavorite.visibility = View.GONE
        binding.recyclerView.adapter = favoritesAdapter
        return view
    }

    private fun getCryptoList(): List<Crypto> {
        return databaseHelper.getFavorites()
    }

    private fun getCurrencyPairList(): List<CurrencyPair> {
        return databaseHelper.getCurrencyPairs()
    }

    private fun getFavoritesAsset(): List<Asset> {
        return databaseHelper.getFavoritesAsset()
    }

    override fun onResume() {
        super.onResume()
        findNavController().popBackStack(R.id.electFragment, false)
    }
    override fun onPause() {
        super.onPause()

        hasNavigated = false
    }
}
