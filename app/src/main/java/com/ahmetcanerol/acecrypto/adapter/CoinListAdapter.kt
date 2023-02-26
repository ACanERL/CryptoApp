package com.ahmetcanerol.acecrypto.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.ahmetcanerol.acecrypto.R
import com.ahmetcanerol.acecrypto.fragment.MarketFragmentDirections
import com.ahmetcanerol.acecrypto.model.CoinListModel
import com.squareup.picasso.Picasso
import java.text.NumberFormat
import java.util.*
import javax.inject.Inject

class CoinListAdapter @Inject constructor(): RecyclerView.Adapter<CoinListAdapter.ViewHolder>(){
    var coins: List<CoinListModel> = emptyList()
    var coinsFilterList = ArrayList<CoinListModel>()
    var onItemClick: ((CoinListModel) -> Unit)? = null
    lateinit var context:Context

    fun setCoin(coin: List<CoinListModel>) {
        this.coins = coin.toMutableList() as ArrayList<CoinListModel>
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinListAdapter.ViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.coin_item,parent,false);
        return CoinListAdapter.ViewHolder(view)
    }
    override fun onBindViewHolder(holder: CoinListAdapter.ViewHolder, position: Int) {
        holder.coinName.text= coins[position].name
        var totalv:Double=coins[position].total_volume
        val usFormatter: NumberFormat = NumberFormat.getInstance(Locale("en", "US"))
        holder.totalvolume.text=("Total Vol "+usFormatter.format(totalv))
        val price= coins[position].current_price
        holder.coinPrice.text="%.3f".format(price)
        val change= coins[position].price_change_percentage_1h_in_currency
        if(change>0){
            holder.change.setTextColor(android.graphics.Color.WHITE)
            holder.change.setBackgroundColor(android.graphics.Color.GREEN)
        }
        else{
            holder.change.setTextColor(android.graphics.Color.WHITE)
            holder.change.setBackgroundColor(android.graphics.Color.RED)
        }
        holder.change.text="%.2f".format(change)
       Picasso.get().load(coins[position].image).placeholder(R.drawable.ic_loading).into(holder.coinImage)
        holder.itemView.setOnClickListener {
                onItemClick?.invoke(coins[position])
        }
    }
    override fun getItemCount(): Int {
        return coins.size
    }
    class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        var coinName:TextView=view.findViewById(R.id.coinName)
        var coinPrice:TextView=view.findViewById(R.id.lastprice)
        var coinImage: ImageView =view.findViewById(R.id.coinImage)
        var totalvolume:TextView=view.findViewById(R.id.total)
        var change:Button=view.findViewById(R.id.change)

    }
}