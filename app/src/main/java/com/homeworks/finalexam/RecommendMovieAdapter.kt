package com.homeworks.finalexam

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.homeworks.finalexam.databinding.ItemMovieListBinding

class RecommendMovieAdapter(
    private val movies: MutableList<Movie>,
    private val onItemClick: (Movie) -> Unit,
    private val onToggleFavorite: (Movie) -> Unit
) : RecyclerView.Adapter<RecommendMovieAdapter.MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieListBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]
        holder.bind(movie)
    }

    override fun getItemCount(): Int = movies.size

    fun submitList(newItems: List<Movie>) {
        movies.clear()
        movies.addAll(newItems)
        notifyDataSetChanged()
    }

    inner class MovieViewHolder(private val binding: ItemMovieListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie) {
            binding.tvMovieTitle.text = movie.title
            binding.tvRating.text = String.format("%.1f%%", movie.voteAverage)
            val url = movie.posterPath?.let { "https://image.tmdb.org/t/p/w500$it" }
            if (url != null) {
                Glide.with(binding.ivMoviePoster).load(url).into(binding.ivMoviePoster)
            } else {
                binding.ivMoviePoster.setImageResource(R.drawable.ic_launcher_foreground)
            }
            binding.ivFavorite.setImageResource(
                if (movie.isFavorite) R.drawable.ic_favorite else R.drawable.ic_favorite_border
            )
            binding.ivFavorite.setOnClickListener {
                onToggleFavorite(movie)
            }
            binding.root.setOnClickListener { onItemClick(movie) }
        }
    }
}