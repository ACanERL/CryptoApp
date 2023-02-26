package com.ahmetcanerol.acecrypto.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager

import com.ahmetcanerol.acecrypto.Constants.totalBalance

import com.ahmetcanerol.acecrypto.R
import com.ahmetcanerol.acecrypto.adapter.TopListAdapter
import com.ahmetcanerol.acecrypto.databinding.FragmentHomeBinding
import com.ahmetcanerol.acecrypto.model.CoinListModel
import com.ahmetcanerol.acecrypto.viewmodel.CoinListViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat
import java.util.*

class HomeFragment : Fragment() {
    private lateinit var mAut: FirebaseAuth
    lateinit var mainViewModel: CoinListViewModel
    lateinit var binding:FragmentHomeBinding
    private var mList = ArrayList<CoinListModel>()
    private var adapter= TopListAdapter()
    private val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    @SuppressLint("MissingInflatedId", "SuspiciousIndentation")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding=FragmentHomeBinding.inflate(inflater,container,false)
      //  val usFormatter: NumberFormat = NumberFormat.getInstance(Locale("en", "US"))
      //  usFormatter.format(totalBalance)
        binding.userBalance.text= totalBalance.toString()

        mainViewModel= ViewModelProvider(this)[CoinListViewModel::class.java]
        mainViewModel.getTop_5()
        getTop_5()
        mAut= FirebaseAuth.getInstance()
        val curretUser=mAut.currentUser
        val photoUrl=curretUser?.photoUrl
        getUserData()

        binding.userN.text=curretUser?.displayName
        Picasso.get().load(photoUrl).into(binding.profileImage)

      binding.profileImage.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_profilFragment)
        }/*
       binding.buy.setOnClickListener {

            findNavController().navigate(R.id.action_homeFragment_to_detailFragment)
        }
        binding.sell.setOnClickListener {

            findNavController().navigate(R.id.action_homeFragment_to_detailFragment)
        }
        binding.send.setOnClickListener {

            findNavController().navigate(R.id.action_homeFragment_to_detailFragment)
        }
        binding.exchange.setOnClickListener {

            findNavController().navigate(R.id.action_homeFragment_to_detailFragment)
        }*/
        adapter.onItemClick={
            val bundle = bundleOf(
                "name" to it.name,
                "price" to it.current_price
            )

            findNavController().navigate(R.id.action_homeFragment_to_detailFragment,bundle)


        }
        return binding.root
    }
    private fun getTop_5(){
        mainViewModel.top_5.observe(viewLifecycleOwner, Observer {
            mList= it as ArrayList<CoinListModel>
            binding.recyclerView2.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)
            adapter.setCoin(it as ArrayList<CoinListModel>)
            binding.recyclerView2.adapter = adapter
        })
    }
    private fun getUserData(){
        val user=mAut.currentUser
        val db = Firebase.firestore
        db.collection("USER")
            .get().addOnSuccessListener {
                for (document in it){
                    if(document.id==user?.uid){
                        println(document.data)
                        totalBalance= document.data["balance"] as Double
                        val balance = String.format("%.2f",totalBalance)
                        binding.userBalance.text=balance
                    }
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(),"Error DataSnapshot",Toast.LENGTH_SHORT).show()
            }
    }
    
}