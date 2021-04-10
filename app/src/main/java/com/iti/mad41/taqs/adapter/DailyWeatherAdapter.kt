package com.iti.mad41.taqs.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.iti.mad41.taqs.data.model.DailyItem
import com.iti.mad41.taqs.data.model.WeatherNode
import com.iti.mad41.taqs.databinding.DayCardItemBinding
import com.iti.mad41.taqs.home.HomeViewModel
import java.text.SimpleDateFormat

class DailyWeatherAdapter:
        ListAdapter<DailyItem, DailyWeatherAdapter.DailyItemHolder>(DailyWeatherDiffCallback()) {

    class DailyItemHolder(var binding: DayCardItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(dailyItem: DailyItem){
            binding.daily = dailyItem
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): DailyItemHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = DayCardItemBinding.inflate(layoutInflater, parent, false)

                return DailyItemHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyItemHolder {
        val viewBinding = DayCardItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DailyItemHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: DailyItemHolder, position: Int) {
        val dailyItem = getItem(position)

        holder.bind(dailyItem)
    }
}

class DailyWeatherDiffCallback: DiffUtil.ItemCallback<DailyItem>(){
    override fun areItemsTheSame(oldItem: DailyItem, newItem: DailyItem): Boolean {
        return oldItem.dt == newItem.dt
    }

    override fun areContentsTheSame(oldItem: DailyItem, newItem: DailyItem): Boolean {
        return oldItem == newItem
    }

}