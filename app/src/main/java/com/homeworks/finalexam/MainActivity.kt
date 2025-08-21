package com.homeworks.finalexam

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.homeworks.finalexam.databinding.ActivityMainBinding
import androidx.lifecycle.ViewModelProvider
import android.content.Intent

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var recMvAdapter: RecommendMovieAdapter
    private lateinit var nowPlayingAdapter: RecommendMovieAdapter
    private lateinit var viewModel: MoviesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[MoviesViewModel::class.java]
        setUpRv()
        observeData()
        viewModel.loadAll()
    }

    private fun setUpRv() {
        recMvAdapter = RecommendMovieAdapter(mutableListOf(), this::openDetail, viewModel::toggleFavorite)
        nowPlayingAdapter = RecommendMovieAdapter(mutableListOf(), this::openDetail, viewModel::toggleFavorite)

        binding.rvRecommendedMovies.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvRecommendedMovies.adapter = recMvAdapter

        binding.rvNowPlayingMovies.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvNowPlayingMovies.adapter = nowPlayingAdapter
    }

    private fun observeData() {
        viewModel.popular.observe(this) { recMvAdapter.submitList(it ?: emptyList()) }
        viewModel.nowPlaying.observe(this) { nowPlayingAdapter.submitList(it ?: emptyList()) }
        viewModel.favoriteState.observe(this) {
            recMvAdapter.notifyDataSetChanged()
            nowPlayingAdapter.notifyDataSetChanged()
        }
    }

    private fun openDetail(movie: Movie) {
        val intent = Intent(this, MovieDetailActivity::class.java)
        intent.putExtra("movie_id", movie.id)
        startActivity(intent)
    }
}