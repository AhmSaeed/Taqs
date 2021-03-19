package com.iti.mad41.taqs.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iti.mad41.taqs.data.model.DailyItem
import com.iti.mad41.taqs.databinding.DayCardItemBinding

class DailyWeatherAdapter(var data: List<DailyItem>): RecyclerView.Adapter<DailyWeatherAdapter.DailyItemHolder>() {
    class DailyItemHolder(var view: DayCardItemBinding): RecyclerView.ViewHolder(view.root){
        fun bind(dailyItem: DailyItem){
            view.daily = dailyItem
            view.executePendingBindings()
        }
    }

    fun setWeatherData(list: MutableList<DailyItem>) {
        data = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyItemHolder {
        val viewBinding = DayCardItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DailyItemHolder(viewBinding)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: DailyItemHolder, position: Int) {
        val habit: DailyItem = data[position]
        holder.bind(habit)
    }
}