package com.homeworks.finalexam

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.homeworks.finalexam.databinding.ActivityMovieDetailBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class MovieDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMovieDetailBinding
    private val repository = MoviesRepository(TmdbApi.create())
    private lateinit var castAdapter: CastAdapter
    private var currentMovieId: Int = -1
    
    companion object {
        private const val PERMISSION_REQUEST_CODE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        
        // Also set click listener on the parent FrameLayout that contains the back button
        binding.frameLayout.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val id = intent.getIntExtra("movie_id", -1)
        currentMovieId = id
        castAdapter = CastAdapter(mutableListOf())
        binding.rvCasts.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvCasts.adapter = castAdapter
        
        if (id != -1) {
            checkNetworkPermission(id)
        }

        binding.btnBookTickets.setOnClickListener {
            val title = binding.tvTitle.text?.toString()?.trim()
            val query = if (!title.isNullOrEmpty()) "https://www.google.com/search?q=" +
                    java.net.URLEncoder.encode("$title tickets", "UTF-8") else "https://www.google.com/search?q=movie+tickets"
            val intent = Intent(Intent.ACTION_VIEW, android.net.Uri.parse(query))
            startActivity(intent)
        }
    }

    private fun loadDetail(id: Int) {
        lifecycleScope.launch {
            val detail = repository.fetchDetail(id)
            val credits = repository.fetchCredits(id)

            val photoUrl = detail.backdropPath?.let { "https://image.tmdb.org/t/p/w780$it" }
            if (photoUrl != null) Glide.with(binding.ivPoster).load(photoUrl).into(binding.ivPoster)

            binding.tvTitle.text = detail.title
            binding.tvVoteAverage.text = String.format(Locale.getDefault(), "%.1f%%", detail.voteAverage)
            binding.tvCountry.text = detail.productionCountries.firstOrNull()?.iso31661 ?: ""
            binding.tvReleaseDate.text = formatDate(detail.releaseDate)
            binding.tvVoteCount.text = detail.voteCount.toString()
            binding.tvRuntime.text = formatRuntime(detail.runtime ?: 0)
            binding.tvGenres.text = detail.genres.joinToString(", ") { it.name }
            binding.tvLanguage.text = detail.spokenLanguages.firstOrNull()?.englishName ?: ""
            binding.tvOverview.text = detail.overview

            // simple cast strip: first three
            castAdapter.submit(credits.cast)

            // favorite
            fun renderFavorite() {
                val isFav = FavoritesStore.contains(detail.id)
                binding.ivFavDetail.setImageResource(if (isFav) R.drawable.ic_favorite else R.drawable.ic_favorite_border)
            }
            renderFavorite()
            binding.ivFavDetail.setOnClickListener {
                FavoritesStore.toggle(detail.id)
                renderFavorite()
                sendBroadcast(Intent("com.homeworks.finalexam.FAVORITES_CHANGED"))
            }
        }
    }

    private fun formatDate(raw: String?): String {
        if (raw.isNullOrBlank()) return ""
        return try {
            val inFmt = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val outFmt = SimpleDateFormat("MMMM d, yyyy", Locale.getDefault())
            outFmt.format(inFmt.parse(raw)!!)
        } catch (_: Throwable) { raw }
    }

    private fun formatRuntime(mins: Int): String {
        val h = mins / 60
        val m = mins % 60
        return "${h}hr ${m} min"
    }
    
    private fun checkNetworkPermission(movieId: Int) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_NETWORK_STATE),
                PERMISSION_REQUEST_CODE
            )
        } else {
            loadDataBasedOnNetworkAvailability(movieId)
        }
    }
    
    private fun loadDataBasedOnNetworkAvailability(movieId: Int) {
        if (NetworkUtils.isNetworkAvailable(this)) {
            loadDetail(movieId)
        } else {
            Toast.makeText(this, "No internet connection available. Please check your network settings.", Toast.LENGTH_LONG).show()
        }
    }
    
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    loadDataBasedOnNetworkAvailability(currentMovieId)
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
            checkNetworkPermission(currentMovieId)
        }
    }
    

}


