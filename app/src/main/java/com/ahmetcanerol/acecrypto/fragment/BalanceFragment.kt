package com.ahmetcanerol.acecrypto.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ahmetcanerol.acecrypto.R
import com.ahmetcanerol.acecrypto.adapter.WalletAdapter
import com.ahmetcanerol.acecrypto.databinding.FragmentBalanceBinding
import com.ahmetcanerol.acecrypto.local.viewmodel.UserCViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class BalanceFragment : Fragment() {
    private lateinit var mAut: FirebaseAuth
   private lateinit var binding: FragmentBalanceBinding
    private lateinit var userCViewModel: UserCViewModel
    private lateinit var recyclerViewAdapter: WalletAdapter
    var totalBalance:Double=0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): ConstraintLayout {
        // Inflate the layout for this fragment
        binding= FragmentBalanceBinding.inflate(inflater, container, false)
        mAut= FirebaseAuth.getInstance()
        val user=mAut.currentUser

        getUserBalance()
        userCViewModel = ViewModelProvider(this)[UserCViewModel::class.java]
        binding.walletRecycler.layoutManager = LinearLayoutManager(context)
        recyclerViewAdapter = WalletAdapter()
        binding.walletRecycler.adapter = recyclerViewAdapter



        userCViewModel.readAllData.observe(viewLifecycleOwner, Observer {
            recyclerViewAdapter.setData(it)
            if (it.isEmpty()) {
                Toast.makeText(context, "Coin Wallet Empty", Toast.LENGTH_SHORT).show()
            }
        })

        return binding.root
    }

    private fun getUserBalance(){
        val user=mAut.currentUser
        val db = Firebase.firestore
        db.collection("USER")
            .get().addOnSuccessListener {
                for (document in it){
                    if(document.id==user?.uid){
                        println(document.data)
                        totalBalance= document.data["balance"] as Double
                        binding.totalBalance.text=totalBalance.toString()
                    }
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(),"Error DataSnapshot", Toast.LENGTH_SHORT).show()
            }
    }
}