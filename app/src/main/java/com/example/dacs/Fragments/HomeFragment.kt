package com.example.dacs.Fragments

import android.content.Intent
import android.graphics.Movie
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.dacs.*
import com.example.dacs.Adapter.PhimMoiAdapter
import com.example.dacs.Adapter.TVShowAdapter
import com.example.dacs.Data.History
import com.example.dacs.Data.MovieData
import com.example.dacs.Data.TVShowData
import com.example.dacs.databinding.FragmentHomeBinding
import com.google.android.youtube.player.internal.m
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import kotlinx.coroutines.*
import java.net.URL
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


private lateinit var binding: FragmentHomeBinding
private lateinit var db: DatabaseReference

class HomeFragment : Fragment() {

    private lateinit var recyclerView1: RecyclerView
    private lateinit var recyclerView2: RecyclerView
    private lateinit var tvShowAdapter: TVShowAdapter
    private lateinit var moiAdapter: PhimMoiAdapter
    private lateinit var imageSlider: ImageSlider
    private val movies = mutableListOf<MovieData>()
    private val tvShows = mutableListOf<TVShowData>()
    private var movieList = mutableListOf<MovieData>()
    private var tvShowList = mutableListOf<TVShowData>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        recyclerView1 = view.findViewById(R.id.recyclerViewPhimmoi)
        recyclerView2 = view.findViewById(R.id.recyclerViewPhimbo)
        imageSlider = view.findViewById(R.id.imageSlider)

        val bundle = arguments
        val name = bundle?.getString("name")
        val id = bundle?.getString("id")

