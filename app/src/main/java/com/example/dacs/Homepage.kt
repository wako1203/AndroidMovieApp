package com.example.dacs

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.dacs.Fragments.FavouriteFragment
import com.example.dacs.Fragments.HomeFragment
import com.example.dacs.Fragments.ProfileFragment

import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.*

//private lateinit var binding: FragmentHomeBinding
//private lateinit var binding1: ActivityHomepageBinding
class Homepage : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        binding =  FragmentHomeBinding.inflate(layoutInflater)
//        binding1 = ActivityHomepageBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_homepage)
        val i = intent
        val name = i.getStringExtra("name")
        val id = i.getStringExtra("id")
        val homeFragment = HomeFragment()
        val favouriteFragment = FavouriteFragment()
        val profileFragment = ProfileFragment()
        val bundle = Bundle()
        bundle.putString("name", name)
        bundle.putString("id", id)
        profileFragment.arguments=bundle
        homeFragment.arguments=bundle
        favouriteFragment.arguments=bundle
        //Tạo fragment để hiển thị
        //Home page sẽ được vào đầu tiên
        replaceFragment(homeFragment)


        //Chỉnh các nút và tới các fragment được tạo
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.ic_home -> replaceFragment(homeFragment)
                R.id.ic_favourite -> replaceFragment(favouriteFragment)
                R.id.ic_profile -> replaceFragment(profileFragment)
            }
            true
        }

//        loadData()

    }

//    private fun loadData() {
//        loadFeatureData()
//    }



//    private fun loadFeatureData() {
//        dbRef = FirebaseDatabase.getInstance().getReference("phim mới")
//        val phimMoiRV = binding.recyclerViewPhimmoi
//        pmlist = ArrayList<DataModel>()
//
//        phimMoiRV.layoutManager = LinearLayoutManager (
//            this,
//            LinearLayoutManager.HORIZONTAL,
//            false)
//
//        val pmAdapter = PhimMoiAdapter(pmlist)
//
//        phimMoiRV.adapter = pmAdapter
//
//        dbRef.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                pmlist.clear()
//                for (contentSnapShot in snapshot.children) {
//                    val phimmoiData = contentSnapShot.getValue(DataModel::class.java)
//                    if (phimmoiData != null) {
//                        pmlist.add(phimmoiData)
//                    }
//                }
//                pmAdapter.notifyDataSetChanged()
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                Log.e(TAG, "Failed to read value.", error.toException())
//            }
//
//        })
//
//
//    }

    //ham thay dổi fragment
    private fun replaceFragment(movieFragment: Fragment) {
        val  fragmentManager = supportFragmentManager
        val framentTransaction = fragmentManager.beginTransaction()
        framentTransaction.replace(R.id.fl_wrapper, movieFragment)
        framentTransaction.commit()
    }


}