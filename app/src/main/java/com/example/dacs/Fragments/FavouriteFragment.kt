package com.example.dacs.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dacs.Adapter.SearchAdapter
import com.example.dacs.Data.SearchData
import com.example.dacs.R
import com.example.dacs.WatchMovie
import com.example.dacs.databinding.FragmentFavouriteBinding
import com.google.gson.Gson
import java.net.URL
import java.util.*


private lateinit var binding: FragmentFavouriteBinding
class FavouriteFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var searchAdapter: SearchAdapter
    private var searchMovie = mutableListOf<SearchData>()
    private lateinit var searchView: SearchView
    private var searchList = mutableListOf<SearchData>()
//    private val originalMovieList = mutableListOf<SearchData>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentFavouriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = binding.movieRecyclerView

        val bundle = arguments
        val name = bundle?.getString("name")
        val id = bundle?.getString("id")

        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        searchAdapter = SearchAdapter(searchMovie)
        recyclerView.adapter = searchAdapter

        Thread {
            //Xoa du lieu de chuan bi du lieu moi
            searchList.clear()
            searchMovie.clear()
//            originalMovieList.clear()


            val url = "https://api.themoviedb.org/3/list/8248497?api_key=eb82a323e426d30d552550d47bc83e2b"
            val response = URL(url).readText()
            val gson = Gson()

            val searchResponse = gson.fromJson(response, SearchResponse::class.java)

//            originalMovieList.addAll(searchResponse.items)
            searchMovie.addAll(searchResponse.items)
            searchList.addAll(searchResponse.items)

            activity?.runOnUiThread {
                searchAdapter = SearchAdapter(searchList)
                recyclerView.adapter = searchAdapter
                searchAdapter.setOnItemClickListener(object : SearchAdapter.OnItemClickListener {
                    override fun onItemClick(position: Int) {
                        val searchshow = searchList[position]
                        val intent = Intent(context, WatchMovie::class.java)
                        intent.putExtra("movieId", searchshow.id)
                        intent.putExtra("movieTitle", searchshow.title)
                        intent.putExtra("movieOverview", searchshow.overview)
                        intent.putExtra("moviePoster", searchshow.poster_path)
                        intent.putExtra("name", name)
                        intent.putExtra("id", id)
                        startActivity(intent)
                    }
                })
            }
        }.start()

        searchView = binding.search
        searchView.clearFocus() // giữ kết quả hiển thị tránh bị che bởi bàn phím
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchList.clear()
                val searchText = newText?.toLowerCase(Locale.getDefault()) ?: ""

                if (searchText.isNotEmpty()) {
                    searchMovie.forEach{
                        if (it.title?.toLowerCase(Locale.getDefault())?.contains(searchText) == true || it.name?.toLowerCase(Locale.getDefault())?.contains(searchText) == true){
                            searchList.add(it)
                        }
                    }
                    recyclerView.adapter!!.notifyDataSetChanged()
                } else {
                    searchList.clear()
                    searchList.addAll(searchMovie)
                    recyclerView.adapter!!.notifyDataSetChanged()
                }
                return false
            }
        })
    }




    data class SearchResponse(
        val create_by : String,
        val description : String,
        val favorite_count : Int,
        val id : Int,
        val items : List<SearchData>
    )

}