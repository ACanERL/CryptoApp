package com.ahmetcanerol.acecrypto.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ahmetcanerol.acecrypto.Constants.coinName
import com.ahmetcanerol.acecrypto.Constants.coinPrice
import com.ahmetcanerol.acecrypto.Constants.getQt
import com.ahmetcanerol.acecrypto.Constants.getTamout
import com.ahmetcanerol.acecrypto.Constants.qt
import com.ahmetcanerol.acecrypto.Constants.totalAmout
import com.ahmetcanerol.acecrypto.Constants.totalBalance
import com.ahmetcanerol.acecrypto.Constants.walletquantity
import com.ahmetcanerol.acecrypto.Constants.wallettotalcoinAmout
import com.ahmetcanerol.acecrypto.databinding.FragmentDetailBinding
import com.ahmetcanerol.acecrypto.local.viewmodel.UserCViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class DetailFragment : Fragment() {
    private lateinit var binding: FragmentDetailBinding
    val curretUser= FirebaseAuth.getInstance().currentUser
    private val db = Firebase.firestore
    private lateinit var userCViewModel: UserCViewModel

    var adettoplami:Double=0.0
    var coinadeti:Double=0.0
    val data: MutableMap<String, Any> = HashMap()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding=FragmentDetailBinding.inflate(inflater, container, false)
        userCViewModel = ViewModelProvider(this)[UserCViewModel::class.java]
        getUserBalance()
        binding.coinN.text=arguments?.getString("name")
        binding.coinP.text=arguments?.getDouble("price").toString()
        coinPrice= arguments?.getDouble("price")!!  //gelen coin fiyati
        coinName= arguments?.getString("name").toString() //gelen coin adi




        //edittext controller
       var string="0"
        binding.quantity.addTextChangedListener{
            if(binding.quantity.text.isEmpty())
            {
                var txt=Editable.Factory.getInstance().newEditable(string)
                binding.quantity.text.clear()
            }
            else{
                var q=binding.quantity.text.toString()
                qt=q.toDouble()
                totalAmout=(coinPrice*qt)
                val balance = String.format("%.2f",totalAmout)
                binding.totalAmout.text=balance
            }

            val data: MutableMap<String, Any> = HashMap()
            data["coinadeti"] = qt
            data["adetToplami"]= totalAmout
            val userRef=db.collection("USER").document(curretUser!!.uid).collection("Coins").document(
                coinName)
            userRef.set(data)

        }



      binding.buybtn.setOnClickListener {
          getUserBalance()

          println("User Total Balance:${totalBalance}")
          totalBalance -= totalAmout

              if (curretUser != null) {
                  if (totalBalance < 0) {  // alim miktari sonucu balance - fiyata dusururse
                      Toast.makeText(requireContext(), "Not enough balance.", Toast.LENGTH_SHORT).show()
                  }else{
                      db.collection("Wallet").document(curretUser!!.uid).collection("Coins").get().addOnSuccessListener {
                          for(doc in it){
                               coinadeti= doc.data["coinadeti"] as Double
                               adettoplami= doc.data["adetToplami"] as Double
                              println(coinadeti)
                          }
                              val userRef=db.collection("USER").document(curretUser!!.uid).collection("Coins").document(
                                  coinName)
                              userRef.update(data)
                      }
                      db.collection("USER").document(curretUser.uid).update("balance", totalBalance)
                      binding.userB.text= totalBalance.toString()
                  }
              }
      }



        //satma işlemleri
        binding.sellbtn.setOnClickListener {
            getUserBalance()
            totalBalance+=qt*coinPrice //satilinca yeni guncell  balance
            walletquantity-=qt //yeni güncell adet
            wallettotalcoinAmout+= qt*coinPrice
            if(walletquantity==0.0){
                Toast.makeText(requireContext(),"Not enough amount of coins",Toast.LENGTH_SHORT).show()

            }else{
                db.collection("USER").document(curretUser!!.uid).update("balance", totalBalance)
                db.collection("USER").document(curretUser!!.uid).collection("userwallet").document(coinName).update("coinMiktari",walletquantity,"coinToplamFiyati",wallettotalcoinAmout)
            }

        }
        return binding.root
    }

    private fun getUserBalance(){  //kullanıcının toplam bakiyesini aliyorum
     db.collection("USER")
            .get().addOnSuccessListener {
                for (document in it){
                    if(document.id==curretUser?.uid){
                        println(document.data)
                        totalBalance= document.data["balance"] as Double
                        val balance = String.format("%.2f", totalBalance) ;
                        binding.userB.text=balance
                        println("User Balance:$balance")
                    }
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(),"Error DataSnapshot", Toast.LENGTH_SHORT).show()
            }
    }
}