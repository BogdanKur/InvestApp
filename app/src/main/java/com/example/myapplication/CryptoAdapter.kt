package com.example.myapplication

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemCryptoBinding

class CryptoAdapter(
    private val onItemClick: (Crypto) -> Unit
) : RecyclerView.Adapter<CryptoAdapter.CryptoViewHolder>() {

    private val cryptos = mutableListOf<Crypto>()

    fun setCryptos(newCryptos: List<Crypto>) {
        cryptos.clear()
        cryptos.addAll(newCryptos)
        notifyDataSetChanged()
    }

    fun getCryptos(): List<Crypto> = cryptos

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CryptoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCryptoBinding.inflate(inflater, parent, false)
        return CryptoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CryptoViewHolder, position: Int) {
        holder.bind(cryptos[position])
    }

    override fun getItemCount(): Int = cryptos.size

    inner class CryptoViewHolder(private val binding: ItemCryptoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(crypto: Crypto) {
            binding.cryptoName.text = crypto.name
            binding.cryptoSymbol.text = crypto.symbol
            itemView.setOnClickListener {
                onItemClick(crypto)
            }
        }
    }
}
