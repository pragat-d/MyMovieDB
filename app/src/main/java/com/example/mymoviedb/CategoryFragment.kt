package com.example.mymoviedb


import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.internal.ContextUtils.getActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.genre_view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body

class CategoryFragment() : Fragment() {

    companion object {
        fun newInstance() = CategoryFragment()
        }



    lateinit var genreBody : MovieGenre
    lateinit var test : Body
    var genreList : List<GenreDetails> = arrayListOf()

    final val api_key : String = "5ee0761a00910666db31f9b99edfd210"
    var mGenreAdapter : GenreGridAdapter?=null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.genre_view,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        genreToolbar.setTitle("MyMovieDB")
        var genreRecyclerView = genreGridRecyclerView
        var gridLayoutManager =  GridLayoutManager(context,2, GridLayoutManager.VERTICAL,false)
        genreRecyclerView.layoutManager = gridLayoutManager
        mGenreAdapter = GenreGridAdapter(object : GenreGridAdapter.OnGenreClickListener{
            override fun onGenreClick(position: Int) {
                var genreId = genreList.get(position).id
                var genreName = genreList.get(position).name
                var fragmentTransaction : FragmentTransaction?=null
                var fragmentManager : FragmentManager? = getFragmentManager()
                var genreMoviesFragment = GenreMoviesFragment()
                var genreData = Bundle()
                genreData.putInt("genreId",genreId)
                genreData.putCharSequence("genre",genreName)
                genreMoviesFragment.arguments = genreData
                fragmentTransaction = fragmentManager?.beginTransaction()
                fragmentTransaction?.addToBackStack(null)
                fragmentTransaction?.hide(fragmentManager?.findFragmentByTag("Genres")!!)
                fragmentTransaction?.add(R.id.fragmentContainer,genreMoviesFragment,"GenresMovies")?.commit()

            }
        })
        genreRecyclerView.adapter = mGenreAdapter
        var retrofit : Retrofit = Retrofit.Builder().baseUrl("https://api.themoviedb.org/").addConverterFactory(
            GsonConverterFactory.create()
        ).build()

        var TMDBJsonHolderApi : TMDBJsonHolderApi = retrofit.create(TMDBJsonHolderApi::class.java)
        var genreLoading  = ProgressBar(context)
        genreLoading = genreProgressBar
        genreLoading.visibility = View.VISIBLE
        var genreCall : Call<MovieGenre> = TMDBJsonHolderApi.getGenre(api_key)
        genreCall.enqueue(object: Callback<MovieGenre> {
            override fun onFailure(call: Call<MovieGenre>?, t: Throwable?) {
                Toast.makeText(context,t?.message, Toast.LENGTH_LONG).show()
            }
            override fun onResponse(
                call: Call<MovieGenre>?,
                response: Response<MovieGenre>?
            ) {
                if(!response?.isSuccessful!!){
                    Toast.makeText(context,response.message(), Toast.LENGTH_LONG)
                    return;
                }
                genreLoading.visibility = View.INVISIBLE
                genreBody = response.body()
                genreList  = genreBody.mGenreList
                mGenreAdapter!!.setGenreData(genreList)
            }
        })
    }
}