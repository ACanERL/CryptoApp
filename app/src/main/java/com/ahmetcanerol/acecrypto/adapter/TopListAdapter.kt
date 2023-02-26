package com.ahmetcanerol.acecrypto.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.ahmetcanerol.acecrypto.R
import com.ahmetcanerol.acecrypto.model.CoinListModel
import java.text.NumberFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class TopListAdapter @Inject constructor(): RecyclerView.Adapter<TopListAdapter.ViewHolder>() {
    var coins: List<CoinListModel> = emptyList()
    var coinsFilterList = ArrayList<CoinListModel>()
    var onItemClick: ((CoinListModel) -> Unit)? = null

    fun setCoin(coin: List<CoinListModel>) {
        this.coins = coin.toMutableList() as ArrayList<CoinListModel>
        notifyDataSetChanged()
    }
    class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        var cName:TextView=view.findViewById(R.id.cName)
        var cPrice:TextView=view.findViewById(R.id.Cprice)
        var cChange:TextView=view.findViewById(R.id.Cchange)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.topfive,parent,false);
        return TopListAdapter.ViewHolder(view)
    }
    override fun getItemCount(): Int {
       return coins.size
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.cName.text= coins[position].name
        val price= coins[position].current_price
        holder.cPrice.text="%.3f".format(price)
        val change_24h=coins.get(position).price_change_percentage_24h
        if(change_24h>0){
            holder.cChange.text="%.4f".format(change_24h)
            holder.cChange.setTextColor(android.graphics.Color.GREEN)
        }else{
            holder.cChange.text="%.4f".format(change_24h)
            holder.cChange.setTextColor(android.graphics.Color.RED)
        }
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(coins[position])
        }


    }
}