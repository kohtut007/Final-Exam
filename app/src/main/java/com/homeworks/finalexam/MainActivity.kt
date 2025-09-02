package com.homeworks.finalexam

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.homeworks.finalexam.databinding.ActivityMainBinding
import androidx.lifecycle.ViewModelProvider
import android.content.Intent

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var popularAdapter: RecommendMovieAdapter
    private lateinit var nowPlayingAdapter: RecommendMovieAdapter
    private lateinit var upcomingAdapter: RecommendMovieAdapter
    private lateinit var viewModel: MoviesViewModel
    
    companion object {
        private const val PERMISSION_REQUEST_CODE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[MoviesViewModel::class.java]
        checkNetworkPermission()

        setUpRv()
        observeData()
        
    }

    private fun setUpRv() {
        popularAdapter = RecommendMovieAdapter(mutableListOf(), this::openDetail, viewModel::toggleFavorite)
        nowPlayingAdapter = RecommendMovieAdapter(mutableListOf(), this::openDetail, viewModel::toggleFavorite)
        upcomingAdapter = RecommendMovieAdapter(mutableListOf(), this::openDetail, viewModel::toggleFavorite)

        binding.rvRecommendedMovies.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvRecommendedMovies.adapter = popularAdapter

        binding.rvNowPlayingMovies.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvNowPlayingMovies.adapter = nowPlayingAdapter

        binding.rvUpcomingMovies.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvUpcomingMovies.adapter = upcomingAdapter
    }

    private fun observeData() {
        viewModel.popular.observe(this) { popularAdapter.submitList(it ?: emptyList()) }
        viewModel.nowPlaying.observe(this) { nowPlayingAdapter.submitList(it ?: emptyList()) }
        viewModel.upcoming.observe(this) { upcomingAdapter.submitList(it ?: emptyList()) }
        viewModel.favoriteState.observe(this) {
            popularAdapter.notifyDataSetChanged()
            nowPlayingAdapter.notifyDataSetChanged()
            upcomingAdapter.notifyDataSetChanged()
        }
    }

    private fun openDetail(movie: Movie) {
        val intent = Intent(this, MovieDetailActivity::class.java)
        intent.putExtra("movie_id", movie.id)
        startActivity(intent)
    }
    
    private fun checkNetworkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_NETWORK_STATE),
                PERMISSION_REQUEST_CODE
            )
        } else {
            loadDataBasedOnNetworkAvailability()
        }
    }
    
    private fun loadDataBasedOnNetworkAvailability() {
        if (NetworkUtils.isNetworkAvailable(this)) {
            viewModel.loadAll()
        } else {
            Toast.makeText(this, "No internet connection available. Please check your network settings.", Toast.LENGTH_LONG).show()
        }
    }
    
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    loadDataBasedOnNetworkAvailability()
                } else {
                    Toast.makeText(this, "Network state permission denied. Some features may not work properly.", Toast.LENGTH_LONG).show()
                }
                return
            }
        }
    }
    
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            checkNetworkPermission()
        }
    }
}