        recyclerView1.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        recyclerView2.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        tvShowAdapter = TVShowAdapter(tvShows)
        moiAdapter = PhimMoiAdapter(movies)
        recyclerView1.adapter = moiAdapter
        recyclerView2.adapter =  tvShowAdapter
        Thread {
            movies.clear()
            tvShows.clear()
            val url = "https://api.themoviedb.org/3/list/8248497?api_key=eb82a323e426d30d552550d47bc83e2b"
            val response = URL(url).readText()
            val gson = Gson()
            val movieResponse = gson.fromJson(response, MovieResponse::class.java)
            val tvShowResponse = gson.fromJson(response, TVShowResponse::class.java)
            movies.addAll(movieResponse.items)
            tvShows.addAll(tvShowResponse.items)
            movieList.clear()
            tvShowList.clear()
            movieList = movies.filter { movies -> movies.media_type == "movie" }.toMutableList()
            tvShowList = tvShows.filter { tvShows -> tvShows.media_type == "tv" }.toMutableList()
            activity?.runOnUiThread {
                tvShowAdapter = TVShowAdapter(tvShowList)
                moiAdapter = PhimMoiAdapter(movieList)
                recyclerView1.adapter = moiAdapter
                moiAdapter.setOnItemClickListener(object : PhimMoiAdapter.OnItemClickListener {
                    @RequiresApi(Build.VERSION_CODES.O)
                    override fun onItemClick(position: Int) {
                        val tvshow = movieList[position]
                        val intent = Intent(context, WatchMovie::class.java)
                        intent.putExtra("movieId", tvshow.id)
                        intent.putExtra("movieTitle", tvshow.title)
                        intent.putExtra("voteAverage", tvshow.vote_average)
                        intent.putExtra("movieOverview", tvshow.overview)
                        intent.putExtra("moviePoster", tvshow.poster_path)
                        intent.putExtra("mediaType", "movie")
                        intent.putExtra("name", name)
                        intent.putExtra("id", id)

                        SaveHistory(id.toString(),tvshow.media_type, tvshow.id.toString(), tvshow.title, tvshow.poster_path)
                        startActivity(intent)
                    }
                })

                recyclerView2.adapter = tvShowAdapter
                tvShowAdapter.setOnItemClickListener(object : TVShowAdapter.OnItemClickListener {
                    @RequiresApi(Build.VERSION_CODES.O)
                    override fun onItemClick(position: Int) {
                        val tvshow = tvShowList[position]
                        val intent = Intent(context, WatchMovie::class.java)
                        intent.putExtra("movieId", tvshow.id)
                        intent.putExtra("movieTitle", tvshow.name)
                        intent.putExtra("voteAverage", tvshow.vote_average)
                        intent.putExtra("movieOverview", tvshow.overview)
                        intent.putExtra("moviePoster", tvshow.poster_path)
                        intent.putExtra("mediaType", "tv")
                        intent.putExtra("name", name)
                        intent.putExtra("id", id)
                        SaveHistory(id.toString(),tvshow.media_type, tvshow.id.toString(), tvshow.name, tvshow.poster_path)
                        startActivity(intent)
                    }
                })
            }
        }.start()
//        val apiKey = "eb82a323e426d30d552550d47bc83e2b"
//        val url = "https://api.themoviedb.org/3/movie/popular?api_key=$apiKey"
//        MyAsyncTask().execute(url)
//        val jsonString = URL(url).readText()
//        val gson = Gson()
//        val movieList = gson.fromJson(jsonString, Array<DataModel>::class.java).toList()


//        val dbRef = FirebaseDatabase.getInstance().getReference("phim mới")
//        val pmList = mutableListOf<DataModel>()
//        pmList.add(DataModel(R.drawable.suzume, "Suzume"))
//        pmList.add(DataModel(R.drawable.taktop, "Đứa con của thời tiết"))
//        dbRef.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                pmList.clear()
//                for (contentSnapShot in snapshot.children) {
//                    val phimmoiData = contentSnapShot.getValue(DataModel::class.java)
//                    if (phimmoiData != null) {
//                        pmList.add(phimmoiData)
//                    }
//                }
//                vadapter.notifyDataSetChanged()
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                Log.e(ContentValues.TAG, "Failed to read value.", error.toException())
//            }
//        })
//        vadapter = PhimMoiAdapter(pmList)
//        recyclerView.adapter = vadapter
        val imageList = ArrayList<SlideModel>()
        imageList.add(
            SlideModel(
                "https://static.lag.vn/upload/news/22/12/12/anime-kimetsu-no-yaiba-season-3-cong-bo-them-movie-3_SQQW.jpg?w=800&encoder=wic&subsampling=444",
                "Demon Slayer: Kimetsu No Yaiba - To the Swordsmith Village"
            )
        )
        imageList.add(
            SlideModel(
                "https://s199.imacdn.com/vg/2022/11/24/fca8120309bfea7b_f8267129988ae630_11397616692764385118684.jpg",
                "MASHLE "
            )
        )
        imageList.add(
            SlideModel(
                "https://blogger.googleusercontent.com/img/b/R29vZ2xl/AVvXsEjTQ17Ms06jTiVtoqFEhYr13ruNuLCgQZTib45vHFstb0LKqoOckPtqyvDu2IhSsMShUMqGe0T6Qyxuq0OXbrLDbA-90FMzrZjwznpzo_4WVVhSB_2VDAJqNYwhDCVpHexT1EhCFKyriKJ6yxzFLB0J3DHoOa9WDfk3WsSe070aRTECrXjRKh3tShVsiA/s700/oshi-no-ko-anime-banner.jpg",
                "Oshi no Ko"
            )
        )
        imageList.add(
            SlideModel(
                "https://www.comingsoon.net/wp-content/uploads/sites/3/2022/04/6025e4b811cf2c61281856be28ffc2c91649000159_main-1.jpeg",
                "Attack on Titan Final Season"
            )
        )

        imageSlider.setImageList(imageList, ScaleTypes.FIT)

        // Inflate the layout for this fragment
        return view
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun SaveHistory(idUser: String, media:String, idMovie: String, nameMovie:String, poster:String) {
        db = FirebaseDatabase.getInstance().getReference("History")
        val date = LocalDateTime.now()
        val formattedDateTime = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))
        val history = History(idUser, media, idMovie, nameMovie,formattedDateTime.toString(), poster)
        db.push().setValue(history)
    }
    data class MovieResponse(
        val create_by : String,
        val description : String,
        val favorite_count : Int,
        val id : Int,
        val items : List<MovieData>
    )
    data class TVShowResponse(
        val create_by : String,
        val description : String,
        val favorite_count : Int,
        val id : Int,
        val items : List<TVShowData>
    )


    companion object {

    }

}