package com.ahmetcanerol.acecrypto.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.currentComposer
import androidx.fragment.app.Fragment
import com.ahmetcanerol.acecrypto.LoginActivity
import com.ahmetcanerol.acecrypto.R
import com.ahmetcanerol.acecrypto.databinding.FragmentProfilBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import kotlin.system.exitProcess


class ProfilFragment : Fragment() {
    private lateinit var mAut: FirebaseAuth
     private lateinit var binding:FragmentProfilBinding
    private lateinit var googleSignInClient: GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding=FragmentProfilBinding.inflate(inflater, container, false)
        mAut= FirebaseAuth.getInstance()
        val gso= GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail().build()
        googleSignInClient= GoogleSignIn.getClient(requireContext(),gso)
        setHasOptionsMenu(true);
        (activity as AppCompatActivity?)!!.setSupportActionBar(binding.myToolbar)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.profil_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.signout->{
                mAut.signOut()
                val intent=Intent(requireContext(),LoginActivity::class.java)
                startActivity(intent)
            }
            R.id.changeacc->{
                mAut.signOut()
                googleSignInClient.signOut()
                val intent=Intent(requireContext(),LoginActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)

    }
}