package com.homeworks.finalexam

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.homeworks.finalexam.databinding.ItemMovieListBinding

class RecommendMovieAdapter(
    private val movies: List<Movie>
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

    inner class MovieViewHolder(private val binding: ItemMovieListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie) {
            binding.tvMovieTitle.text = movie.title
            binding.tvRating.text = movie.rating
            binding.ivFavorite.setImageResource(
                if (movie.isFavorite) R.drawable.ic_favorite else R.drawable.ic_favorite_border
            )
        }
    }
}