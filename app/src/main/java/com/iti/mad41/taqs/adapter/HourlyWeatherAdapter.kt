package com.iti.mad41.taqs.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.iti.mad41.taqs.data.model.HourlyItem
import com.iti.mad41.taqs.databinding.DayCardItemBinding
import com.iti.mad41.taqs.databinding.HourCardItemBinding
import com.iti.mad41.taqs.home.HomeViewModel

class HourlyWeatherAdapter(private val viewModel: HomeViewModel):
        ListAdapter<HourlyItem, HourlyWeatherAdapter.HourlyItemHolder>(HourlyWeatherDiffCallback()) {

    class HourlyItemHolder(var binding: HourCardItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(viewModel: HomeViewModel, dailyItem: HourlyItem){
            binding.homeView = viewModel
            binding.hourly = dailyItem
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): HourlyItemHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = HourCardItemBinding.inflate(layoutInflater, parent, false)

                return HourlyItemHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyItemHolder {
        val viewBinding = HourCardItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HourlyItemHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: HourlyItemHolder, position: Int) {
        val hourlyItem = getItem(position)

        holder.bind(viewModel, hourlyItem)
    }
}

class HourlyWeatherDiffCallback: DiffUtil.ItemCallback<HourlyItem>(){
    override fun areItemsTheSame(oldItem: HourlyItem, newItem: HourlyItem): Boolean {
        return oldItem.dt == newItem.dt
    }

    override fun areContentsTheSame(oldItem: HourlyItem, newItem: HourlyItem): Boolean {
        return oldItem == newItem
    }

}