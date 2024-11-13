package com.example.myapplication

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemCurrencyBinding

class ConvertCurrencyAdapter(private val onClick: (String) -> Unit) : RecyclerView.Adapter<ConvertCurrencyAdapter.CurrencyViewHolder>() {

    private var currencies: List<String> = listOf()
    private var selectedPosition: Int = RecyclerView.NO_POSITION

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        val binding = ItemCurrencyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CurrencyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        val currency = currencies[position]
        holder.bind(currency)
    }

    override fun getItemCount(): Int = currencies.size

    fun setCurrencies(currencies: List<String>) {
        this.currencies = currencies
        notifyDataSetChanged()
    }

    fun getCurrencies(): List<String> {
        return currencies
    }

    inner class CurrencyViewHolder(private val binding: ItemCurrencyBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(currency: String) {
            val selectedBackground = ContextCompat.getDrawable(binding.root.context, R.drawable.item_country_asset_background_choose)
            val defaultBackground = ContextCompat.getDrawable(binding.root.context, R.drawable.item_country_asset_background)

            if (position == selectedPosition) {
                binding.root.background = selectedBackground
            } else {
                binding.root.background = defaultBackground
            }
            binding.currencyPairName.text = currency
            binding.root.setOnClickListener {
                selectedPosition = position
                notifyDataSetChanged()
                onClick(currency)
            }
        }
    }
}
