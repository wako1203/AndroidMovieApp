package com.example.dacs.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.dacs.FavoriteActivity
import com.example.dacs.HistoryActivity
import com.example.dacs.MainActivity
import com.example.dacs.R
import com.example.dacs.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference


private lateinit var binding: FragmentProfileBinding
class ProfileFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater)

        val bundle = arguments
        val name = bundle?.getString("name")
        val id = bundle?.getString("id")

        binding.tvUser.text = name
        binding.btnLichsu.setOnClickListener {
            val intent = Intent(context, HistoryActivity::class.java)
            intent.putExtra("idUser", id)
            startActivity(intent)
        }
        binding.btnYeuThich.setOnClickListener {
            val intent = Intent(context, FavoriteActivity::class.java)
            intent.putExtra("idUser", id)
            startActivity(intent)
        }
        binding.btnLogout.setOnClickListener {
            // Đăng xuất
            FirebaseAuth.getInstance().signOut()

            // Chuyển hướng đến màn hình đăng nhập
            val intent = Intent(context, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
        return binding.root
    }



}