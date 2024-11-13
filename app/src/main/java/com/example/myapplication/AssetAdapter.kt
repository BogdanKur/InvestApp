package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class AssetAdapter(
    private val onAssetSelected: (Asset) -> Unit
) : RecyclerView.Adapter<AssetAdapter.AssetViewHolder>() {
    private val assets = mutableListOf<Asset>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AssetViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_asset, parent, false)
        return AssetViewHolder(view)
    }

    fun getAssets(): List<Asset> {
        return assets
    }

    override fun onBindViewHolder(holder: AssetViewHolder, position: Int) {
        holder.bind(assets[position])
    }

    override fun getItemCount(): Int = assets.size

    fun setAssets(newAssets: List<Asset>) {
        assets.clear()
        assets.addAll(newAssets)
        notifyDataSetChanged()
    }

    inner class AssetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val assetName: TextView = itemView.findViewById(R.id.assetName)
        private val assetSymbol: TextView = itemView.findViewById(R.id.assetSymbol)

        fun bind(asset: Asset) {

            assetName.text = asset.name
            assetSymbol.text = asset.symbol
            itemView.setOnClickListener {
                onAssetSelected(asset)
            }
        }
    }
}