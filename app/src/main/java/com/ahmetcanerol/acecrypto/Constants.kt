package com.ahmetcanerol.acecrypto

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object Constants {
    const val BASE_URL="https://api.coingecko.com/"
    const val day1:String="1"
    const val day7:String="7"
    const val day14:String="14"
    const val base_url="https://rest.coinapi.io/"


    var coinPrice:Double=0.0
    var totalBalance:Double=0.0 // toplam kullanıcı balance
    var totalAmout:Double=0.0 //toplam mikter
    var coinName:String=""
    var qt:Double=0.0  // adet mikter
    var wallettotalcoinAmout:Double=0.0
    var walletquantity:Double=0.0

    var getQt=0.0
    var getTamout=0.0



}