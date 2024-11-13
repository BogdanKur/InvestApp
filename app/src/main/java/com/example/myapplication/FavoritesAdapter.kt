package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FavoritesAdapter(
    private val databaseHelper: FavoritesDatabaseHelper,
    private var cryptoList: MutableList<Crypto>,
    private var currencyPairList: MutableList<CurrencyPair>,
    private var assetList: MutableList<Asset>,
    private val onCryptoItemClicked: (Crypto) -> Unit,
    private val onCurrencyPairItemClicked: (CurrencyPair) -> Unit,
    private val onAssetItemClicked: (Asset) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_CRYPTO = 0
        private const val VIEW_TYPE_CURRENCY_PAIR = 1
        private const val VIEW_TYPE_ASSET = 2
    }


    inner class CryptoFavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textName: TextView = itemView.findViewById(R.id.textName)
        private val textSymbol: TextView = itemView.findViewById(R.id.textSymbol)
        private val textType: TextView = itemView.findViewById(R.id.textType)
        private val delete: ImageButton = itemView.findViewById(R.id.imgBtnDelete)

        fun bind(crypto: Crypto) {
            textName.text = crypto.name
            textSymbol.text = crypto.symbol
            textType.text = "Криптовалюта"
            delete.setOnClickListener {
                databaseHelper.removeCryptoFromFavorites(crypto.symbol)
                removeCryptoItem(adapterPosition)
            }

            itemView.setOnClickListener {
                onCryptoItemClicked(crypto)
            }
        }
    }

    inner class CurrencyFavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textName: TextView = itemView.findViewById(R.id.textName)
        private val textSymbol: TextView = itemView.findViewById(R.id.textSymbol)
        private val textType: TextView = itemView.findViewById(R.id.textType)
        private val delete: ImageButton = itemView.findViewById(R.id.imgBtnDelete)

        fun bind(currencyPair: CurrencyPair) {
            textName.text = currencyPair.base
            textSymbol.text = currencyPair.second
            textType.text = "Валюта"
            delete.setOnClickListener {
                databaseHelper.removeCurrencyPairFromFavorites(currencyPair.base, currencyPair.second)
                removeCurrencyPairItem(adapterPosition)
            }
            itemView.setOnClickListener {
                onCurrencyPairItemClicked(currencyPair)
            }
        }
    }

    inner class AssetFavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textName: TextView = itemView.findViewById(R.id.textName)
        private val textSymbol: TextView = itemView.findViewById(R.id.textSymbol)
        private val textType: TextView = itemView.findViewById(R.id.textType)
        private val delete: ImageButton = itemView.findViewById(R.id.imgBtnDelete)

        fun bind(asset: Asset) {
            textName.text = asset.name
            textSymbol.text = asset.symbol
            textType.text = "Акция"
            delete.setOnClickListener {
                databaseHelper.removeAssetFromFavorites(asset.symbol)
                removeAssetItem(adapterPosition)
            }
            itemView.setOnClickListener {
                onAssetItemClicked(asset)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            position < cryptoList.size -> VIEW_TYPE_CRYPTO
            position < cryptoList.size + currencyPairList.size -> VIEW_TYPE_CURRENCY_PAIR
            else -> VIEW_TYPE_ASSET
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_CRYPTO -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_crypto_favorite, parent, false)
                CryptoFavoriteViewHolder(view)
            }
            VIEW_TYPE_CURRENCY_PAIR -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_currency_favorite, parent, false)
                CurrencyFavoriteViewHolder(view)
            }
            VIEW_TYPE_ASSET -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_stocks_favorite, parent, false)
                AssetFavoriteViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CryptoFavoriteViewHolder -> holder.bind(cryptoList[position])
            is CurrencyFavoriteViewHolder -> holder.bind(currencyPairList[position - cryptoList.size])
            is AssetFavoriteViewHolder -> holder.bind(assetList[position - cryptoList.size - currencyPairList.size])
        }
    }

    override fun getItemCount(): Int {
        return cryptoList.size + currencyPairList.size + assetList.size
    }

    private fun removeCryptoItem(position: Int) {
        cryptoList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)
    }

    private fun removeCurrencyPairItem(position: Int) {
        val adjustedPosition = position - cryptoList.size
        currencyPairList.removeAt(adjustedPosition)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)
    }

    private fun removeAssetItem(position: Int) {
        val adjustedPosition = position - cryptoList.size - currencyPairList.size
        assetList.removeAt(adjustedPosition)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)
    }

    fun updateLists(
        newCryptoList: List<Crypto>,
        newCurrencyPairList: List<CurrencyPair>,
        newAssetList: List<Asset>
    ) {
        cryptoList = newCryptoList.toMutableList()
        currencyPairList = newCurrencyPairList.toMutableList()
        assetList = newAssetList.toMutableList()

        notifyDataSetChanged()
    }
}

