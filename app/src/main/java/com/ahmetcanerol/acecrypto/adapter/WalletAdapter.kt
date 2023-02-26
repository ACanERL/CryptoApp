package com.ahmetcanerol.acecrypto.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ahmetcanerol.acecrypto.R
import com.ahmetcanerol.acecrypto.model.UserCoins

class WalletAdapter: RecyclerView.Adapter<WalletAdapter.ViewHolder>() {
    private var userCList = emptyList<UserCoins>()
    class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val cN=view.findViewById<TextView>(R.id.coinname)
        val cQ=view.findViewById<TextView>(R.id.coinQ)

        fun bind(user:UserCoins){
            cN.text = user.coinsName
            cQ.text = user.coinQu
        }

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_wallet, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return userCList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentUser= userCList[position]
        holder.bind(currentUser)
    }

    fun setData(userCList: List<UserCoins>) {
        this.userCList = userCList
        notifyDataSetChanged()
    }
}