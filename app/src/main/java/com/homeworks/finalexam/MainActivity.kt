package com.homeworks.finalexam

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.homeworks.finalexam.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var recMvAdapter: RecommendMovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpRv()
    }

    private fun setUpRv() {

        val sampleMovieList = listOf(
            Movie("Inception", "8.8", true),
            Movie("The Dark Knight", "9.0", false),
            Movie("Interstellar", "8.6", true),
            Movie("Parasite", "8.6", false)
        )

        recMvAdapter = RecommendMovieAdapter(movies = sampleMovieList)
        binding.rvRecommendedMovies.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvRecommendedMovies.adapter = recMvAdapter

        binding.rvNowPlayingMovies.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvNowPlayingMovies.adapter = recMvAdapter


    }
}