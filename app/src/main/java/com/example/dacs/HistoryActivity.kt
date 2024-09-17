package com.example.dacs

import HistoryAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dacs.Data.Episode
import com.example.dacs.Data.History
import com.example.dacs.databinding.ActivityHistoryBinding
import com.google.firebase.database.*

private lateinit var binding: ActivityHistoryBinding
private lateinit var recyclerView: RecyclerView
private lateinit var historyAdapter: HistoryAdapter
private lateinit var db: DatabaseReference
private val histories= mutableListOf<History>()


class HistoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val i = intent
        val id = i.getStringExtra("idUser")
        recyclerView = binding.rvHistory
        recyclerView.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
        db = FirebaseDatabase.getInstance().getReference("History")
        db.orderByChild("idUser").equalTo(id).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                histories.clear()
                if (snapshot.exists()) {
                    for (h in snapshot.children) {
                        histories.add(h.getValue(History::class.java)!!)
                    }
                }
                historyAdapter = HistoryAdapter(histories)
                recyclerView.adapter = historyAdapter
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