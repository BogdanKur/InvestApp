package com.example.myapplication

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemCurrencyBinding
import com.google.api.ResourceProto.resource

class CurrencyAdapter(private val onClick: (CurrencyPair) -> Unit) : RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder>() {

    private var currencies: List<CurrencyPair> = listOf()
    private var selectedPosition: Int = RecyclerView.NO_POSITION

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        val binding = ItemCurrencyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CurrencyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        val currencyPair = currencies[position]
        holder.bind(currencyPair, position)
    }

    override fun getItemCount(): Int = currencies.size

    fun setCurrencies(currencies: List<CurrencyPair>) {
        this.currencies = currencies
        notifyDataSetChanged()
    }

    fun getCurrencies(): List<CurrencyPair> {
        return currencies
    }

    inner class CurrencyViewHolder(private val binding: ItemCurrencyBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(currencyPair: CurrencyPair, position: Int) {
            binding.currencyPairName.text = currencyPair.base

            val selectedBackground = ContextCompat.getDrawable(binding.root.context, R.drawable.item_country_asset_background_choose)
            val defaultBackground = ContextCompat.getDrawable(binding.root.context, R.drawable.item_country_asset_background)

            if (position == selectedPosition) {
                binding.root.background = selectedBackground
            } else {
                binding.root.background = defaultBackground
            }

            binding.root.setOnClickListener {
                selectedPosition = position
                notifyDataSetChanged()
                onClick(currencyPair)
            }
        }
    }
}
