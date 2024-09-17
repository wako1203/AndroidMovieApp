package com.example.dacs

import FavoriteAdapter
import HistoryAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dacs.Data.Episode
import com.example.dacs.Data.Favorite
import com.example.dacs.Data.History
import com.example.dacs.Fragments.FavouriteFragment
import com.example.dacs.Fragments.HomeFragment
import com.example.dacs.Fragments.ProfileFragment
import com.example.dacs.databinding.ActivityFavoriteBinding
import com.example.dacs.databinding.ActivityHistoryBinding
import com.google.firebase.database.*
import java.util.zip.Inflater

private lateinit var binding: ActivityFavoriteBinding
private lateinit var recyclerView: RecyclerView
private lateinit var favoriteAdapter: FavoriteAdapter
private lateinit var db: DatabaseReference
private val favorites= mutableListOf<Favorite>()


class FavoriteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val i = intent
        val id = i.getStringExtra("idUser")
        recyclerView = binding.rvFavorite
        recyclerView.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
        db = FirebaseDatabase.getInstance().getReference("Favorites")
        db.orderByChild("idUser").equalTo(id).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                favorites.clear()
                if (snapshot.exists()) {
                    for (h in snapshot.children) {
                        favorites.add(h.getValue(Favorite::class.java)!!)
                    }
                }
                favoriteAdapter = FavoriteAdapter(favorites)
                recyclerView.adapter = favoriteAdapter
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })


        val textView = findViewById<TextView>(R.id.back)
        textView.setOnClickListener {
            val intent = Intent(this, Homepage::class.java)
            startActivity(intent)
        }


    }

}