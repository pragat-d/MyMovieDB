package com.example.mymoviedb

import EndlessRecyclerViewScrollListener
import android.app.SearchManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.movies_genre.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class GenreMoviesFragment : Fragment(){
    var mPageIndex : Int = 0
    var mMoviesGenreAdapter: GenreMoviesAdapter? = null
    var mIsScrolling: Boolean = false
    var mCurrentItems : Int = 0
    var mTotalItems : Int = 0
    var mScrolledOutItems : Int = 0
    var mLinearLayoutManager : LinearLayoutManager ?=null
    var mBaseUrl : String = "https://api.themoviedb.org/"
    final val mAPIKey : String = "5ee0761a00910666db31f9b99edfd210"
    var mGenreBundle : Bundle?=null
    var mGenreId : Int = 0
    var mListGenreMovies : MutableList<GenreMoviesDesc> = arrayListOf()
    var mListGenreSearchMovies : MutableList<GenreMovieSearchDesc> = arrayListOf()
    lateinit var mGenreMovieResult : GenreMovieResult
    lateinit var mGenreMovieSearchResult: GenreMovieSearchResult
    var ifSearch : Boolean = false
    var searchQueryString : String = ""

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.movies_genre, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }



    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity?)!!.setSupportActionBar(movieGenreToolbar)
        movieGenrePageTitle.setText(arguments?.getCharSequence("genre"))
        movieGenrePageTitle.visibility = TextView.VISIBLE
        movieGenreToolbar.setTitle("")
        movieGenreToolbar.setNavigationOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                var fragmentManager: FragmentManager? = getFragmentManager()
                fragmentManager?.popBackStackImmediate()
            }

        })
        var response: Response<GenreMovieResult>?=null
        mLinearLayoutManager = LinearLayoutManager(context)
        genreMovieRecycleView.layoutManager = mLinearLayoutManager
        mMoviesGenreAdapter = GenreMoviesAdapter(object : GenreMoviesAdapter.OnMovieClickListener {
            override fun onMovieClickListener(position: Int) {
                var movieId = mListGenreMovies.get(position).id
                var movieImageUrl =
                    "https://image.tmdb.org/t/p/w500" + mListGenreMovies.get(position).poster_path
                var fragmentTransaction: FragmentTransaction? = null
                var fragmentManager: FragmentManager? = getFragmentManager()
                var moviesFragment = MovieDetailsFragments()
                var movieData = Bundle()
                movieData.putInt("movieId", movieId)
                movieData.putCharSequence("movieImageUrl", movieImageUrl)
                moviesFragment.arguments = movieData
                fragmentTransaction = fragmentManager?.beginTransaction()
                fragmentTransaction?.addToBackStack(null)
                fragmentTransaction?.hide(fragmentManager?.findFragmentByTag("GenresMovies")!!)
                fragmentTransaction?.add(R.id.fragmentContainer, moviesFragment, "Movie Details")
                    ?.commit()
            }
        })
        genreMovieRecycleView.adapter = mMoviesGenreAdapter
        fetchData(1)
        genreMovieRecycleView.addOnScrollListener(object : EndlessRecyclerViewScrollListener(
            mLinearLayoutManager!!
        ) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                if(ifSearch == false)
                    fetchData(page + 1)
                else
                    fetchSearchData(searchQueryString,page+1)
            }
        })
    }

    private fun fetchData(pageIndex: Int)  {
        ifSearch = false
        if(pageIndex == 1){
            clearAdapter(mMoviesGenreAdapter)
            mListGenreMovies = arrayListOf()

        }
        var finalResponse : Response<GenreMovieResult>?=null
        mGenreId = arguments?.getInt("genreId")!!
        var genreMoviesRetrofit : Retrofit = Retrofit.Builder().baseUrl(mBaseUrl).addConverterFactory(
            GsonConverterFactory.create()
        ).build()
        var tmdbJsonHolderApi : TMDBJsonHolderApi = genreMoviesRetrofit.create(TMDBJsonHolderApi::class.java)
        var moviesLoading  = ProgressBar(context)
        moviesLoading = loadingEndlessScroll
        moviesLoading.visibility = View.VISIBLE
        var genreMoviesCall : Call<GenreMovieResult> = tmdbJsonHolderApi.getMoviesPerGenre(
            mAPIKey,
            mGenreId,
            pageIndex
        )
        genreMoviesCall.enqueue(object : Callback<GenreMovieResult> {
            override fun onFailure(call: Call<GenreMovieResult>?, t: Throwable?) {
                Toast.makeText(context, "Error : " + t?.message, Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<GenreMovieResult>?,
                response: Response<GenreMovieResult>?
            ) {
                if (!response?.isSuccessful!!) {
                    Toast.makeText(context, response.message(), Toast.LENGTH_LONG)
                    return;
                }
                if (response != null) {
                    mGenreMovieResult = response?.body()!!
                    mListGenreMovies.addAll(mGenreMovieResult.results)
                    moviesLoading.visibility = View.GONE
                    mMoviesGenreAdapter?.setGenreMovies(mListGenreMovies)
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.genre_movies_toolbar_layout, menu)
        val menuItem = menu!!.findItem(R.id.genreMovieSearch)
        val searchView : SearchView = menuItem.actionView as SearchView
        searchView.queryHint = "Search Movie"
        searchView?.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                searchQueryString = query
                fetchSearchData(query,1)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText.equals("")){
                    fetchData(1)
                }
                return true
            }
        })
    }

    fun fetchSearchData(query : String,page: Int){
        ifSearch = true
        if(page == 1){
            clearAdapter(mMoviesGenreAdapter)
            mListGenreMovies = arrayListOf()

        }
        var finalResponse : Response<GenreMovieSearchResult>?=null

        var genreSearchMoviesRetrofit : Retrofit = Retrofit.Builder().baseUrl(mBaseUrl).addConverterFactory(GsonConverterFactory.create()).build()
        var tmdbJsonHolderApi : TMDBJsonHolderApi = genreSearchMoviesRetrofit.create(TMDBJsonHolderApi::class.java)
        var moviesLoading  = ProgressBar(context)
        moviesLoading = loadingEndlessScroll
        moviesLoading.visibility = View.VISIBLE
        var genreMovieSearchCall : Call<GenreMovieSearchResult> = tmdbJsonHolderApi.getSearchMovies(mAPIKey,query,page)
        genreMovieSearchCall.enqueue(object : Callback<GenreMovieSearchResult>{
            override fun onFailure(call: Call<GenreMovieSearchResult>?, t: Throwable?) {
                Toast.makeText(context,"Error : "+t?.message,Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<GenreMovieSearchResult>?,
                response: Response<GenreMovieSearchResult>?
            ) {
                if(!response?.isSuccessful!!){
                    Toast.makeText(context,response.message(), Toast.LENGTH_LONG)
                    return;
                }
                if (response != null){
                    mGenreMovieSearchResult = response?.body()!!
                    mListGenreMovies.addAll(mGenreMovieSearchResult.results)
                    moviesLoading.visibility = View.GONE
                    mMoviesGenreAdapter?.setGenreMovies(mListGenreMovies)
                }
            }
        })
    }

    fun clearAdapter(adapter : GenreMoviesAdapter?){
        adapter?.clearData()
    }
}
