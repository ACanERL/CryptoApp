package com.ahmetcanerol.acecrypto.fragment

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import com.ahmetcanerol.acecrypto.R
import com.ahmetcanerol.acecrypto.adapter.CoinListAdapter
import com.ahmetcanerol.acecrypto.databinding.FragmentMarketBinding
import com.ahmetcanerol.acecrypto.model.CoinListModel
import com.ahmetcanerol.acecrypto.viewmodel.CoinListViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*


class MarketFragment : Fragment() {
    private lateinit var mAut: FirebaseAuth
    lateinit var mainViewModel: CoinListViewModel
    private var adapter= CoinListAdapter()
    private var mList = ArrayList<CoinListModel>()
    lateinit var binding: FragmentMarketBinding
    private var totalBalance:Double=0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentMarketBinding.inflate(inflater,container,false)
        setHasOptionsMenu(true);
        (activity as AppCompatActivity?)!!.setSupportActionBar(binding.myToolbar)

        mainViewModel= ViewModelProvider(this)[CoinListViewModel::class.java]
        mainViewModel.getAllCoin()
        getObserverData()


        binding.swiperefreslayout.setOnRefreshListener {
            binding.swiperefreslayout.isRefreshing=false
            binding.progressBar.visibility=View.VISIBLE
            mainViewModel.getAllCoin()
            getObserverData()
        }
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search, menu)
        val menuItem = menu.findItem(R.id.ara)
        val searchView = menuItem.actionView as SearchView
        searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                adapter.notifyDataSetChanged()
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    filter(newText)
                }
                return true
            }
        })

    }
    fun filter(text: String) {
        val coinslist=ArrayList<CoinListModel>()
        for (model in mList) {
            if (model.name.toLowerCase().contains(text.lowercase(Locale.getDefault()))) {
                coinslist.add(model)
            }
            else {
                adapter.setCoin(coinslist)
            }
        }
    }
    private fun getObserverData(){
        mainViewModel.coinListLiveData.observe(viewLifecycleOwner, Observer {
            mList= it as ArrayList<CoinListModel>
            binding.recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
            adapter.setCoin(it as ArrayList<CoinListModel>)
            binding.recyclerView.adapter = adapter
            binding.progressBar.visibility=View.GONE


        })
        adapter.onItemClick={
            val bundle = bundleOf(
                "name" to it.name,
                "price" to it.current_price,
                "coinrank" to it.marketcaprank
            )
            var wallettotalcoinAmout=0.0
            var walletquantity=0.0
            findNavController().navigate(R.id.action_marketFragment_to_detailFragment,bundle)
        }
    }

}