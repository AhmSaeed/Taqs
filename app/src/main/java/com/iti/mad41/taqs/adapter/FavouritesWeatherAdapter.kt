package com.iti.mad41.taqs.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.iti.mad41.taqs.data.model.HourlyItem
import com.iti.mad41.taqs.data.model.WeatherNode
import com.iti.mad41.taqs.databinding.FavouriteCardItemBinding
import com.iti.mad41.taqs.databinding.HourCardItemBinding
import com.iti.mad41.taqs.favourites.FavouritesViewModel
import com.iti.mad41.taqs.home.HomeViewModel

class FavouritesWeatherAdapter(private val viewModel: FavouritesViewModel):
        ListAdapter<WeatherNode, FavouritesWeatherAdapter.WeatherNodeHolder>(FavouriteWeatherDiffCallback()) {

    class WeatherNodeHolder(var binding: FavouriteCardItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(viewModel: FavouritesViewModel, weatherNode: WeatherNode){
            binding.favouriteView = viewModel
            binding.favouriteItem = weatherNode
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): WeatherNodeHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FavouriteCardItemBinding.inflate(layoutInflater, parent, false)

                return WeatherNodeHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherNodeHolder {
        val viewBinding = FavouriteCardItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WeatherNodeHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: WeatherNodeHolder, position: Int) {
        val weatherNode = getItem(position)

        holder.bind(viewModel, weatherNode)
    }
}

class FavouriteWeatherDiffCallback: DiffUtil.ItemCallback<WeatherNode>(){
    override fun areItemsTheSame(oldItem: WeatherNode, newItem: WeatherNode): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: WeatherNode, newItem: WeatherNode): Boolean {
        return oldItem == newItem
    }

}